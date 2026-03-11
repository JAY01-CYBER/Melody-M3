package com.melodym3.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.usecase.SearchMusicUseCase
import com.melodym3.ui.utils.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreUiState(
    val searchQuery: String = "",
    val searchResults: List<MusicItem> = emptyList(),
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val searchMusicUseCase: SearchMusicUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    
    init {
        observeSearchQuery()
    }
    
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.length >= 2 || it.isEmpty() }
                .collect { query ->
                    _uiState.value = _uiState.value.copy(searchQuery = query)
                    if (query.isNotBlank()) {
                        performSearch(query)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            searchResults = emptyList(),
                            isSearching = false
                        )
                    }
                }
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)
            try {
                val results = searchMusicUseCase(query)
                // Ensure duration is Long
                val fixedResults = results.map { musicItem ->
                    musicItem.copy(
                        duration = musicItem.duration  // Already Long, but ensure
                    )
                }
                _uiState.value = _uiState.value.copy(
                    searchResults = fixedResults,
                    isSearching = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    errorMessage = "Search failed: ${e.message}"
                )
                SnackbarManager.showError("Search failed: ${e.message}", viewModelScope)
            }
        }
    }
    
    fun retry() {
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
