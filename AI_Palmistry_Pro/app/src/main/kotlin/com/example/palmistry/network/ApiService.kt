package com.example.palmistry.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Data class for encrypted payload to be sent to backend
data class EncryptedPayload(
    val ciphertext: String
)

// Data class for the response returned by the backend
data class ReadingResponse(
    val readingText: String,
    val timestamp: Long
)

interface ApiService {
    @POST("v1/analyze-palm")
    suspend fun getPalmReading(
        @Header("Authorization") token: String, // Supabase JWT token
        @Body payload: EncryptedPayload
    ): Response<ReadingResponse>
}
