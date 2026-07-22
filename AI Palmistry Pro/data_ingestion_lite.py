# data_ingestion_lite.py
# ---------------------------------------------------------------
# Lightweight ingestion script that avoids torch/sentence-transformers.
# Uses Supabase REST API directly + HuggingFace Inference API
# for embeddings (all-MiniLM-L6-v2) — no local GPU/torch needed.
# ---------------------------------------------------------------
import os
import uuid
import json
import fitz          # PyMuPDF
import httpx
from pathlib import Path

# ---------------------------------------------------------------
# Credentials from environment
# ---------------------------------------------------------------
SUPABASE_URL     = os.getenv("SUPABASE_URL", "https://vvblxfplyncbqlmzsjus.supabase.co")
SUPABASE_ANON_KEY = os.getenv("SUPABASE_ANON_KEY", "")
HF_API_KEY       = os.getenv("HF_API_KEY", "")   # optional – works without key (rate-limited)

EMBED_API = "https://api-inference.huggingface.co/pipeline/feature-extraction/sentence-transformers/all-MiniLM-L6-v2"

CHUNK_SIZE    = 1500   # characters
CHUNK_OVERLAP = 300

# ---------------------------------------------------------------
# Helper: chunk plain text
# ---------------------------------------------------------------
def chunk_text(text: str) -> list[str]:
    chunks = []
    start = 0
    while start < len(text):
        end = start + CHUNK_SIZE
        chunks.append(text[start:end].strip())
        start += CHUNK_SIZE - CHUNK_OVERLAP
    return [c for c in chunks if len(c) > 50]   # skip tiny chunks

# ---------------------------------------------------------------
# Helper: get embedding via HuggingFace Inference API
# ---------------------------------------------------------------
def get_embedding(text: str) -> list[float]:
    headers = {"Content-Type": "application/json"}
    if HF_API_KEY:
        headers["Authorization"] = f"Bearer {HF_API_KEY}"
    resp = httpx.post(
        EMBED_API,
        json={"inputs": text, "options": {"wait_for_model": True}},
        headers=headers,
        timeout=60
    )
    resp.raise_for_status()
    result = resp.json()
    # API returns list-of-lists for batch; we sent one string so it's a nested list
    if isinstance(result[0], list):
        # Average token embeddings to get sentence embedding
        vec_len = len(result[0])
        avg = [sum(t[i] for t in result) / len(result) for i in range(vec_len)]
        return avg
    return result   # already flat list

# ---------------------------------------------------------------
# Helper: insert one chunk into Supabase via REST
# ---------------------------------------------------------------
def insert_chunk(record: dict):
    url = f"{SUPABASE_URL}/rest/v1/palmistry_books_knowledge"
    headers = {
        "apikey": SUPABASE_ANON_KEY,
        "Authorization": f"Bearer {SUPABASE_ANON_KEY}",
        "Content-Type": "application/json",
        "Prefer": "return=minimal"
    }
    resp = httpx.post(url, json=record, headers=headers, timeout=30)
    if resp.status_code not in (200, 201):
        print(f"  [WARN] Insert failed ({resp.status_code}): {resp.text}")
    else:
        print(f"  [OK] Chunk {record['id'][:8]} inserted (page {record['page']})")

# ---------------------------------------------------------------
# Process one PDF
# ---------------------------------------------------------------
def process_pdf(pdf_path: Path):
    print(f"\n[Processing]: {pdf_path.name}")
    doc = fitz.open(pdf_path)
    for page_num, page in enumerate(doc, start=1):
        text = page.get_text("text").strip()
        if not text:
            continue
        chunks = chunk_text(text)
        for chunk in chunks:
            try:
                embedding = get_embedding(chunk)
                record = {
                    "id": str(uuid.uuid4()),
                    "source": pdf_path.name,
                    "page": page_num,
                    "chunk_text": chunk,
                    "embedding": embedding
                }
                insert_chunk(record)
            except Exception as e:
                print(f"  [ERROR] on page {page_num}: {e}")

# ---------------------------------------------------------------
# Main
# ---------------------------------------------------------------
def main():
    pdf_folder = Path(__file__).parent / "pdfs"
    pdfs = list(pdf_folder.glob("*.pdf"))
    if not pdfs:
        raise RuntimeError("No PDFs found in pdfs/ folder!")
    print(f"Found {len(pdfs)} PDF(s): {[p.name for p in pdfs]}")
    for pdf in pdfs:
        process_pdf(pdf)
    print("\n[DONE] All PDFs ingested into Supabase successfully!")

if __name__ == "__main__":
    main()
