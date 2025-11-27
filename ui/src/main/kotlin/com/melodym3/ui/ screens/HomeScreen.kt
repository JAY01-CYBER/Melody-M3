package com.melodym3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.model.ItemType // Ensure this is imported

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // These parameters are passed from the ViewModel/MainActivity (Step 7)
    recommendations: List<MusicItem>,
    isLoading: Boolean,
    errorMessage: String?,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // --- 1. Large Top App Bar (M3 Standard) ---
        item {
            LargeTopAppBar(
                title = { Text("Good evening", style = MaterialTheme.typography.headlineLarge) },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            )
        }

        // --- 2. Loading, Error, or Content Display ---
        if (isLoading) {
            item {
                // Show loading indicator
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
                Text("Loading recommendations...", modifier = Modifier.padding(16.dp))
            }
        } else if (errorMessage != null) {
            item {
                // Show M3 style error message
                Text(
                    "Error: $errorMessage. Tap to retry.", 
                    color = MaterialTheme.colorScheme.error, 
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            // --- Content Loaded Successfully ---
            item {
                Text(
                    "Quick Picks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
            }

            // M3 Music Cards for each recommendation
            items(recommendations.size) { index ->
                MusicCard(item = recommendations[index])
            }
        }
    }
}

// --- MusicCard Composable (Reused from Step 6) ---
@Composable
fun MusicCard(item: MusicItem) {
    ElevatedCard(
        onClick = { /* Navigate to item details */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Placeholder for Album Art
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(64.dp),
                shape = MaterialTheme.shapes.small
            ) {
                // Image loading logic here using item.imageUrl
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    item.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
