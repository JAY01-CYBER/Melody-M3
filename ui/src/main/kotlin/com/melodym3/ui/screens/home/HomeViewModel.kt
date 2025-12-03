package com.melodym3.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.data.repository.YouTubeRepository
import com.melodym3.domain.model.Song
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.playback.PlaybackController
import com.melodym3.service.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recommendations: List<MusicItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playbackController: PlaybackController,
    private val snackbarManager: SnackbarManager,
    private val youTubeRepository: YouTubeRepository          // ← Naya add
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
                // Real YouTube se songs la rahe hain
                val songs: List<Song> = youTubeRepository.searchSongs("trending india")

                uiState = uiState.copy(
                    recommendations = songs.map {
                        MusicItem(
                            id = it.id,
                            title = it.title,
                            subtitle = it.subtitle,
                            thumbnailUrl = it.thumbnail
                            // agar MusicItem mein aur fields hain to yahan add kar dena
                        )
                    },
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

    fun playItem(item: MusicItem) {
        viewModelScope.launch {
            playbackController.playItem(item)
            snackbarManager.showMessage("Now playing: ${item.title}")
        }
    }

    // Agar refresh button daalna chahe to
    fun refresh() {
        loadRecommendations()
    }
}
