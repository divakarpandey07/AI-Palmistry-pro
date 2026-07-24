package com.example.palmistry.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android Keystore Manager — securely generates and retrieves AES-256 keys.
 * Keys never leave the secure hardware enclave.
 */
@Singleton
class KeystoreManager @Inject constructor() {

    companion object {
        private const val KEY_ALIAS    = "palmistry_master_key"
        private const val KEYSTORE     = "AndroidKeyStore"
    }

    /**
     * Returns the existing AES key from the Keystore, or creates one if it doesn't exist.
     * This key is used by EncryptionUtil for all payload encryption/decryption.
     */
    fun generateOrGetKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEYSTORE).also { it.load(null) }

        if (keyStore.containsAlias(KEY_ALIAS)) {
            return (keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
        }

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE
        )
        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(false) // Background processing allowed
                .build()
        )
        return keyGenerator.generateKey()
    }
}
