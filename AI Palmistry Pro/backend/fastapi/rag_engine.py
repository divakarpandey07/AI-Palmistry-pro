# rag_engine.py
# ---------------------------------------------------------------
# Core RAG pipeline used by FastAPI endpoint.
# ---------------------------------------------------------------
import os
import logging
from typing import List, Dict, Any

from sentence_transformers import SentenceTransformer

from .supabase_client import semantic_search

# Load the same embedding model used during ingestion.
EMBEDDER = SentenceTransformer("all-MiniLM-L6-v2")

# Safety threshold – if the best similarity distance is > 0.2 (i.e.
# low similarity) we consider the query out‑of‑scope.
SIMILARITY_THRESHOLD = 0.2

# System prompt that forces the LLM to stay within Palmistry.
SYSTEM_PROMPT = (
    "You are a professional Palmistry AI. Answer ONLY based on the provided context. "
    "If the context does not contain information relevant to the user's query, "
    "respond with: \"I am a Palmistry AI and can only discuss hand lines and mounts.\""
)

def embed_query(question: str) -> List[float]:
    """Return the embedding vector for a user question."""
    return EMBEDDER.encode(question).tolist()

def build_prompt(context_chunks: List[Dict[str, Any]], question: str) -> str:
    """Combine system prompt, retrieved context and the user question.

    The LLM receives a single string; we keep it concise for token budget.
    """
    context_text = "\n\n---\n\n".join(chunk["chunk_text"] for chunk in context_chunks)
    return f"{SYSTEM_PROMPT}\n\nContext:{context_text}\n\nQuestion: {question}"

def generate_reading(question: str, metadata: Dict[str, Any] = None) -> str:
    """Run the full RAG flow and return the LLM answer.

    1. Embed the question.
    2. Retrieve the top‑k similar chunks from Supabase.
    3. If similarity is below the safety threshold, abort.
    4. Build the prompt and call the LLM (Groq API).
    5. Return the raw answer string.
    """
    logging.info("🔎 Embedding user question")
    query_vec = embed_query(question)

    logging.info("📚 Performing semantic search")
    results = semantic_search(query_vec, top_k=5)
    if not results:
        raise RuntimeError("No knowledge base results returned")

    # The `<=>` operator in Supabase returns cosine distance; lower is more similar.
    best_distance = results[0].get("distance", 1.0) if isinstance(results[0], dict) else 1.0
    logging.debug(f"Best similarity distance: {best_distance}")
    if best_distance > SIMILARITY_THRESHOLD:
        raise ValueError("Query out of scope – similarity below threshold")

    prompt = build_prompt(results, question)
    logging.info("🤖 Calling LLM (Groq) to generate reading")
    # -------------------------------------------------------------------
    # Groq API call – you must set GROQ_API_KEY in environment.
    # -------------------------------------------------------------------
    import httpx
    GROQ_API_KEY = os.getenv("GROQ_API_KEY")
    if not GROQ_API_KEY:
        raise RuntimeError("GROQ_API_KEY environment variable not set")

    headers = {"Authorization": f"Bearer {GROQ_API_KEY}", "Content-Type": "application/json"}
    payload = {
        "model": "mixtral-8x7b-32768",  # Replace with Llama‑3‑8B if available on Groq
        "messages": [{"role": "user", "content": prompt}],
        "temperature": 0.2,
        "max_tokens": 1024,
    }
    response = httpx.post("https://api.groq.com/openai/v1/chat/completions", json=payload, headers=headers, timeout=30)
    response.raise_for_status()
    data = response.json()
    answer = data["choices"][0]["message"]["content"].strip()
    return answer
