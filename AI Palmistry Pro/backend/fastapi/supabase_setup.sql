-- ================================================================
-- Supabase Complete Setup SQL for AI Palmistry Pro
-- Run this ONCE in Supabase SQL Editor before ingestion
-- ================================================================

-- Step 1: Enable pgvector extension
CREATE EXTENSION IF NOT EXISTS vector;

-- ================================================================
-- Step 2: Main knowledge base table (from PDF ingestion)
-- ================================================================
CREATE TABLE IF NOT EXISTS palmistry_books_knowledge (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  source      TEXT,
  page        INTEGER,
  chunk_text  TEXT NOT NULL,
  embedding   vector(384)
);

-- Fast cosine similarity index
CREATE INDEX IF NOT EXISTS palm_embedding_idx
  ON palmistry_books_knowledge
  USING ivfflat (embedding vector_cosine_ops)
  WITH (lists = 100);

-- ================================================================
-- Step 3: AI Memory table (stores liked Q&A pairs)
-- ================================================================
CREATE TABLE IF NOT EXISTS ai_memory (
  id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  question    TEXT NOT NULL,
  answer      TEXT NOT NULL,
  created_at  TIMESTAMPTZ DEFAULT now()
);

-- ================================================================
-- Step 4: RPC function for vector similarity search
-- Called by backend: POST /rest/v1/rpc/match_palmistry_chunks
-- ================================================================
CREATE OR REPLACE FUNCTION match_palmistry_chunks(
  query_embedding vector(384),
  match_count     int DEFAULT 5
)
RETURNS TABLE (
  id          UUID,
  source      TEXT,
  page        INTEGER,
  chunk_text  TEXT,
  similarity  FLOAT
)
LANGUAGE plpgsql
AS $$
BEGIN
  RETURN QUERY
  SELECT
    pbk.id,
    pbk.source,
    pbk.page,
    pbk.chunk_text,
    1 - (pbk.embedding <=> query_embedding) AS similarity
  FROM palmistry_books_knowledge pbk
  ORDER BY pbk.embedding <=> query_embedding
  LIMIT match_count;
END;
$$;

-- ================================================================
-- Step 5: Row Level Security (RLS) Policies
-- ================================================================
ALTER TABLE palmistry_books_knowledge ENABLE ROW LEVEL SECURITY;
ALTER TABLE ai_memory ENABLE ROW LEVEL SECURITY;

-- Allow anon to READ knowledge base
CREATE POLICY "allow_anon_read_knowledge"
  ON palmistry_books_knowledge FOR SELECT
  TO anon USING (true);

-- Allow anon to INSERT into ai_memory (for feedback)
CREATE POLICY "allow_anon_insert_memory"
  ON ai_memory FOR INSERT
  TO anon WITH CHECK (true);

-- Allow anon to READ ai_memory (for RAG context)
CREATE POLICY "allow_anon_read_memory"
  ON ai_memory FOR SELECT
  TO anon USING (true);
