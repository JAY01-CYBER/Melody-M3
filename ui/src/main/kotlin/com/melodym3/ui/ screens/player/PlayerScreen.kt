package com.melodym3.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    onDismiss: () -> Unit, // Function to close the player screen
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state by viewModel.playerState.collectAsState()
    val item = state.currentItem

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar (Dismiss Button & Like Button)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Minimize Player")
                }
                
                // Title and Like Button Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Now Playing", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Like/Unlike Button Logic
                    IconButton(onClick = viewModel::toggleLikeStatus) { // <-- CALLS VIEWMODEL
                        val icon = if (state.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                        // Use M3 error color for the 'Liked' state (red heart)
                        val tint = if (state.isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        
                        Icon(
                            icon, 
                            contentDescription = "Toggle Like Status", 
                            tint = tint
                        )
                    }
                }
                
                // Placeholder for overflow menu
                IconButton(onClick = { /* More actions */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Large Album Art (M3 Rounded Corner)
            AsyncImage(
                model = item?.imageUrl ?: "https://via.placeholder.com/300?text=Melody",
                contentDescription = item?.title ?: "Album Art",
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Title and Artist Info
            item?.let {
                Text(
                    it.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    it.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Progress Bar (Placeholder)
            Slider(
                value = state.position.toFloat(),
                onValueChange = { /* viewModel.seekToPosition(it.toLong()) */ },
                valueRange = 0f..state.duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Time Display (Placeholder)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("0:00", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("3:00", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 5. Playback Controls (M3 Icons)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shuffle Button
                IconButton(onClick = { /* Toggle Shuffle */ }) {
                    Icon(Icons.Default.Shuffle, contentDescription = "Shuffle", modifier = Modifier.size(24.dp))
                }
                // Previous Button
                IconButton(onClick = { /* Skip Previous */ }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", modifier = Modifier.size(48.dp))
                }
                // Play/Pause Button (FAB-style for emphasis)
                FloatingActionButton(onClick = viewModel::togglePlayback, modifier = Modifier.size(72.dp)) {
                    Icon(
                        if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (state.isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(36.dp)
                    )
                }
                // Next Button
                IconButton(onClick = viewModel::skipToNext) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next", modifier = Modifier.size(48.dp))
                }
                // Repeat Button
                IconButton(onClick = { /* Toggle Repeat Mode */ }) {
                    Icon(Icons.Default.Repeat, contentDescription = "Repeat", modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}
