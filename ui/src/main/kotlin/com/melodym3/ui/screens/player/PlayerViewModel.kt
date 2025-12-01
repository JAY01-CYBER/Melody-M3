package com.melodym3.ui.screens.player

import androidx.lifecycle.ViewModel
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Placeholder data class for current player state
data class PlayerState(
    val currentItem: MusicItem? = null,
    val isPlaying: Boolean = false,
    val duration: Long = 0L,
    val position: Long = 0L,
    val error: String? = null
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playbackController: PlaybackController
) : ViewModel() {

    // NOTE: In a complete implementation, you would need to set up listeners 
    // in PlaybackController to actively map ExoPlayer state changes to this flow.
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    init {
        // Here you would start listening to the ExoPlayer state updates
        // For now, we will use mock values for demonstration
        _playerState.value = _playerState.value.copy(
            currentItem = MusicItem("mock", "Melody M3 Theme Song", "The Developers", "https://picsum.photos/id/160/300", com.melodym3.domain.model.ItemType.SONG),
            isPlaying = true,
            duration = 180000L // 3 minutes
        )
    }

    fun togglePlayback() {
        if (_playerState.value.isPlaying) {
            playbackController.pause()
        } else {
            playbackController.resume()
        }
        // Mock state update (replace with actual ExoPlayer state listener in production)
        _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
    }

    fun skipToNext() {
        playbackController.seekToNext()
    }
    
    // Add seekToPosition(position: Long) function here
}
