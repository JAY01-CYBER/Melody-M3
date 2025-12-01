package com.melodym3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.melodym3.domain.model.LikedTrack
import com.melodym3.ui.screens.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 1. Large Top App Bar
        item {
            LargeTopAppBar(
                title = { Text("Your Library", style = MaterialTheme.typography.headlineLarge) },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            )
        }

        // 2. Loading / Error / Empty States
        if (uiState.isLoading) {
            item {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(16.dp))
                Text("Loading your saved tracks...", modifier = Modifier.padding(16.dp))
            }
        } else if (uiState.errorMessage != null) {
            item {
                Text(
                    "Error: ${uiState.errorMessage}", 
                    color = MaterialTheme.colorScheme.error, 
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (uiState.likedTracks.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("Your library is empty. Start liking some songs!", 
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            // 3. Display Liked Tracks List
            item {
                Text(
                    "Liked Tracks (${uiState.likedTracks.size})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
            }
            
            items(uiState.likedTracks) { track ->
                LibraryTrackItem(
                    track = track,
                    onPlay = { viewModel.playTrack(track) },
                    onRemove = { viewModel.removeTrack(track) }
                )
            }
        }
    }
}

@Composable
fun LibraryTrackItem(
    track: LikedTrack,
    onPlay: () -> Unit,
    onRemove: () -> Unit
) {
    // List item using M3 standard containers
    ListItem(
        headlineContent = { Text(track.title) },
        supportingContent = { Text(track.artist) },
        leadingContent = {
            // Placeholder for Album Art/Number
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(48.dp)
            ) { /* Image placeholder here */ }
        },
        trailingContent = {
            Row {
                IconButton(onClick = onPlay) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove from Library")
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
            .clickable { onPlay() } // Make the entire row clickable to play
    )
    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.surfaceVariant)
}
