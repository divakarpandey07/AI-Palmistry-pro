import os
import json
import httpx
import base64
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

BASE_URL = os.getenv("BASE_URL", "http://localhost:8000")
AES_SECRET_KEY = os.getenv("AES_SECRET_KEY", "0123456789abcdef0123456789abcdef")

key_bytes = AES_SECRET_KEY.encode('utf-8')[:32].ljust(32, b'0')

def encrypt(data: dict) -> str:
    aesgcm = AESGCM(key_bytes)
    nonce = os.urandom(12)
    payload_bytes = json.dumps(data).encode('utf-8')
    ct = aesgcm.encrypt(nonce, payload_bytes, None)
    return base64.b64encode(nonce + ct).decode('utf-8')

def decrypt(payload: str) -> str:
    data_bytes = base64.b64decode(payload)
    nonce, ct = data_bytes[:12], data_bytes[12:]
    aesgcm = AESGCM(key_bytes)
    pt = aesgcm.decrypt(nonce, ct, None)
    return pt.decode('utf-8')

def test_api():
    print(f"Testing /health at {BASE_URL}...")
    try:
        health_res = httpx.get(f"{BASE_URL}/health")
        print("Health response:", health_res.json())
    except Exception as e:
        print("Health check failed:", e)

    metadata = {
        "hand_type": "water",
        "prominent_lines": ["heart", "head"]
    }
    request_data = {
        "question": "What does my deep heart line mean?",
        "metadata": metadata
    }
    
    encrypted_payload = encrypt(request_data)
    
    print("\nTesting /api/v1/readings/generate...")
    try:
        res = httpx.post(f"{BASE_URL}/api/v1/readings/generate", json={"payload": encrypted_payload})
        if res.status_code == 200:
            encrypted_resp = res.json().get("payload")
            if encrypted_resp:
                decrypted_answer = decrypt(encrypted_resp)
                print("Decrypted reading answer:", decrypted_answer)
            else:
                print("No payload in response:", res.json())
        else:
            print("Error generating reading:", res.status_code, res.text)
    except Exception as e:
        print("Generate request failed:", e)

if __name__ == "__main__":
    test_api()
