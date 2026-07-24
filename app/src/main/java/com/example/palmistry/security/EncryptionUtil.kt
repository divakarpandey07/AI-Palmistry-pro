package com.example.palmistry.security

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AES-256-GCM Encryption Utility with Pure Kotlin Base64 (zero dependencies).
 */
@Singleton
class EncryptionUtil @Inject constructor() {

    companion object {
        private const val AES_KEY_SIZE = 256
        private const val GCM_IV_LENGTH = 12   // 96 bits recommended for GCM
        private const val GCM_TAG_LENGTH = 128  // 128 bits authentication tag
        private const val ALGORITHM = "AES/GCM/NoPadding"

        private const val BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

        fun base64Encode(bytes: ByteArray): String {
            val sb = StringBuilder()
            var i = 0
            while (i < bytes.size) {
                val b1 = bytes[i++].toInt() and 0xFF
                if (i == bytes.size) {
                    sb.append(BASE64_ALPHABET[b1 shr 2])
                    sb.append(BASE64_ALPHABET[(b1 and 0x03) shl 4])
                    sb.append("==")
                    break
                }
                val b2 = bytes[i++].toInt() and 0xFF
                if (i == bytes.size) {
                    sb.append(BASE64_ALPHABET[b1 shr 2])
                    sb.append(BASE64_ALPHABET[((b1 and 0x03) shl 4) or (b2 shr 4)])
                    sb.append(BASE64_ALPHABET[(b2 and 0x0F) shl 2])
                    sb.append("=")
                    break
                }
                val b3 = bytes[i++].toInt() and 0xFF
                sb.append(BASE64_ALPHABET[b1 shr 2])
                sb.append(BASE64_ALPHABET[((b1 and 0x03) shl 4) or (b2 shr 4)])
                sb.append(BASE64_ALPHABET[((b2 and 0x0F) shl 2) or (b3 shr 6)])
                sb.append(BASE64_ALPHABET[b3 and 0x3F])
            }
            return sb.toString()
        }

        fun base64Decode(str: String): ByteArray {
            val cleanStr = str.replace("=", "").replace("\n", "").replace("\r", "")
            val out = java.io.ByteArrayOutputStream()
            var i = 0
            while (i < cleanStr.length) {
                val c1 = BASE64_ALPHABET.indexOf(cleanStr[i++])
                val c2 = if (i < cleanStr.length) BASE64_ALPHABET.indexOf(cleanStr[i++]) else 0
                val c3 = if (i < cleanStr.length) BASE64_ALPHABET.indexOf(cleanStr[i++]) else 0
                val c4 = if (i < cleanStr.length) BASE64_ALPHABET.indexOf(cleanStr[i++]) else 0

                val b1 = ((c1 shl 2) or (c2 shr 4)).toByte()
                out.write(b1.toInt())
                if (c3 != -1 && i <= cleanStr.length) {
                    val b2 = (((c2 and 0x0F) shl 4) or (c3 shr 2)).toByte()
                    out.write(b2.toInt())
                }
                if (c4 != -1 && i <= cleanStr.length) {
                    val b3 = (((c3 and 0x03) shl 6) or c4).toByte()
                    out.write(b3.toInt())
                }
            }
            return out.toByteArray()
        }
    }

    /**
     * Generates a new random AES-256 secret key.
     */
    fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(AES_KEY_SIZE)
        return keyGen.generateKey()
    }

    /**
     * Encrypts plaintext string using AES-256-GCM.
     */
    fun encrypt(plaintext: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val cipherText = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        val ivAndCipherText = iv + cipherText
        return base64Encode(ivAndCipherText)
    }

    /**
     * Decrypts Base64 string using AES-256-GCM.
     */
    fun decrypt(encryptedData: String, secretKey: SecretKey): String {
        val ivAndCipherText = base64Decode(encryptedData)
        val iv = ivAndCipherText.copyOfRange(0, GCM_IV_LENGTH)
        val cipherText = ivAndCipherText.copyOfRange(GCM_IV_LENGTH, ivAndCipherText.size)

        val cipher = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val plainText = cipher.doFinal(cipherText)
        return String(plainText, Charsets.UTF_8)
    }

    fun keyFromBase64(base64Key: String): SecretKey {
        val keyBytes = base64Decode(base64Key)
        return SecretKeySpec(keyBytes, "AES")
    }

    fun keyToBase64(secretKey: SecretKey): String {
        return base64Encode(secretKey.encoded)
    }
}
