package dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gokanaz.kplayer.core.model.MediaLayoutMode
import dev.gokanaz.kplayer.core.model.MediaViewMode
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.feature.videopicker.composables.*
import dev.gokanaz.kplayer.feature.videopicker.extensions.*
import dev.gokanaz.kplayer.feature.videopicker.screens.MediaItem
import dev.gokanaz.kplayer.feature.videopicker.screens.MediaState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediaPickerScreen(
    viewModel: MediaPickerViewModel,
    folderName: String? = null,
    isSearchMode: Boolean = false,
    onFolderClick: (String, String) -> Unit = { _, _ -> },
    onVideoClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val mediaState by viewModel.mediaState.collectAsStateWithLifecycle()
    val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()
    val layoutMode by viewModel.layoutMode.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val filterOptions by viewModel.filterOptions.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val currentFolder by viewModel.currentFolder.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    val selectionManager = viewModel.selectionManager
    val selectedItems by selectionManager.selectedItems.collectAsStateWithLifecycle()
    val isSelectionMode by selectionManager.selectionMode.collectAsStateWithLifecycle()
    
    var showSearch by remember { mutableStateOf(isSearchMode) }
    var showSortFilter by remember { mutableStateOf(false) }
    var localSearchQuery by remember { mutableStateOf(searchQuery) }
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(searchQuery) {
        localSearchQuery = searchQuery
    }
    
    Scaffold(
        topBar = {
            MediaPickerTopBar(
                title = when {
                    folderName != null -> folderName
                    isSearchMode -> "Search"
                    else -> viewMode.toDisplayName(LocalContext.current)
                },
                isSelectionMode = isSelectionMode,
                selectedCount = selectedItems.size,
                showSearch = showSearch,
                searchQuery = localSearchQuery,
                onSearchQueryChange = { query ->
                    localSearchQuery = query
                    viewModel.onSearchQueryChanged(query)
                },
                onSearchToggle = { showSearch = !showSearch },
                onSortFilterClick = { showSortFilter = true },
                onViewModeToggle = {
                    viewModel.toggleLayoutMode()
                },
                onSelectionClear = { selectionManager.clearSelection() },
                onSelectionDelete = {
                    selectionManager.deleteSelected(
                        onConfirm = { viewModel.refresh() }
                    )
                },
                onSelectionShare = { selectionManager.shareSelected() },
                onSelectionAddToPlaylist = { /* Handle add to playlist */ },
                onNavigationClick = onNavigateBack,
                currentLayoutMode = layoutMode,
                showBackButton = folderName != null || isSearchMode
            )
        },
        bottomBar = {
            if (isSelectionMode) {
                SelectionBottomBar(
                    selectedCount = selectedItems.size,
                    onPlayAll = { selectionManager.playSelected() },
                    onAddToPlaylist = { /* Handle add to playlist */ },
                    onShare = { selectionManager.shareSelected() },
                    onDelete = {
                        selectionManager.deleteSelected(
                            onConfirm = { viewModel.refresh() }
                        )
                    },
                    onCancel = { selectionManager.clearSelection() },
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (!isLandscape && !isSearchMode) {
                MediaViewModeBar(
                    viewMode = viewMode,
                    onViewModeChange = { viewModel.setViewMode(it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        floatingActionButton = {
            if (!isSelectionMode && !isSearchMode) {
                FloatingActionButton(
                    onClick = { viewModel.refresh() },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
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
            when (mediaState) {
                is MediaState.Loading && (mediaState as MediaState.Loading).isLoading() -> {
                    LoadingContent(layoutMode)
                }
                
                is MediaState.Error -> {
                    ErrorContent(
                        message = (mediaState as MediaState.Error).message,
                        onRetry = { viewModel.refresh() }
                    )
                }
                
                is MediaState.Empty, is MediaState.Success -> {
                    val items = mediaState.getItems()
                    
                    if (items.isEmpty()) {
                        EmptyContent(
                            message = when {
                                isSearchMode -> "No videos found for \"$searchQuery\""
                                folderName != null -> "No videos in this folder"
                                else -> "No ${viewMode.toDisplayName(LocalContext.current).lowercase()} found"
                            },
                            onRefresh = { viewModel.refresh() }
                        )
                    } else {
                        if (layoutMode == MediaLayoutMode.GRID) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 160.dp),
                                state = gridState,
                                contentPadding = PaddingValues(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = items,
                                    key = { it.id }
                                ) { item ->
                                    when (item) {
                                        is MediaItem.VideoItem -> {
                                            VideoItem(
                                                video = item.video,
                                                layoutMode = layoutMode,
                                                isSelected = selectionManager.isSelected(item.id),
                                                isSelectionMode = isSelectionMode,
                                                isFavorite = item.video.isFavorite,
                                                onClick = {
                                                    if (isSelectionMode) {
                                                        selectionManager.toggleSelection(item.id)
                                                    } else {
                                                        onVideoClick(item.video.id)
                                                    }
                                                },
                                                onLongClick = {
                                                    selectionManager.enterSelectionMode()
                                                    selectionManager.toggleSelection(item.id)
                                                },
                                                onFavoriteClick = { /* Handle favorite */ },
                                                onContextMenuItemClick = { menuItem ->
                                                    when (menuItem) {
                                                        ContextMenuItem.Play -> onVideoClick(item.video.id)
                                                        ContextMenuItem.Delete -> selectionManager.deleteItem(item.id, item.video)
                                                        ContextMenuItem.Share -> selectionManager.shareItem(item.video)
                                                        ContextMenuItem.Info -> viewModel.showVideoInfo(item.video)
                                                        else -> {}
                                                    }
                                                }
                                            )
                                        }
                                        is MediaItem.FolderItem -> {
                                            FolderItem(
                                                folder = item.folder,
                                                isSelected = selectionManager.isSelected(item.id),
                                                isGridMode = true,
                                                onFolderClick = {
                                                    if (isSelectionMode) {
                                                        selectionManager.toggleSelection(item.id)
                                                    } else {
                                                        onFolderClick(item.folder.id, item.folder.name)
                                                    }
                                                },
                                                onFolderLongClick = {
                                                    selectionManager.enterSelectionMode()
                                                    selectionManager.toggleSelection(item.id)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                state = listState,
                                contentPadding = PaddingValues(vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = items,
                                    key = { it.id }
                                ) { item ->
                                    when (item) {
                                        is MediaItem.VideoItem -> {
                                            VideoItem(
                                                video = item.video,
                                                layoutMode = layoutMode,
                                                isSelected = selectionManager.isSelected(item.id),
                                                isSelectionMode = isSelectionMode,
                                                isFavorite = item.video.isFavorite,
                                                onClick = {
                                                    if (isSelectionMode) {
                                                        selectionManager.toggleSelection(item.id)
                                                    } else {
                                                        onVideoClick(item.video.id)
                                                    }
                                                },
                                                onLongClick = {
                                                    selectionManager.enterSelectionMode()
                                                    selectionManager.toggleSelection(item.id)
                                                },
                                                onFavoriteClick = { /* Handle favorite */ },
                                                onContextMenuItemClick = { menuItem ->
                                                    when (menuItem) {
                                                        ContextMenuItem.Play -> onVideoClick(item.video.id)
                                                        ContextMenuItem.Delete -> selectionManager.deleteItem(item.id, item.video)
                                                        ContextMenuItem.Share -> selectionManager.shareItem(item.video)
                                                        ContextMenuItem.Info -> viewModel.showVideoInfo(item.video)
                                                        else -> {}
                                                    }
                                                }
                                            )
                                        }
                                        is MediaItem.FolderItem -> {
                                            FolderItem(
                                                folder = item.folder,
                                                isSelected = selectionManager.isSelected(item.id),
                                                isGridMode = false,
                                                onFolderClick = {
                                                    if (isSelectionMode) {
                                                        selectionManager.toggleSelection(item.id)
                                                    } else {
                                                        onFolderClick(item.folder.id, item.folder.name)
                                                    }
                                                },
                                                onFolderLongClick = {
                                                    selectionManager.enterSelectionMode()
                                                    selectionManager.toggleSelection(item.id)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            if (showSortFilter) {
                QuickSettingDialog(
                    currentSort = sortOption,
                    currentSortOrder = sortOrder,
                    currentFilters = filterOptions,
                    onDismiss = { showSortFilter = false },
                    onApply = { sort, order, filters ->
                        viewModel.updateSort(sort, order)
                        viewModel.updateFilter(filters)
                        showSortFilter = false
                    }
                )
            }
            
            val scrollToTopVisible = if (layoutMode == MediaLayoutMode.GRID) {
                gridState.firstVisibleItemIndex > 0
            } else {
                listState.firstVisibleItemIndex > 0
            }
            
            if (scrollToTopVisible && !isSelectionMode) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            if (layoutMode == MediaLayoutMode.GRID) {
                                gridState.animateScrollToItem(0)
                            } else {
                                listState.animateScrollToItem(0)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Scroll to top"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaPickerTopBar(
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
    currentLayoutMode: MediaLayoutMode,
    showBackButton: Boolean = false
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
            showBackButton = showBackButton,
            onSearchClick = onSearchToggle,
            onSortFilterClick = onSortFilterClick,
            onViewModeToggle = onViewModeToggle,
            onNavigateBack = onNavigationClick,
            currentLayoutMode = currentLayoutMode
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultTopBar(
    title: String,
    showBackButton: Boolean,
    onSearchClick: () -> Unit,
    onSortFilterClick: () -> Unit,
    onViewModeToggle: () -> Unit,
    onNavigateBack: () -> Unit,
    currentLayoutMode: MediaLayoutMode
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
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
                    imageVector = currentLayoutMode.opposite().toIcon(),
                    contentDescription = if (currentLayoutMode.isGrid()) {
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
private fun SearchTopBar(
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
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionModeTopBar(
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
private fun MediaViewModeBar(
    viewMode: MediaViewMode,
    onViewModeChange: (MediaViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        MediaViewMode.entries.forEach { mode ->
            NavigationBarItem(
                selected = viewMode == mode,
                onClick = { onViewModeChange(mode) },
                icon = {
                    Icon(
                        imageVector = mode.toIcon(),
                        contentDescription = null
                    )
                },
                label = { Text(mode.toDisplayName(LocalContext.current)) }
            )
        }
    }
}

@Composable
private fun SelectionBottomBar(
    selectedCount: Int,
    onPlayAll: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SelectionAction(
                icon = Icons.Default.PlayArrow,
                text = "Play",
                onClick = onPlayAll
            )
            
            SelectionAction(
                icon = Icons.Default.PlaylistAdd,
                text = "Playlist",
                onClick = onAddToPlaylist
            )
            
            SelectionAction(
                icon = Icons.Default.Share,
                text = "Share",
                onClick = onShare
            )
            
            SelectionAction(
                icon = Icons.Default.Delete,
                text = "Delete",
                onClick = onDelete
            )
            
            SelectionAction(
                icon = Icons.Default.Clear,
                text = "Cancel",
                onClick = onCancel
            )
        }
    }
}

@Composable
private fun SelectionAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun LoadingContent(layoutMode: MediaLayoutMode) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(10) {
            VideoItemPlaceholder(layoutMode)
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Error",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun EmptyContent(
    message: String,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.VideoLibrary,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}
