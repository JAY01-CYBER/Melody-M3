package com.melodym3.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.data.repository.FirebaseLibraryRepository
import com.melodym3.domain.model.LikedTrack
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.service.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Placeholder data class for current player state
data class PlayerState(
    val currentItem: MusicItem? = null,
    val isPlaying: Boolean = false,
    val duration: Long = 0L,
    val position: Long = 0L,
    val isLiked: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackController: PlaybackController,
    private val libraryRepository: FirebaseLibraryRepository,
    private val snackbarManager: SnackbarManager // Injected for user feedback
) : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        // Mock state initialization for immediate UI preview
        _playerState.value = _playerState.value.copy(
            currentItem = MusicItem("mock-id-123", "Melody M3 Theme Song", "The Developers", "https://picsum.photos/id/160/300", com.melodym3.domain.model.ItemType.SONG),
            isPlaying = true,
            duration = 180000L 
        )
        // Start observing the liked status of the current track from Firestore
        observeLikedStatus()
    }
    
    /**
     * Observes the liked tracks stream and updates the isLiked state for the current item.
     */
    private fun observeLikedStatus() {
        viewModelScope.launch {
            libraryRepository.getLikedTracks().collect { likedTracks ->
                val currentTrackId = _playerState.value.currentItem?.id
                // Check if the current playing track exists in the list of liked tracks
                val isCurrentTrackLiked = likedTracks.any { it.id == currentTrackId }
                
                if (_playerState.value.isLiked != isCurrentTrackLiked) {
                     _playerState.update { it.copy(isLiked = isCurrentTrackLiked) }
                }
            }
        }
    }

    /**
     * Toggles the track's status between Liked (saved to Firestore) and Unliked (removed).
     */
    fun toggleLikeStatus() {
        viewModelScope.launch {
            val currentItem = _playerState.value.currentItem ?: return@launch
            
            val isCurrentlyLiked = _playerState.value.isLiked
            val result = if (isCurrentlyLiked) {
                // Remove the track from the library
                libraryRepository.removeTrack(currentItem.id).onSuccess {
                    snackbarManager.showMessage("Removed from Library: ${currentItem.title}")
                }
            } else {
                // Save the track to the library
                val trackToSave = LikedTrack(
                    id = currentItem.id,
                    title = currentItem.title,
                    artist = currentItem.subtitle,
                    albumArtUrl = currentItem.imageUrl
                )
                libraryRepository.saveTrack(trackToSave).onSuccess {
                    snackbarManager.showMessage("Added to Library: ${currentItem.title}")
                }
            }

            result.onFailure { e ->
                snackbarManager.showMessage("Action failed: ${e.message}")
            }
            
            // The isLiked state will update automatically via observeLikedStatus flow
        }
    }
    
    /**
     * Toggles play/pause state for the main player controls.
     */
    fun togglePlayback() {
        if (_playerState.value.isPlaying) {
            playbackController.pause()
        } else {
            playbackController.resume()
        }
        _playerState.update { it.copy(isPlaying = !_playerState.value.isPlaying) }
    }
    
    /**
     * Toggles play/pause state specifically for the Mini-Player controls (Step 23).
     */
    fun togglePlaybackFromMiniPlayer() {
        togglePlayback()
    }

    fun skipToNext() {
        playbackController.seekToNext()
        snackbarManager.showMessage("Skipping to next track...")
    }

    // TODO: Add seekToPosition(position: Long) function for slider control
}
