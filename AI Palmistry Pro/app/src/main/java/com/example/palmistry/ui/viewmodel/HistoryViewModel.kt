package com.example.palmistry.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.palmistry.data.model.ReadingEntity
import com.example.palmistry.data.repository.PalmistryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HistoryViewModel exposes reading history from the local encrypted Room DB.
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: PalmistryRepository
) : ViewModel() {

    /** Reactive list of all past readings ordered by timestamp DESC */
    val readings: StateFlow<List<ReadingEntity>> = repository
        .getAllReadings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun deleteReading(id: Int) {
        viewModelScope.launch { repository.deleteReading(id) }
    }
}
