package com.melodym3.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.data.repository.FirebaseLibraryRepository
import com.melodym3.domain.model.LikedTrack
import com.melodym3.domain.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryUiState(
    val likedTracks: List<LikedTrack> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val libraryRepository: FirebaseLibraryRepository,
    private val playbackController: PlaybackController
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    init {
        // Start listening to the liked tracks stream when the ViewModel is created
        loadLikedTracks()
    }

    private fun loadLikedTracks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // NOTE: In a production app, the repository should expose a proper
            // flow/snapshot listener that emits updates in real-time.
            try {
                // Since our repository currently uses a simple flow for initial fetch (Step 19), 
                // we observe it here.
                libraryRepository.getLikedTracks().collect { tracks ->
                    _uiState.value = _uiState.value.copy(
                        likedTracks = tracks,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                 _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load library: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    // Functionality to remove a track will be added here
    fun removeTrack(track: LikedTrack) {
        // Placeholder for removal logic
    }
    
    // Functionality to play a track from the library
    fun playTrack(track: LikedTrack) {
        // NOTE: A conversion function (LikedTrack -> MusicItem) is needed here 
        // before calling playbackController.play()
    }
}
