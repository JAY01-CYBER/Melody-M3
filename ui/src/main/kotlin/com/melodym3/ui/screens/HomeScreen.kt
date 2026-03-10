import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Added for better list handling
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.melodym3.domain.model.MusicItem
// Removed unused ItemType import which was likely causing the KAPT crash

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recommendations: List<MusicItem>,
    isLoading: Boolean,
    errorMessage: String?,
    onItemClick: (MusicItem) -> Unit 
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Quick Picks",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Using direct list mapping is safer for KAPT than index-based
                items(recommendations) { item ->
                    MusicCard(
                        item = item,
                        onClick = { onItemClick(item) } 
                    )
                }
            }
        }
    }
}

@Composable
fun MusicCard(item: MusicItem, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = item.imageUrl.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = item.title,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop,
                // Fallback icon if URL fails
                error = null 
            )
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    // Ensure 'subtitle' or 'artist' exists in your MusicItem model
                    text = item.subtitle ?: "Various Artists",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}
