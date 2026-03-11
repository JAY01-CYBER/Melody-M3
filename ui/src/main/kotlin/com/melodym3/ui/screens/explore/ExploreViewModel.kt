package com.melodym3.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.melodym3.domain.model.MusicItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreState(
    val searchResults: List<MusicItem> = emptyList(),
    val isSearching: Boolean = false
)

@HiltViewModel
class ExploreViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSearching = true)
            val results = listOf(
                MusicItem("2", "Found Track", "Artist", "https://via.placeholder.com/150", "", "04:00")
            )
            _state.value = _state.value.copy(searchResults = results, isSearching = false)
        }
    }
}
