package com.example.palmistry.data.repository

import com.example.palmistry.data.local.ReadingDao
import com.example.palmistry.data.model.EncryptedRequest
import com.example.palmistry.data.model.ReadingEntity
import com.example.palmistry.network.PalmistryApiService
import com.example.palmistry.security.EncryptionUtil
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository: Single source of truth between ViewModel, Network and Local DB.
 * Handles encryption before sending, decryption after receiving.
 */
@Singleton
class PalmistryRepository @Inject constructor(
    private val apiService: PalmistryApiService,
    private val readingDao: ReadingDao,
    private val encryptionUtil: EncryptionUtil
) {
    private val gson = Gson()

    /**
     * Send palm metadata to backend and get AI reading.
     * @param question  The user's palmistry question
     * @param metadata  The palm line JSON from TFLite
     * @param secretKey AES-256 key for payload encryption
     */
    suspend fun generateReading(
        question: String,
        metadata: Map<String, Any>,
        secretKey: SecretKey
    ): Result<String> {
        return try {
            // Build payload
            val payload = mapOf("question" to question, "metadata" to metadata)
            val jsonPayload = gson.toJson(payload)

            // Encrypt payload
            val encryptedPayload = encryptionUtil.encrypt(jsonPayload, secretKey)
            val request = EncryptedRequest(payload = encryptedPayload)

            // Send to backend
            val response = apiService.generateReading(request)
            if (response.isSuccessful) {
                val body = response.body()!!
                // Decrypt response
                val decryptedJson = encryptionUtil.decrypt(body.payload, secretKey)
                val resultMap = gson.fromJson(decryptedJson, Map::class.java)
                Result.success(resultMap["reading"] as String)
            } else {
                Result.failure(Exception("Server error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Save reading to local encrypted Room DB */
    suspend fun saveReadingLocally(reading: ReadingEntity) {
        readingDao.insertReading(reading)
    }

    /** Get all past readings as a reactive Flow */
    fun getAllReadings(): Flow<List<ReadingEntity>> = readingDao.getAllReadings()

    /** Delete a specific reading */
    suspend fun deleteReading(id: Int) = readingDao.deleteReading(id)
}
