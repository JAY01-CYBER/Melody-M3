package com.melodym3.ui.screens.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.domain.usecase.SearchMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State for the Search Screen
data class ExploreUiState(
    val query: String = "",
    val results: List<MusicItem> = emptyList(),
    val isLoading: Boolean = false,
    val isSearchBarActive: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val searchMusicUseCase: SearchMusicUseCase,
    private val playbackController: PlaybackController
) : ViewModel() {

    var uiState by mutableStateOf(ExploreUiState())
        private set
        
    private var searchJob: Job? = null
    
    /**
     * Handles query input and debounces the search execution.
     */
    fun onQueryChange(newQuery: String) {
        uiState = uiState.copy(query = newQuery)
        
        // Cancel any previous search job
        searchJob?.cancel() 

        if (newQuery.length < 3) {
            // Clear results if query is too short
            uiState = uiState.copy(results = emptyList())
            return
        }

        // Start new job with debounce
        searchJob = viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            delay(300L) // Debounce delay
            executeSearch(newQuery)
        }
    }

    private fun executeSearch(query: String) {
        viewModelScope.launch {
            val result = searchMusicUseCase(query)
            
            result.onSuccess { items ->
                uiState = uiState.copy(
                    results = items,
                    isLoading = false
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    errorMessage = error.message ?: "Search failed.",
                    isLoading = false
                )
            }
        }
    }
    
    /**
     * Toggles the active state of the Material 3 SearchBar.
     */
    fun onActiveChange(isActive: Boolean) {
        uiState = uiState.copy(isSearchBarActive = isActive)
        if (!isActive) {
            // Clear search when deactivated
            uiState = uiState.copy(query = "", results = emptyList(), errorMessage = null)
        }
    }
    
    /**
     * Starts playback when a search result item is clicked.
     */
    fun playItem(item: MusicItem) {
        playbackController.play(item)
    }
}
