package dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.gokanaz.kplayer.core.model.MediaLayoutMode
import dev.gokanaz.kplayer.core.model.MediaViewMode
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.feature.videopicker.composables.*
import dev.gokanaz.kplayer.feature.videopicker.composables.toSortOption
import dev.gokanaz.kplayer.feature.videopicker.composables.toComposableType
import dev.gokanaz.kplayer.feature.videopicker.composables.toSortType
import dev.gokanaz.kplayer.feature.videopicker.composables.toCoreType
import dev.gokanaz.kplayer.feature.videopicker.composables.toViewModelType
import dev.gokanaz.kplayer.feature.videopicker.composables.toDisplayMode
import dev.gokanaz.kplayer.feature.videopicker.extensions.*
import dev.gokanaz.kplayer.feature.videopicker.screens.MediaItem
import kotlinx.coroutines.launch

import android.content.Context

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
    
    val context = LocalContext.current
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
                    else -> viewMode.toDisplayName(context)
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
                onSelectionAddToPlaylist = { },
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
                    onAddToPlaylist = { },
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
                is MediaState.Loading -> {
                    if ((mediaState as MediaState.Loading).isLoading()) {
                        LoadingContent(layoutMode)
                    }
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
                                else -> "No ${viewMode.toDisplayName(context).lowercase()} found"
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
                                                onFavoriteClick = { },
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
                                                onFavoriteClick = { },
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
                    currentSort = sortOption.toSortOption(),
                    currentSortOrder = sortOrder.toComposableType(),
                    currentFilters = filterOptions,
                    onDismiss = { showSortFilter = false },
                    onApply = { sort, order, filters ->
                        viewModel.updateSort(sort.toSortType(), order.toCoreType())
                        viewModel.updateFilter(filters.toViewModelType())
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
            currentDisplayMode = currentLayoutMode.toDisplayMode()
        )
    }
}
