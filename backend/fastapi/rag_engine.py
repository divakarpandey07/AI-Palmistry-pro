import os
import json
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
# Supabase vector stores
# ---------------------------------------------------------------------------
supabase_client: Client = create_client(SUPABASE_URL, SUPABASE_KEY)
embeddings = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")

# Books knowledge store
books_store = SupabaseVectorStore(
    client=supabase_client,
    embedding=embeddings,
    table_name="palmistry_books_knowledge",
    query_name="match_palmistry_documents",
)

# Memory (ai_experiences) store – stores past high‑quality Q&A pairs
memory_store = SupabaseVectorStore(
    client=supabase_client,
    embedding=embeddings,
    table_name="ai_experiences",
    query_name="match_ai_experiences",
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
# Prompt template – strict guardrail + dual context
# ---------------------------------------------------------------------------
PROMPT_TEMPLATE = """Aap ek strict aur professional Palmistry Expert hain.
Aapka kaam SIRF hath ki rekhaon aur astrology par baat karna hai.
Agar question palmistry se related nahi hai, toh strictly bolein: \"Main sirf palmistry ke baare mein baat kar sakta hoon.\"

Neeche diye gaye Palmistry Books ke 'Knowledge' aur purane 'Experience' ko combine karke user ko best answer dein.

--- KNOWLEDGE (From Books) ---
{book_context}

--- PAST EXPERIENCE (Similar previous answers) ---
{memory_context}

User Question: {question}
Answer:"""

prompt_template = PromptTemplate(
    input_variables=["book_context", "memory_context", "question"],
    template=PROMPT_TEMPLATE,
)

# ---------------------------------------------------------------------------
# Helper: retrieve context from both stores
# ---------------------------------------------------------------------------
def _retrieve_book_context(user_question: str) -> str:
    """Return concatenated book chunks (top 3) for the given question."""
    docs = books_store.similarity_search(user_question, k=3)
    if not docs:
        return ""
    return "\n---\n".join([doc.page_content for doc in docs])

def _retrieve_memory_context(user_question: str) -> str:
    """Return the most relevant past experience (if any)."""
    docs = memory_store.similarity_search(user_question, k=1)
    if not docs:
        return ""
    # The metadata field contains the stored answer
    past_answer = docs[0].metadata.get("ai_answer", "")
    past_question = docs[0].page_content
    return f"Question: {past_question}\nAnswer: {past_answer}"

# ---------------------------------------------------------------------------
# Core generation function with guardrail
# ---------------------------------------------------------------------------
def generate_reading(user_question: str, user_metadata: Dict) -> str:
    """Generate a palmistry reading.
    - Performs a similarity search in the book store.
    - If the similarity is too low (no hits), returns the guardrail message.
    - Otherwise combines book and memory contexts and queries Groq LLM.
    """
    logging.info("🔎 Searching book knowledge")
    book_context = _retrieve_book_context(user_question)
    if not book_context:
        # Guardrail: out‑of‑domain request
        return "Main sirf palmistry ke baare mein baat kar sakta hoon."

    logging.info("🔎 Searching memory experiences")
    memory_context = _retrieve_memory_context(user_question)

    # Build final prompt
    final_prompt = prompt_template.format(
        book_context=book_context,
        memory_context=memory_context,
        question=user_question,
    )
    logging.info("💬 Sending prompt to Groq LLM")
    response = llm.invoke(final_prompt)
    if isinstance(response, dict) and "content" in response:
        answer = response["content"]
    else:
        answer = str(response)

    # If the model somehow returned the guardrail text, we do NOT store it
    if "Main sirf palmistry" not in answer:
        # Save high‑quality answer to memory (will be filtered by thumbs‑up later)
        save_experience_to_memory(user_question, answer)
    return answer

# ---------------------------------------------------------------------------
# Persistence: save a verified experience to the memory store
# ---------------------------------------------------------------------------
def save_experience_to_memory(question: str, answer: str) -> None:
    """Insert a Q&A pair into the `ai_experiences` vector table.
    Called only after a thumbs‑up feedback (or automatically for demo).
    """
    try:
        memory_store.add_texts(
            texts=[question],
            metadatas=[{"ai_answer": answer, "timestamp": str(datetime.utcnow())}],
        )
        logging.info("✅ New experience added to AI Memory")
    except Exception as e:
        logging.error(f"Failed to save experience: {e}")
