import httpx, json, os

metadata = {"palm_json": '{"life_line": 0.82}', "hand": "right"}
question = "Meri jeevan rekha kaisi hai?"
context_text = "Samudrik Shastra context"

prompt = f"""You are an expert Vedic Palm Reader (Hastrekha Specialist).
Metadata: {json.dumps(metadata)}
Context: {context_text}
Question: {question}"""

groq_key = "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"
headers = {
    "Authorization": f"Bearer {groq_key}",
    "Content-Type": "application/json"
}
payload = {
    "model": "llama-3.1-8b-instant",
    "messages": [{"role": "user", "content": prompt}],
    "temperature": 0.7,
    "max_tokens": 1024
}

r = httpx.post("https://api.groq.com/openai/v1/chat/completions", headers=headers, json=payload, timeout=15)
print("STATUS:", r.status_code)
if r.status_code == 200:
    print("SUCCESS!")
    print(r.json()["choices"][0]["message"]["content"][:200])
else:
    print("ERROR:", r.text)
