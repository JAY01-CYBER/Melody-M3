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
    val recommendations: List<MusicItem> = emptyList()
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
            _state.update { it.copy(isLoading = true) }
            try {
                val mockItems = listOf(
                    MusicItem(
                        id = "1", 
                        title = "Trending Song", 
                        artist = "English Artist", 
                        imageUrl = "https://via.placeholder.com/150",
                        url = "https://example.com/stream",
                        duration = "03:30"
                    )
                )
                _state.update { it.copy(isLoading = false, recommendations = mockItems) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                SnackbarManager.showError(e.message ?: "Unknown Error", viewModelScope)
            }
        }
    }

    fun playItem(item: MusicItem) {}
}
