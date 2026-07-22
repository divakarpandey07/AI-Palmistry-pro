package com.example.palmistry.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single palm reading session stored locally.
 * The database is encrypted with SQLCipher.
 */
@Entity(tableName = "readings")
data class ReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val palmMetadataJson: String,      // Encrypted palm line JSON
    val readingResult: String,          // AI-generated reading text
    val lifeLineScore: Float,
    val heartLineScore: Float,
    val headLineScore: Float,
    val fateLineScore: Float,
    val confidenceScore: Float
)

/** Request model for the encrypted API call */
data class EncryptedRequest(val payload: String)

/** Response model from the encrypted API call */
data class EncryptedResponse(val payload: String)
