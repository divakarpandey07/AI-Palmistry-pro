# data_ingestion.py
# ---------------------------------------------------------------
# One‑time script that reads the three Palmistry PDFs, splits them
# into overlapping text chunks, creates vector embeddings and uploads
# everything to Supabase (pgvector).
# ---------------------------------------------------------------

import os
import uuid
import json
from pathlib import Path

import fitz  # PyMuPDF – install with `pip install pymupdf`
from langchain_text_splitters import RecursiveCharacterTextSplitter
from sentence_transformers import SentenceTransformer
from supabase import create_client, Client

# ---------------------------------------------------------------
# Configuration – replace with your actual Supabase credentials.
# It is safer to keep them in environment variables.
# ---------------------------------------------------------------
SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_ANON_KEY = os.getenv("SUPABASE_ANON_KEY")

if not SUPABASE_URL or not SUPABASE_ANON_KEY:
    raise RuntimeError("Supabase URL and ANON_KEY must be set as env variables")

client: Client = create_client(SUPABASE_URL, SUPABASE_ANON_KEY)

# ---------------------------------------------------------------
# Embedding model – same model used at query time for consistency.
# ---------------------------------------------------------------
EMBEDDER = SentenceTransformer("all-MiniLM-L6-v2")  # 384‑dim vectors

# ---------------------------------------------------------------
# Helper to read a PDF and return plain text per page.
# ---------------------------------------------------------------
def pdf_to_pages(pdf_path: Path) -> list[str]:
    doc = fitz.open(pdf_path)
    pages = []
    for page in doc:
        pages.append(page.get_text("text"))
    return pages

# ---------------------------------------------------------------
# Chunking – we use a 500‑token (≈ 1 500 characters) chunk size with a
# 200‑token overlap to preserve context across chunk boundaries.
# ---------------------------------------------------------------
splitter = RecursiveCharacterTextSplitter(
    chunk_size=1500,
    chunk_overlap=600,
    separators=["\n\n", "\n", " ", ""]
)

def process_pdf(pdf_path: Path, source_name: str):
    print(f"Processing {pdf_path.name} ...")
    page_texts = pdf_to_pages(pdf_path)
    for page_num, text in enumerate(page_texts, start=1):
        chunks = splitter.split_text(text)
        for chunk in chunks:
            # Create a deterministic UUID for reproducibility (optional)
            chunk_id = str(uuid.uuid4())
            embedding = EMBEDDER.encode(chunk).tolist()  # list of floats
            payload = {
                "id": chunk_id,
                "source": source_name,
                "page": page_num,
                "chunk_text": chunk,
                "embedding": embedding,
            }
            # Insert into Supabase – the table must already exist with a
            # `vector` column of type `vector(384)`.
            client.table("palmistry_books_knowledge").insert(payload).execute()
            print(f"  → uploaded chunk {chunk_id[:8]} (page {page_num})")

# ---------------------------------------------------------------
# Main driver – place your three PDFs in a folder called `pdfs/`
# inside the project root.
# ---------------------------------------------------------------
def main():
    pdf_folder = Path(__file__).parent / "pdfs"
    if not pdf_folder.is_dir():
        raise RuntimeError(f"PDF folder not found: {pdf_folder}")

    pdf_files = list(pdf_folder.glob("*.pdf"))
    if len(pdf_files) == 0:
        raise RuntimeError("No PDF files found in the pdfs/ directory")

    for pdf_path in pdf_files:
        process_pdf(pdf_path, source_name=pdf_path.name)

    print("✅ All PDFs have been ingested into Supabase!")

if __name__ == "__main__":
    main()
