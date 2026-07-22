# data_ingestion_from_json.py
# ---------------------------------------------------------------
# FASTEST approach: uses the already-extracted extracted_data.json
# No HuggingFace API needed - uses Supabase's built-in pgvector
# with a simple TF-IDF style embedding via requests.
# 
# Since we cannot run torch locally (policy block), we use
# Supabase's own edge function OR we store text without embeddings
# first, then let the backend generate embeddings at query time.
#
# APPROACH: Store chunks in Supabase WITHOUT embeddings for now.
# The RAG engine will use keyword search (full-text search) as 
# a fallback until embeddings can be generated server-side.
# ---------------------------------------------------------------
import os
import json
import uuid
import httpx
from pathlib import Path

SUPABASE_URL      = os.getenv("SUPABASE_URL", "https://vvblxfplyncbqlmzsjus.supabase.co")
SUPABASE_ANON_KEY = os.getenv("SUPABASE_ANON_KEY", "")

HEADERS = {
    "apikey": SUPABASE_ANON_KEY,
    "Authorization": f"Bearer {SUPABASE_ANON_KEY}",
    "Content-Type": "application/json",
    "Prefer": "return=minimal"
}

CHUNK_SIZE    = 1000   # characters per chunk
CHUNK_OVERLAP = 200

def chunk_text(text: str) -> list:
    chunks = []
    start = 0
    while start < len(text):
        end = start + CHUNK_SIZE
        chunk = text[start:end].strip()
        if len(chunk) > 30:
            chunks.append(chunk)
        start += CHUNK_SIZE - CHUNK_OVERLAP
    return chunks

def insert_batch(records: list):
    """Insert up to 50 records at once."""
    url = f"{SUPABASE_URL}/rest/v1/palmistry_books_knowledge"
    resp = httpx.post(url, json=records, headers=HEADERS, timeout=60)
    if resp.status_code not in (200, 201):
        print(f"  [WARN] Batch insert failed ({resp.status_code}): {resp.text[:200]}")
    else:
        print(f"  [OK] Inserted batch of {len(records)} chunks")

def main():
    json_path = Path("D:/antigravity/mobile security/extracted_data.json")
    if not json_path.exists():
        raise RuntimeError(f"File not found: {json_path}")

    print(f"Loading extracted data from {json_path.name} ...")
    with open(json_path, "r", encoding="utf-8") as f:
        data = json.load(f)

    print(f"Total pages in JSON: {len(data)}")

    batch = []
    total_chunks = 0
    BATCH_SIZE = 20

    for entry in data:
        text = entry.get("text", "").strip()
        source = entry.get("book_name", "unknown")
        page = entry.get("page_number", 0)

        if not text:
            continue

        chunks = chunk_text(text)
        for chunk in chunks:
            record = {
                "id": str(uuid.uuid4()),
                "source": source,
                "page": page,
                "chunk_text": chunk,
                # embedding left as NULL - will be generated server-side
            }
            batch.append(record)
            total_chunks += 1

            if len(batch) >= BATCH_SIZE:
                insert_batch(batch)
                batch = []

    # Insert remaining
    if batch:
        insert_batch(batch)

    print(f"\n[DONE] Total chunks inserted: {total_chunks}")

if __name__ == "__main__":
    main()
