package com.melodym3.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NowPlayingBar() {
    // Surface for a floating, elevated look (M3 standard for bottom controls)
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Album Art / Title (Example Data)
            Column(modifier = Modifier.weight(1f)) {
                Text("Current Song Title", style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Text("Artist Name", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            
            Spacer(Modifier.width(16.dp))

            // 2. Play/Pause Button
            IconButton(onClick = { /* Toggle Play/Pause */ }) {
                Icon(Icons.Filled.Pause, contentDescription = "Pause")
            }
            
            // 3. Skip Next Button
            IconButton(onClick = { /* Skip to Next Track */ }) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Next")
            }
        }
    }
}
