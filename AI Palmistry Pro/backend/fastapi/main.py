import os
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv
from pydantic import BaseModel

from security import decrypt_payload, encrypt_payload
from rag_engine import generate_reading, save_experience_to_memory

load_dotenv()

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class GenerateRequest(BaseModel):
    payload: str

class FeedbackRequest(BaseModel):
    question: str
    answer: str

@app.get("/health")
async def health():
    return {"status": "ok"}

@app.post("/api/v1/readings/generate")
async def generate(req: GenerateRequest):
    decrypted_data = decrypt_payload(req.payload)
    question = decrypted_data.get("question", "")
    metadata = decrypted_data.get("metadata", {})
    
    answer = generate_reading(question, metadata)
    return {"payload": encrypt_payload(answer)}

@app.post("/api/v1/readings/feedback")
async def feedback(req: FeedbackRequest):
    save_experience_to_memory(req.question, req.answer)
    return {"status": "success"}
