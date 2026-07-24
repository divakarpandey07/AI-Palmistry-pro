package com.example.palmistry.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data classes for AES-256 encrypted network communication.
 */
data class EncryptedRequest(
    @SerializedName("payload") val payload: String
)

data class EncryptedResponse(
    @SerializedName("payload") val payload: String,
    @SerializedName("status") val status: String = "success"
)
