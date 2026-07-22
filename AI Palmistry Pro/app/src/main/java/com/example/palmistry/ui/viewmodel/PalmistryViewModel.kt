package com.example.palmistry.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmistry.data.model.ReadingEntity
import com.example.palmistry.data.repository.PalmistryRepository
import com.example.palmistry.security.EncryptionUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ReadingUiState {
    object Idle : ReadingUiState()
    object Loading : ReadingUiState()
    data class Success(val reading: String) : ReadingUiState()
    data class Error(val message: String) : ReadingUiState()
}

@HiltViewModel
class PalmistryViewModel @Inject constructor(
    private val repository: PalmistryRepository,
    private val encryptionUtil: EncryptionUtil
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReadingUiState>(ReadingUiState.Idle)
    val uiState: StateFlow<ReadingUiState> = _uiState.asStateFlow()

    // Generate a new AES key per session (in production: load from Keystore)
    private val sessionKey = encryptionUtil.generateKey()

    val readingHistory = repository.getAllReadings()

    /**
     * Called when the palm is captured and TFLite returns the metadata JSON.
     */
    fun generateReading(palmJson: String, userQuestion: String = "Mera haath padhiye") {
        _uiState.value = ReadingUiState.Loading
        viewModelScope.launch {
            val metadata = mapOf("palm_json" to palmJson)
            val result = repository.generateReading(userQuestion, metadata, sessionKey)
            result.fold(
                onSuccess = { reading ->
                    _uiState.value = ReadingUiState.Success(reading)
                    // Save to local encrypted DB
                    repository.saveReadingLocally(
                        ReadingEntity(
                            palmMetadataJson = palmJson,
                            readingResult = reading,
                            lifeLineScore = 0f,
                            heartLineScore = 0f,
                            headLineScore = 0f,
                            fateLineScore = 0f,
                            confidenceScore = 0f
                        )
                    )
                },
                onFailure = { e ->
                    _uiState.value = ReadingUiState.Error(e.message ?: "Unknown error")
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = ReadingUiState.Idle
    }

    fun deleteReading(id: Int) {
        viewModelScope.launch { repository.deleteReading(id) }
    }
}
