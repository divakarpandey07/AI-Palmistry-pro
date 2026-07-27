import os
from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_community.embeddings import HuggingFaceEmbeddings
from langchain_community.vectorstores import SupabaseVectorStore
from supabase.client import Client, create_client

# Supabase Configuration – read from environment variables
SUPABASE_URL = os.getenv("SUPABASE_URL", "https://your-project.supabase.co")
SUPABASE_KEY = os.getenv("SUPABASE_KEY", "your-service-role-key")
supabase_client: Client = create_client(SUPABASE_URL, SUPABASE_KEY)

# Directory containing the PDF books (place your three PDFs here)
BOOKS_DIR = "./books"

def ingest_books_to_supabase():
    print("Initializing embedding model...")
    # Embedding model must match the one used in rag_engine.py
    embeddings = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")

    # Chunking strategy – 1000 chars with 200 overlap for context continuity
    text_splitter = RecursiveCharacterTextSplitter(
        chunk_size=1000,
        chunk_overlap=200,
        length_function=len,
        separators=["\n\n", "\n", ".", " ", ""]
    )

    all_chunks = []
    print(f"Scanning directory: {BOOKS_DIR}")
    for filename in os.listdir(BOOKS_DIR):
        if filename.lower().endswith('.pdf'):
            file_path = os.path.join(BOOKS_DIR, filename)
            print(f"Processing: {filename}")
            loader = PyPDFLoader(file_path)
            documents = loader.load()
            chunks = text_splitter.split_documents(documents)
            for chunk in chunks:
                # Attach source book metadata for later citation
                if not hasattr(chunk, 'metadata') or chunk.metadata is None:
                    chunk.metadata = {}
                chunk.metadata["source_book"] = filename
            all_chunks.extend(chunks)
            print(f"-> Generated {len(chunks)} chunks from {filename}")

    if not all_chunks:
        print("No PDF files found in the books directory. Exiting.")
        return

    print(f"Uploading {len(all_chunks)} total chunks to Supabase pgvector...")
    # Store chunks in the Supabase vector table defined in the SQL setup
    SupabaseVectorStore.from_documents(
        documents=all_chunks,
        embedding=embeddings,
        client=supabase_client,
        table_name="palmistry_books_knowledge",
        query_name="match_palmistry_documents"
    )
    print("✅ Ingestion complete! Knowledge base is ready for RAG.")

if __name__ == "__main__":
    ingest_books_to_supabase()
