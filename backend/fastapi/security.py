import base64
import json
import os
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

# NOTE: In production replace this with a securely generated 32‑byte key stored in env
SECRET_KEY = os.getenv("AES_KEY", "12345678901234567890123456789012").encode()

def decrypt_payload(b64_string: str) -> dict:
    """Decrypt a Base64‑encoded payload coming from the Android client.
    Returns a Python dict parsed from the JSON.
    """
    try:
        raw = base64.b64decode(b64_string)
        iv = raw[:12]
        ciphertext = raw[12:]
        aesgcm = AESGCM(SECRET_KEY)
        plaintext = aesgcm.decrypt(iv, ciphertext, None)
        return json.loads(plaintext.decode("utf-8"))
    except Exception as e:
        raise ValueError(f"Decryption failed: {e}")

def encrypt_payload(data_dict: dict) -> str:
    """Encrypt a Python dict (will be JSON‑encoded) and return a Base64 string.
    The Android side expects the same format (IV + ciphertext).
    """
    try:
        plain = json.dumps(data_dict).encode("utf-8")
        aesgcm = AESGCM(SECRET_KEY)
        iv = os.urandom(12)
        ciphertext = aesgcm.encrypt(iv, plain, None)
        combined = iv + ciphertext
        return base64.b64encode(combined).decode("utf-8")
    except Exception as e:
        raise ValueError(f"Encryption failed: {e}")
