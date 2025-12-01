package com.melodym3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.melodym3.ui.components.NowPlayingBar
import com.melodym3.ui.screens.ExploreScreen // Import the new Explore Screen
import com.melodym3.ui.screens.HomeScreen
import com.melodym3.ui.screens.home.HomeViewModel
import com.melodym3.ui.theme.MelodyM3Theme
import dagger.hilt.android.AndroidEntryPoint

// Define the screens for Navigation Bar
sealed class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : Screen("home", Icons.Filled.Home, "Home")
    object Explore : Screen("explore", Icons.Filled.Search, "Explore")
    object Library : Screen("library", Icons.Filled.LibraryMusic, "Library")
}

val items = listOf(Screen.Home, Screen.Explore, Screen.Library)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MelodyM3App()
        }
    }
}

@Composable
fun MelodyM3App() {
    var selectedItem by remember { mutableIntStateOf(0) }
    
    // Inject and observe the HomeViewModel for the Home Screen
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState = homeViewModel.uiState 

    MelodyM3Theme {
        Scaffold(
            // Combine NowPlayingBar and Navigation Bar in the bottomBar slot
            bottomBar = { 
                Column {
                    NowPlayingBar() 
                    AppNavigationBar(selectedItem) { index -> selectedItem = index } 
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                // Handle screen navigation based on the selected item
                when (items[selectedItem]) {
                    Screen.Home -> HomeScreen(
                        recommendations = homeUiState.recommendations,
                        isLoading = homeUiState.isLoading,
                        errorMessage = homeUiState.errorMessage,
                        onItemClick = homeViewModel::playItem 
                    )
                    // NEW INTEGRATION: Load the ExploreScreen (Search UI)
                    Screen.Explore -> ExploreScreen() 
                    Screen.Library -> Text("Library Screen Placeholder")
                }
            }
        }
    }
}

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
