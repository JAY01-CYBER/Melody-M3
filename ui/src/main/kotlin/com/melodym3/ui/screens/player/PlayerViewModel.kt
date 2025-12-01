package com.melodym3.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.data.repository.FirebaseLibraryRepository
import com.melodym3.domain.model.LikedTrack
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
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
    val isLiked: Boolean = false, // <-- NEW STATE
    val error: String? = null
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackController: PlaybackController,
    private val libraryRepository: FirebaseLibraryRepository // <-- INJECTED
) : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        // Mock state update for demonstration
        _playerState.value = _playerState.value.copy(
            currentItem = MusicItem("mock", "Melody M3 Theme Song", "The Developers", "https://picsum.photos/id/160/300", com.melodym3.domain.model.ItemType.SONG),
            isPlaying = true,
            duration = 180000L 
        )
        // Start observing the liked status of the current track
        observeLikedStatus()
    }
    
    private fun observeLikedStatus() {
        viewModelScope.launch {
            // This flow keeps the 'isLiked' state updated based on Firestore
            libraryRepository.getLikedTracks().collect { likedTracks ->
                val currentTrackId = _playerState.value.currentItem?.id
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
                // If liked, remove it
                libraryRepository.removeTrack(currentItem.id)
            } else {
                // If unliked, save it
                val trackToSave = LikedTrack(
                    id = currentItem.id,
                    title = currentItem.title,
                    artist = currentItem.subtitle,
                    albumArtUrl = currentItem.imageUrl
                )
                libraryRepository.saveTrack(trackToSave)
            }

            result.onFailure { e ->
                println("Error toggling like status: ${e.message}")
            }
        }
    }
    
    fun togglePlayback() {
        if (_playerState.value.isPlaying) {
            playbackController.pause()
        } else {
            playbackController.resume()
        }
        _playerState.update { it.copy(isPlaying = !_playerState.value.isPlaying) }
    }

    fun skipToNext() {
        playbackController.seekToNext()
    }
}
