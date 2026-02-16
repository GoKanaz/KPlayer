package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.gokanaz.kplayer.feature.videopicker.VideoPickerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaView(
    viewModel: VideoPickerViewModel = viewModel(),
    onVideoClick: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var showSearch by remember { mutableStateOf(false) }
    var showSortFilter by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MediaTopBar(
                title = when (uiState.viewMode) {
                    ViewMode.Folders -> "Folders"
                    ViewMode.Videos -> "Videos"
                    ViewMode.Recent -> "Recent"
                    ViewMode.Favorites -> "Favorites"
                },
                isSelectionMode = uiState.isSelectionMode,
                selectedCount = uiState.selectedItems.size,
                showSearch = showSearch,
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    searchQuery = query
                    viewModel.onSearchQueryChanged(query)
                },
                onSearchToggle = { showSearch = !showSearch },
                onSortFilterClick = { showSortFilter = true },
                onViewModeToggle = { viewModel.toggleDisplayMode() },
                onSelectionClear = { viewModel.clearSelection() },
                onSelectionDelete = { },
                onSelectionShare = { },
                onSelectionAddToPlaylist = { },
                onNavigationClick = onNavigateBack,
                currentDisplayMode = uiState.displayMode
            )
        },
        bottomBar = {
            if (!isLandscape) {
                MediaBottomBar(
                    viewMode = uiState.viewMode,
                    onViewModeChange = { viewModel.setViewMode(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        floatingActionButton = {
            if (!uiState.isSelectionMode) {
                FloatingActionButton(
                    onClick = { },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Scan"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MediaContent(
                items = uiState.items,
                viewMode = uiState.viewMode,
                displayMode = uiState.displayMode,
                selectedItems = uiState.selectedItems,
                onItemClick = { item ->
                    if (uiState.isSelectionMode) {
                        viewModel.toggleItemSelection(item.id)
                    } else {
                        onVideoClick(item.id)
                    }
                },
                onItemLongClick = { item ->
                    viewModel.startSelection(item.id)
                },
                onRefresh = { viewModel.refreshVideos() },
                onLoadMore = { viewModel.loadMoreVideos() },
                isLoading = uiState.isLoading,
                isRefreshing = uiState.isRefreshing,
                hasMore = uiState.hasMore,
                emptyStateMessage = when (uiState.viewMode) {
                    ViewMode.Folders -> "No folders found"
                    ViewMode.Videos -> "No videos found"
                    ViewMode.Recent -> "No recent videos"
                    ViewMode.Favorites -> "No favorites yet"
                },
                modifier = Modifier.fillMaxSize()
            )
            
            if (showSortFilter) {
                QuickSettingDialog(
                    currentSort = uiState.sortOption,
                    currentSortOrder = uiState.sortOrder,
                    currentFilters = uiState.filters,
                    onDismiss = { showSortFilter = false },
                    onApply = { sort, order, filters ->
                        viewModel.applySortAndFilter(sort, order, filters)
                        showSortFilter = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaTopBar(
    title: String,
    isSelectionMode: Boolean,
    selectedCount: Int,
    showSearch: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onSortFilterClick: () -> Unit,
    onViewModeToggle: () -> Unit,
    onSelectionClear: () -> Unit,
    onSelectionDelete: () -> Unit,
    onSelectionShare: () -> Unit,
    onSelectionAddToPlaylist: () -> Unit,
    onNavigationClick: () -> Unit,
    currentDisplayMode: DisplayMode
) {
    if (isSelectionMode) {
        SelectionModeTopBar(
            selectedCount = selectedCount,
            onClearSelection = onSelectionClear,
            onDelete = onSelectionDelete,
            onShare = onSelectionShare,
            onAddToPlaylist = onSelectionAddToPlaylist,
            onNavigateBack = onNavigationClick
        )
    } else if (showSearch) {
        SearchTopBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            onClose = onSearchToggle
        )
    } else {
        DefaultTopBar(
            title = title,
            onSearchClick = onSearchToggle,
            onSortFilterClick = onSortFilterClick,
            onViewModeToggle = onViewModeToggle,
            onNavigateBack = onNavigationClick,
            currentDisplayMode = currentDisplayMode
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DefaultTopBar(
    title: String,
    onSearchClick: () -> Unit,
    onSortFilterClick: () -> Unit,
    onViewModeToggle: () -> Unit,
    onNavigateBack: () -> Unit,
    currentDisplayMode: DisplayMode
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
            
            IconButton(onClick = onSortFilterClick) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = "Sort & Filter"
                )
            }
            
            IconButton(onClick = onViewModeToggle) {
                Icon(
                    imageVector = if (currentDisplayMode == DisplayMode.Grid) {
                        Icons.Default.ViewList
                    } else {
                        Icons.Default.ViewModule
                    },
                    contentDescription = if (currentDisplayMode == DisplayMode.Grid) {
                        "Switch to list"
                    } else {
                        "Switch to grid"
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search videos...") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close search"
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectionModeTopBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedCount selected") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onAddToPlaylist) {
                Icon(
                    imageVector = Icons.Default.PlaylistAdd,
                    contentDescription = "Add to playlist"
                )
            }
            
            IconButton(onClick = onShare) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share"
                )
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
            
            IconButton(onClick = onClearSelection) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear selection"
                )
            }
        }
    )
}

@Composable
private fun MediaBottomBar(
    viewMode: ViewMode,
    onViewModeChange: (ViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = viewMode == ViewMode.Folders,
            onClick = { onViewModeChange(ViewMode.Folders) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = "Folders"
                )
            },
            label = { Text("Folders") }
        )
        
        NavigationBarItem(
            selected = viewMode == ViewMode.Videos,
            onClick = { onViewModeChange(ViewMode.Videos) },
            icon = {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = "All videos"
                )
            },
            label = { Text("All videos") }
        )
        
        NavigationBarItem(
            selected = viewMode == ViewMode.Recent,
            onClick = { onViewModeChange(ViewMode.Recent) },
            icon = {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Recent"
                )
            },
            label = { Text("Recent") }
        )
        
        NavigationBarItem(
            selected = viewMode == ViewMode.Favorites,
            onClick = { onViewModeChange(ViewMode.Favorites) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites"
                )
            },
            label = { Text("Favorites") }
        )
    }
}
