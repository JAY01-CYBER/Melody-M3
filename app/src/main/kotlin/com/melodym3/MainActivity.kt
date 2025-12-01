package com.melodym3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.melodym3.service.SnackbarManager
import com.melodym3.ui.screens.ExploreScreen
import com.melodym3.ui.screens.HomeScreen
import com.melodym3.ui.screens.LibraryScreen
import com.melodym3.ui.screens.home.HomeViewModel
import com.melodym3.ui.screens.player.PlayerScreen
import com.melodym3.ui.screens.player.PlayerViewModel
import com.melodym3.ui.theme.MelodyM3Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- 1. Navigation Definitions ---
sealed class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : Screen("home", Icons.Filled.Home, "Home")
    object Explore : Screen("explore", Icons.Filled.Search, "Explore")
    object Library : Screen("library", Icons.Filled.LibraryMusic, "Library")
}

val items = listOf(Screen.Home, Screen.Explore, Screen.Library)

// --- 2. Main Activity Entry Point (Hilt Annotation) ---
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Inject the SnackbarManager at the activity level (Step 22)
    @Inject
    lateinit var snackbarManager: SnackbarManager 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Pass the injected manager to the composable function
            MelodyM3App(snackbarManager = snackbarManager) 
        }
    }
}

// --- 3. Main Application Composable ---
@Composable
fun MelodyM3App(snackbarManager: SnackbarManager) { 
    // State for bottom navigation
    var selectedItem by remember { mutableIntStateOf(0) }
    // State for showing the full screen modal player
    var isPlayerScreenVisible by remember { mutableStateOf(false) } 

    // Inject and observe the HomeViewModel (for initial data/playback setup)
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState = homeViewModel.uiState 
    
    // Snackbar Host State (Step 22)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Listener for messages coming from the ViewModel/Repository layer (Step 22)
    LaunchedEffect(snackbarManager) {
        snackbarManager.messages.collect { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    MelodyM3Theme {
        Scaffold(
            // Set up the SnackbarHost
            snackbarHost = { SnackbarHost(snackbarHostState) }, 
            
            // Bottom bar contains the Now Playing bar AND the Navigation Bar
            bottomBar = { 
                Column {
                    // Mini-Player: Click opens the full player modal
                    NowPlayingBar(onClick = { isPlayerScreenVisible = true }) 
                    AppNavigationBar(selectedItem) { index -> selectedItem = index } 
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                // Handle screen navigation
                when (items[selectedItem]) {
                    Screen.Home -> HomeScreen(
                        recommendations = homeUiState.recommendations,
                        isLoading = homeUiState.isLoading,
                        errorMessage = homeUiState.errorMessage,
                        onItemClick = { item -> 
                            homeViewModel.playItem(item)
                            isPlayerScreenVisible = true // Start playback and open player
                        }
                    )
                    Screen.Explore -> ExploreScreen() 
                    Screen.Library -> LibraryScreen() 
                }
            }
            
            // 4. Full Screen Player Modal (Displayed over all content)
            if (isPlayerScreenVisible) {
                PlayerScreen(onDismiss = { isPlayerScreenVisible = false }) 
            }
        }
    }
}

// --- 5. Navigation Bar Composable ---
@Composable
fun AppNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

// --- 6. Now Playing Bar Component (State-driven Mini-Player) ---
@Composable
fun NowPlayingBar(onClick: () -> Unit) { 
    // Inject the PlayerViewModel to get real-time state
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val state by playerViewModel.playerState.collectAsState()
    
    // Mini-player only appears if a song is loaded (current item is not null)
    if (state.currentItem == null) {
        return 
    }

    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                // Clicking the entire row opens the full player
                .clickable { onClick() } 
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Song Info (Text)
            Column(
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text(
                    state.currentItem?.title ?: "Unknown Track",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    state.currentItem?.subtitle ?: "Unknown Artist",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            
            // 2. Play/Pause Button
            IconButton(
                onClick = { 
                    playerViewModel.togglePlaybackFromMiniPlayer()
                }
            ) {
                Icon(
                    imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (state.isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
