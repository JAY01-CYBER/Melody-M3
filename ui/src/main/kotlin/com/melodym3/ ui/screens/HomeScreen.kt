// ... (Existing Imports) ...
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage // <-- Coil Import
// ... (Other Composables) ...

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
            
            // --- UPDATED: Coil AsyncImage ---
            AsyncImage(
                model = item.imageUrl.ifEmpty { 
                    "https://via.placeholder.com/64?text=Melody" // Fallback Placeholder
                },
                contentDescription = item.title,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop, 
            )
            // ---------------------------------
            
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
// ... (HomeScreen composable remains the same) ...
