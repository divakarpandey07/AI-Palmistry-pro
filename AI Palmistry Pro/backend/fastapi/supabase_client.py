# supabase_client.py
# ---------------------------------------------------------------
# Thin wrapper around the Supabase Python SDK that provides two
# helpers used by the RAG pipeline: one for inserting chunks (used by
# the ingestion script) and one for performing a similarity search.
# ---------------------------------------------------------------
import os
from typing import List, Dict, Any

from supabase import create_client, Client

# Environment variables – same ones that `data_ingestion.py` uses.
SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_ANON_KEY = os.getenv("SUPABASE_ANON_KEY")

if not SUPABASE_URL or not SUPABASE_ANON_KEY:
    raise RuntimeError("Supabase credentials missing – set SUPABASE_URL & SUPABASE_ANON_KEY")

client: Client = create_client(SUPABASE_URL, SUPABASE_ANON_KEY)

# ---------------------------------------------------------------
# Helper: search the vector store for the *n* most similar chunks.
# ---------------------------------------------------------------
def semantic_search(query_embedding: List[float], top_k: int = 5) -> List[Dict[str, Any]]:
    """Return the *top_k* most similar chunks from `palmistry_books_knowledge`.

    The Supabase table must contain a column `embedding` of type
    `vector(384)`. The `<=>` operator computes cosine distance.
    """
    response = (
        client.table("palmistry_books_knowledge")
        .select("id, source, page, chunk_text, embedding")
        .order("embedding", {
            "foreignTable": "embedding",
            "direction": "asc",
            "using": "<=>",
            "value": query_embedding,
        })
        .limit(top_k)
        .execute()
    )
    if response.get("error"):
        raise RuntimeError(f"Supabase search error: {response['error']}")
    return response.get("data", [])

# ---------------------------------------------------------------
# Helper: insert a new chunk – used by the ingestion script.
# ---------------------------------------------------------------
def insert_chunk(chunk_record: Dict[str, Any]):
    """Insert a single chunk into the vector table.

    `chunk_record` must contain the keys:
    `id`, `source`, `page`, `chunk_text`, `embedding`.
    """
    resp = client.table("palmistry_books_knowledge").insert(chunk_record).execute()
    if resp.get("error"):
        raise RuntimeError(f"Supabase insert error: {resp['error']}")
    return resp.get("data")
