package com.melodym3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.melodym3.ui.components.NowPlayingBar // Assuming this is defined
import com.melodym3.ui.screens.ExploreScreen
import com.melodym3.ui.screens.HomeScreen
import com.melodym3.ui.screens.home.HomeViewModel
import com.melodym3.ui.screens.player.PlayerScreen // The full screen modal player
import com.melodym3.ui.theme.MelodyM3Theme
import dagger.hilt.android.AndroidEntryPoint

// --- 1. Navigation Definitions ---
sealed class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : Screen("home", Icons.Filled.Home, "Home")
    object Explore : Screen("explore", Icons.Filled.Search, "Explore")
    object Library : Screen("library", Icons.Filled.LibraryMusic, "Library")
}

val items = listOf(Screen.Home, Screen.Explore, Screen.Library)

// --- 2. Main Activity Entry Point ---
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MelodyM3App()
        }
    }
}

// --- 3. Main Application Composable ---
@Composable
fun MelodyM3App() {
    // State for bottom navigation
    var selectedItem by remember { mutableIntStateOf(0) }
    // State for showing the full screen modal player
    var isPlayerScreenVisible by remember { mutableStateOf(false) } 

    // Inject and observe the HomeViewModel (also triggers initialization)
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState = homeViewModel.uiState 

    MelodyM3Theme {
        Scaffold(
            // Bottom bar contains the Now Playing bar AND the Navigation Bar
            bottomBar = { 
                Column {
                    // Click on NowPlayingBar opens the full player modal
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
                    Screen.Library -> Text("Library Screen Placeholder")
                }
            }
            
            // 4. Full Screen Player Modal
            if (isPlayerScreenVisible) {
                // PlayerScreen opens on top of all content
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

// NOTE: NowPlayingBar definition must be updated in its file to accept onClick
@Composable
fun NowPlayingBar(onClick: () -> Unit) { 
    Surface(
        // Placeholder for real elevated design
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // The clickable modifier ensures the entire bar responds to taps
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable { onClick() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart // Use Box for simple centering
        ) {
            Text("Now Playing... (Tap to expand)", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
