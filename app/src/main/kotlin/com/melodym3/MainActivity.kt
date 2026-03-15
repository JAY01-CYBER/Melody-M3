package com.melodym3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
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

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : Screen("home", Icons.Filled.Home, "Home")
    data object Explore : Screen("explore", Icons.Filled.Search, "Explore")
    data object Library : Screen("library", Icons.Filled.LibraryMusic, "Library")
}

private val items = listOf(
    Screen.Home,
    Screen.Explore,
    Screen.Library
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MelodyM3App(snackbarManager = snackbarManager)
        }
    }
}

@Composable
fun MelodyM3App(snackbarManager: SnackbarManager) {
    var selectedItem by remember { mutableIntStateOf(0) }
    var isPlayerVisible by remember { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState = homeViewModel.uiState

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarManager) {
        snackbarManager.messages.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    MelodyM3Theme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                Column {
                    MiniPlayerBar(
                        onOpenPlayer = { isPlayerVisible = true }
                    )
                    BottomNavBar(
                        selectedItem = selectedItem,
                        onItemSelected = { selectedItem = it }
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (items[selectedItem]) {
                    Screen.Home -> HomeScreen(
                        recommendations = homeUiState.recommendations,
                        isLoading = homeUiState.isLoading,
                        errorMessage = homeUiState.errorMessage,
                        onItemClick = { item ->
                            homeViewModel.playItem(item)
                            isPlayerVisible = true
                        }
                    )

                    Screen.Explore -> ExploreScreen()

                    Screen.Library -> LibraryScreen()
                }

                if (isPlayerVisible) {
                    PlayerScreen(
                        onDismiss = { isPlayerVisible = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
private fun MiniPlayerBar(
    onOpenPlayer: () -> Unit
) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val playerState by playerViewModel.playerState.collectAsState()

    val currentItem = playerState.currentItem ?: return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable { onOpenPlayer() }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = currentItem.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentItem.subtitle ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = { playerViewModel.togglePlaybackFromMiniPlayer() }
            ) {
                Icon(
                    imageVector = if (playerState.isPlaying) {
                        Icons.Filled.Pause
                    } else {
                        Icons.Filled.PlayArrow
                    },
                    contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
