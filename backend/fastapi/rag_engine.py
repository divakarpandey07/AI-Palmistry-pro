import os
import logging
from typing import Dict

from langchain_community.embeddings import HuggingFaceEmbeddings
from langchain_community.vectorstores import SupabaseVectorStore
from langchain_groq import ChatGroq
from langchain.prompts import PromptTemplate
from supabase.client import Client, create_client

# ---------------------------------------------------------------------------
# Environment configuration (Render will provide these as env vars)
# ---------------------------------------------------------------------------
SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_KEY = os.getenv("SUPABASE_KEY")
GROQ_API_KEY = os.getenv("GROQ_API_KEY")

if not all([SUPABASE_URL, SUPABASE_KEY, GROQ_API_KEY]):
    raise EnvironmentError("Missing one of SUPABASE_URL, SUPABASE_KEY, GROQ_API_KEY env variables")

# ---------------------------------------------------------------------------
# Supabase vector store (pgvector) – holds the three palmistry book chunks
# ---------------------------------------------------------------------------
supabase_client: Client = create_client(SUPABASE_URL, SUPABASE_KEY)
embeddings = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")

vector_store = SupabaseVectorStore(
    client=supabase_client,
    embedding=embeddings,
    table_name="palmistry_books_knowledge",
    query_name="match_palmistry_documents",
)

# ---------------------------------------------------------------------------
# LLM – Groq hosted Llama‑3 8B (free tier)
# ---------------------------------------------------------------------------
llm = ChatGroq(
    temperature=0.2,
    model_name="llama3-8b-8192",
    api_key=GROQ_API_KEY,
)

# ---------------------------------------------------------------------------
# Prompt template – we want the model to answer only from the supplied context
# ---------------------------------------------------------------------------
prompt_template = PromptTemplate(
    input_variables=["context", "metadata"],
    template=(
        "You are a professional palmistry interpreter. Use ONLY the information provided in the "
        "context (which comes from three trusted palmistry books) to answer the user's request. "
        "Do NOT hallucinate or add any extra facts.\n\n"
        "User metadata (JSON) describing life line, heart line and mounts:\n{metadata}\n\n"
        "Context excerpts (relevant book chunks):\n{context}\n\n"
        "Provide a concise, friendly reading in Hindi, bullet‑point format."
    ),
)

def _retrieve_context(user_metadata: Dict) -> str:
    """Simple similarity search – we concatenate the JSON values into a single query string.
    In a production app you would craft a richer embedding, but for demo purposes we just use the raw JSON.
    """
    query_text = json.dumps(user_metadata)
    # `similarity_search` returns a list of Document objects; we join their pages.
    docs = vector_store.similarity_search(query_text, k=4)
    return "\n---\n".join([doc.page_content for doc in docs])

def generate_reading(user_metadata: Dict) -> str:
    """Runs the full RAG pipeline (retrieve → LLM) and returns the plain reading string.
    The FastAPI endpoint will encrypt the dict that contains this string.
    """
    logging.info("🔎 Performing vector similarity search")
    context = _retrieve_context(user_metadata)
    logging.info(f"Retrieved {len(context)} characters of context")

    prompt = prompt_template.format(context=context, metadata=json.dumps(user_metadata, ensure_ascii=False))
    logging.info("💬 Sending prompt to Groq LLM")
    response = llm.invoke(prompt)
    # `response` can be a string or a dict depending on the wrapper version – handle both.
    if isinstance(response, dict) and "content" in response:
        return response["content"]
    return str(response)
