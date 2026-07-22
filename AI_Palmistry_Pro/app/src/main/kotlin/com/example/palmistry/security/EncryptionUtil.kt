package com.example.palmistry.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    private const val ALGORITHM = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_LENGTH_BYTE = 12 // GCM mode ke liye 12 bytes ideal hai

    /**
     * JSON text ko AES-256-GCM se encrypt karke Base64 string return karta hai.
     * Note: Production mein secret key securely exchange honi chahiye (e.g., RSA key exchange)
     * ya NDK (C++) ke through hide ki jani chahiye.
     */
    fun encryptData(jsonMetadata: String, secretKeyString: String): String {
        try {
            // 1. Convert string key to SecretKey (Must be exactly 32 bytes for AES-256)
            val secretKey: SecretKey = SecretKeySpec(secretKeyString.toByteArray(Charsets.UTF_8), "AES")

            // 2. Generate secure random IV (Initialization Vector)
            val iv = ByteArray(IV_LENGTH_BYTE)
            SecureRandom().nextBytes(iv)
            val gcmParameterSpec = GCMParameterSpec(TAG_LENGTH_BIT, iv)

            // 3. Initialize Cipher in ENCRYPT_MODE
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec)

            // 4. Encrypt the data
            val cipherText = cipher.doFinal(jsonMetadata.toByteArray(Charsets.UTF_8))

            // 5. Combine IV and CipherText
            // Backend ko decrypt karne ke liye IV chahiye hota hai, isliye hum usey ciphertext ke aage jod dete hain
            val combinedPayload = iv + cipherText

            // 6. Return as a safe Base64 String to send over HTTP
            return Base64.encodeToString(combinedPayload, Base64.NO_WRAP)

        } catch (e: Exception) {
            // In a real app, log this securely and handle the error
            throw SecurityException("Encryption failed", e)
        }
    }
}
