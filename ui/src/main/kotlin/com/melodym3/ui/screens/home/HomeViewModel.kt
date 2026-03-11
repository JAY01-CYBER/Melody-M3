package com.melodym3.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.repository.MusicRepository
import com.melodym3.ui.utils.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val homeItems: List<MusicItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeRecommendations()
    }
    
    fun loadHomeRecommendations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val items = musicRepository.getHomeRecommendations()
                // Ensure duration is Long
                val fixedItems = items.map { musicItem ->
                    musicItem.copy(
                        duration = musicItem.duration  // Already Long, but ensure
                    )
                }
                _uiState.value = _uiState.value.copy(
                    homeItems = fixedItems,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load: ${e.message}"
                )
                SnackbarManager.showError("Failed to load home: ${e.message}", viewModelScope)
            }
        }
    }
    
    fun onSongClick(song: MusicItem) {
        viewModelScope.launch {
            SnackbarManager.showMessage("Playing: ${song.title}", viewModelScope)
        }
    }
    
    fun retry() {
        loadHomeRecommendations()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
