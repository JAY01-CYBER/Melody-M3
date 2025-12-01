package com.melodym3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.melodym3.domain.model.MusicItem
import com.melodym3.ui.screens.home.MusicCard
import com.melodym3.ui.screens.explore.ExploreViewModel
import com.melodym3.ui.screens.explore.ExploreUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    // Inject ViewModel using Hilt
    viewModel: ExploreViewModel = hiltViewModel() 
) {
    val uiState = viewModel.uiState
    
    // Use a Box to layer the SearchBar over the content
    Box(modifier = Modifier.fillMaxSize()) {
        
        // --- 1. Material 3 Search Bar ---
        SearchBar(
            query = uiState.query,
            onQueryChange = viewModel::onQueryChange,
            onSearch = { viewModel.onActiveChange(false) }, // Collapse search bar on final search
            active = uiState.isSearchBarActive,
            onActiveChange = viewModel::onActiveChange,
            placeholder = { Text("Search songs, artists, albums, or videos") },
            leadingIcon = {
                if (uiState.isSearchBarActive) {
                    IconButton(onClick = { viewModel.onActiveChange(false) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Close Search")
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = if (uiState.isSearchBarActive) 0.dp else 16.dp)
        ) {
            // --- Content displayed when SearchBar is ACTIVE ---
            SearchContent(uiState = uiState, onItemClick = viewModel::playItem)
        }
        
        // --- 2. Background Content (when SearchBar is INACTIVE) ---
        if (!uiState.isSearchBarActive) {
            // Placeholder/Suggestion Content when not searching
            Column(modifier = Modifier.padding(top = 72.dp, start = 16.dp)) {
                Text("Explore Trending Music", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tap the search icon to start searching.")
            }
        }
    }
}

@Composable
private fun SearchContent(
    uiState: ExploreUiState,
    onItemClick: (MusicItem) -> Unit
) {
    if (uiState.isLoading) {
        // Loading State
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Searching...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else if (uiState.errorMessage != null) {
        // Error State
        Text(
            "Error: ${uiState.errorMessage}",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else if (uiState.results.isEmpty() && uiState.query.isNotEmpty()) {
        // No Results Found
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No results found for \"${uiState.query}\"", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else if (uiState.results.isNotEmpty()) {
        // Display Search Results
        LazyColumn(contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp)) {
            items(uiState.results.size) { index ->
                val item = uiState.results[index]
                // Reusing the MusicCard component from HomeScreen
                MusicCard(item = item, onClick = { onItemClick(item) }) 
            }
        }
    } else {
        // Initial state or empty query
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Type at least 3 characters to search.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
