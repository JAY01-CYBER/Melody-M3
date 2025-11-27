package com.melodym3.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.usecase.GetHomeRecommendationsUseCase
import kotlinx.coroutines.launch

// Define the state of the Home Screen
data class HomeUiState(
    val recommendations: List<MusicItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * ViewModel to manage the state and data fetching for the Home Screen.
 * NOTE: Dependency Injection (Hilt/Koin) would normally be used here. 
 * For simplicity, we are passing the UseCase directly for now.
 */
class HomeViewModel(
    private val getHomeRecommendationsUseCase: GetHomeRecommendationsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadRecommendations()
    }

    fun loadRecommendations() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            val result = getHomeRecommendationsUseCase()
            
            result.onSuccess { items ->
                uiState = uiState.copy(
                    recommendations = items,
                    isLoading = false
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    errorMessage = error.message ?: "Failed to load music data.",
                    isLoading = false
                )
            }
        }
    }
}
