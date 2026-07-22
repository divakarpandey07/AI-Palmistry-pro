# security.py
# ---------------------------------------------------------------
# Backend AES-256-GCM payload encryption/decryption.
# Mirrors the Android EncryptionUtil.kt logic exactly.
# ---------------------------------------------------------------
import os
import base64
import json
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

# ---------------------------------------------------------------
# The shared AES-256 key must be kept in an environment variable.
# Generate once with: python -c "import os,base64; print(base64.b64encode(os.urandom(32)).decode())"
# Then set as: AES_SECRET_KEY=<base64_key>
# ---------------------------------------------------------------
_raw_key = os.getenv("AES_SECRET_KEY", "")
if not _raw_key:
    raise RuntimeError("AES_SECRET_KEY environment variable is not set!")

SECRET_KEY: bytes = base64.b64decode(_raw_key)  # Must be 32 bytes for AES-256

GCM_IV_LENGTH = 12  # 96 bits

def encrypt_payload(data: dict) -> str:
    """Encrypt a Python dict as Base64(IV + CipherText)."""
    plaintext = json.dumps(data).encode("utf-8")
    iv = os.urandom(GCM_IV_LENGTH)
    aesgcm = AESGCM(SECRET_KEY)
    ciphertext = aesgcm.encrypt(iv, plaintext, None)
    iv_and_cipher = iv + ciphertext
    return base64.b64encode(iv_and_cipher).decode("utf-8")

def decrypt_payload(encoded: str) -> dict:
    """Decrypt Base64(IV + CipherText) back to a Python dict."""
    raw = base64.b64decode(encoded)
    iv = raw[:GCM_IV_LENGTH]
    ciphertext = raw[GCM_IV_LENGTH:]
    aesgcm = AESGCM(SECRET_KEY)
    try:
        plaintext = aesgcm.decrypt(iv, ciphertext, None)
    except Exception:
        raise ValueError("Decryption failed – payload may have been tampered with!")
    return json.loads(plaintext.decode("utf-8"))
