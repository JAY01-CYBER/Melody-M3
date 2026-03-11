package com.melodym3.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.LikedTrack
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.domain.repository.LibraryRepository
import com.melodym3.ui.utils.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI State data class
data class LibraryUiState(
    val likedTracks: List<LikedTrack> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        loadLikedTracks()
    }

    private fun loadLikedTracks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val tracks = libraryRepository.getLikedTracks()
                _uiState.value = _uiState.value.copy(
                    likedTracks = tracks,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load library: ${e.message}",
                    isLoading = false
                )
                SnackbarManager.showError("Failed to load library: ${e.message}", viewModelScope)
            }
        }
    }

    fun removeTrack(track: LikedTrack) {
        viewModelScope.launch {
            try {
                libraryRepository.removeFromLiked(track.id)
                // Refresh the list
                loadLikedTracks()
                SnackbarManager.showMessage("Removed from library", viewModelScope)
            } catch (e: Exception) {
                SnackbarManager.showError("Failed to remove: ${e.message}", viewModelScope)
            }
        }
    }

    fun playTrack(track: LikedTrack) {
        viewModelScope.launch {
            try {
                // Convert LikedTrack to MusicItem
                val musicItem = MusicItem(
                    id = track.id,
                    title = track.title,
                    artist = track.artist,
                    duration = track.duration,
                    url = track.url,
                    imageUrl = track.imageUrl,
                    subtitle = track.subtitle
                )
                playbackController.play(musicItem)
                SnackbarManager.showMessage("Now playing: ${track.title}", viewModelScope)
            } catch (e: Exception) {
                SnackbarManager.showError("Failed to play: ${e.message}", viewModelScope)
            }
        }
    }

    fun retry() {
        loadLikedTracks()
    }
}
