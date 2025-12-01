package com.melodym3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.melodym3.domain.model.MusicItem
import com.melodym3.domain.model.ItemType 

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // ViewModel States
    recommendations: List<MusicItem>,
    isLoading: Boolean,
    errorMessage: String?,
    // NEW: Click Handler to play music
    onItemClick: (MusicItem) -> Unit 
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // ... (Top App Bar and Loading/Error logic remains the same) ...

        // --- Content Loaded Successfully ---
        if (!isLoading && errorMessage == null) {
            item {
                Text(
                    "Quick Picks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                )
            }

            // M3 Music Cards for each recommendation
            items(recommendations.size) { index ->
                val item = recommendations[index]
                MusicCard(
                    item = item,
                    // Pass the click action up to the ViewModel
                    onClick = { onItemClick(item) } 
                )
            }
        }
    }
}

@Composable
fun MusicCard(item: MusicItem, onClick: () -> Unit) { // <-- onClick added
    ElevatedCard(
        onClick = onClick, // <-- Apply the click handler
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            
            AsyncImage(
                model = item.imageUrl.ifEmpty { 
                    "https://via.placeholder.com/64?text=Melody" 
                },
                contentDescription = item.title,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop, 
            )
            
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
