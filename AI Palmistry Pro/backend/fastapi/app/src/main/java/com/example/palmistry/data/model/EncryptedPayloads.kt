package com.example.palmistry.data.model

import com.google.gson.annotations.SerializedName

data class EncryptedRequest(
    @SerializedName("payload")
    val payload: String
)

data class EncryptedResponse(
    @SerializedName("payload")
    val payload: String
)
