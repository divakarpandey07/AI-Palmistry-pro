package com.example.palmistry.network

import com.example.palmistry.data.model.EncryptedRequest
import com.example.palmistry.data.model.EncryptedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API interface for the FastAPI backend.
 * All payloads are AES-256-GCM encrypted – no raw data is ever sent.
 */
interface PalmistryApiService {

    @POST("api/v1/readings/generate")
    suspend fun generateReading(
        @Body request: EncryptedRequest
    ): Response<EncryptedResponse>

    @POST("api/v1/readings/feedback")
    suspend fun sendFeedback(
        @Body request: FeedbackRequest
    ): Response<FeedbackResponse>
}

data class FeedbackRequest(
    val user_question: String,
    val ai_answer: String,
    val is_liked: Boolean
)

data class FeedbackResponse(
    val status: String,
    val message: String
)
