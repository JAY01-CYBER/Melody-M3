package com.melodym3.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.data.repository.YouTubeRepository
import com.melodym3.domain.model.Song
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.service.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recommendations: List<Song> = emptyList(),   // ← Song use kar rahe hain
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playbackController: PlaybackController,
    private val snackbarManager: SnackbarManager,
    private val youTubeRepository: YouTubeRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadRecommendations()
    }

    private fun loadRecommendations() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val songs = youTubeRepository.searchSongs("trending india")
                uiState = uiState.copy(
                    recommendations = songs,   // ← direct Song list daal rahe hain
                    isLoading = false
                )
                snackbarManager.showMessage("Loaded ${songs.size} trending songs!")
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Check internet connection"
                )
                snackbarManager.showMessage("Failed to load songs")
            }
        }
    }

    fun playItem(song: Song) {
        viewModelScope.launch {
            playbackController.playItem(song)
            snackbarManager.showMessage("Now playing: ${song.title}")
        }
    }

    fun refresh() {
        loadRecommendations()
    }
}
