"""
Quick end-to-end test:
Simulates an Android app sending an encrypted palm reading request
and receiving an encrypted AI reading response.
"""
import os, base64, json, httpx
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

BASE_URL = "http://localhost:8000"
AES_KEY  = base64.b64decode("BP99iKWY7iibCmQvoOKi8nJj+tjp7PS+ZelJdsIMFAs=")

def encrypt(data: dict) -> str:
    iv = os.urandom(12)
    aesgcm = AESGCM(AES_KEY)
    ct = aesgcm.encrypt(iv, json.dumps(data).encode(), None)
    return base64.b64encode(iv + ct).decode()

def decrypt(encoded: str) -> dict:
    raw = base64.b64decode(encoded)
    iv, ct = raw[:12], raw[12:]
    aesgcm = AESGCM(AES_KEY)
    return json.loads(aesgcm.decrypt(iv, ct, None))

print("=" * 60)
print("   AI PALMISTRY PRO - END TO END TEST")
print("=" * 60)

# Step 1: Health check
r = httpx.get(f"{BASE_URL}/health")
print(f"\n[1] Health: {r.status_code} => {r.json()}")

# Step 2: Send palm reading request
payload = {
    "question": "Meri jeevan rekha kaisi hai? Mera bhavishya kaisa hoga?",
    "metadata": {
        "palm_json": '{"life_line": 0.82, "heart_line": 0.75, "head_line": 0.68, "fate_line": 0.55}',
        "hand": "right"
    }
}

encrypted_payload = encrypt(payload)
print(f"\n[2] Sending encrypted palm reading request...")
print(f"    Encrypted payload length: {len(encrypted_payload)} chars")

resp = httpx.post(
    f"{BASE_URL}/api/v1/readings/generate",
    json={"payload": encrypted_payload},
    timeout=60
)
print(f"\n[3] Response status: {resp.status_code}")

if resp.status_code == 200:
    body = resp.json()
    result = decrypt(body["payload"])
    print(f"\n[4] DECRYPTED AI READING:")
    print("=" * 60)
    print(result.get("reading", result))
    print("=" * 60)
    print("\n[SUCCESS] End-to-end test PASSED!")
else:
    print(f"\n[ERROR] {resp.text}")

if __name__ == "__main__":
    pass
