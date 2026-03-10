package com.melodym3.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import com.melodym3.ui.utils.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val recommendations: List<MusicItem> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val mockItems = listOf(
                    MusicItem(
                        id = "1", 
                        title = "Trending Now", 
                        subtitle = "Various Artists", 
                        imageUrl = "https://via.placeholder.com/150"
                    )
                )
                
                _state.update { 
                    it.copy(
                        isLoading = false, 
                        recommendations = mockItems 
                    ) 
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                SnackbarManager.showError(
                    message = e.message ?: "An unexpected error occurred",
                    scope = viewModelScope
                )
            }
        }
    }

    fun playItem(item: MusicItem) {
        // Playback logic here
    }
}
