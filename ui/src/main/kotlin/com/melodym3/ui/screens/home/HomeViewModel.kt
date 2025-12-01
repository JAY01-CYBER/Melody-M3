package com.melodym3.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.domain.usecase.GetHomeRecommendationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Define the state of the Home Screen
data class HomeUiState(
    val recommendations: List<MusicItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeRecommendationsUseCase: GetHomeRecommendationsUseCase,
    // NEW: Inject the PlaybackController to start playing music
    private val playbackController: PlaybackController 
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadRecommendations()
    }

    /**
     * Fetches recommendations from the use case and updates the UI state.
     */
    fun loadRecommendations() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            val result = getHomeRecommendationsUseCase()
            
            result.onSuccess { items ->
                uiState = uiState.copy(
                    recommendations = items,
                    isLoading = false
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    errorMessage = error.message ?: "Failed to load music data.",
                    isLoading = false
                )
            }
        }
    }
    
    /**
     * Public function called when a Music Item is clicked in the UI.
     * It delegates the action to the PlaybackController.
     */
    fun playItem(item: MusicItem) {
        // This initiates the stream URL fetching and playback process in the background
        playbackController.play(item)
        println("Playback initiated for: ${item.title}")
    }
}
