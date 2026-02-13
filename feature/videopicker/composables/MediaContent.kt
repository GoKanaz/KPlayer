package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MediaContent(
    items: List<MediaItem>,
    viewMode: ViewMode = ViewMode.Videos,
    displayMode: DisplayMode = DisplayMode.Grid,
    selectedItems: Set<String> = emptySet(),
    onItemClick: (MediaItem) -> Unit = {},
    onItemLongClick: (MediaItem) -> Unit = {},
    onRefresh: () -> Unit = {},
    onLoadMore: () -> Unit = {},
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isRefreshing: Boolean = false,
    hasMore: Boolean = true,
    emptyStateMessage: String = "No videos found",
    errorStateMessage: String = "Failed to load videos"
) {
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    
    val gridColumns = when (configuration.screenWidthDp) {
        in 0..360 -> 2
        in 361..600 -> 3
        in 601..840 -> 4
        else -> 5
    }
    
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (isLoading && items.isEmpty()) {
            LoadingContent()
        } else if (items.isEmpty()) {
            EmptyContent(message = emptyStateMessage)
        } else {
            when (displayMode) {
                DisplayMode.Grid -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridColumns),
                        state = gridState,
                        contentPadding = PaddingValues(4.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = if (hasMore) items.size + 1 else items.size,
                            key = { index ->
                                if (index < items.size) items[index].id
                                else "loading"
                            }
                        ) { index ->
                            if (index < items.size) {
                                val item = items[index]
                                AnimatedItem(
                                    visible = true,
                                    enter = fadeIn(
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            delayMillis = index * 50
                                        )
                                    )
                                ) {
                                    MediaGridItem(
                                        item = item,
                                        isSelected = selectedItems.contains(item.id),
                                        onClick = { onItemClick(item) },
                                        onLongClick = { onItemLongClick(item) }
                                    )
                                }
                            } else {
                                LoadingNextItem()
                            }
                        }
                    }
                    
                    if (hasMore && gridState.layoutInfo.totalItemsCount > 0) {
                        val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
                        val totalItems = gridState.layoutInfo.totalItemsCount
                        
                        LaunchedEffect(lastVisibleItem) {
                            if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 10) {
                                onLoadMore()
                            }
                        }
                    }
                }
                
                DisplayMode.List -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val groupedItems = groupItemsBySection(viewMode, items)
                        
                        groupedItems.forEach { (section, sectionItems) ->
                            if (section.isNotBlank()) {
                                stickyHeader {
                                    SectionHeader(title = section)
                                }
                            }
                            
                            items(
                                count = if (hasMore) sectionItems.size + 1 else sectionItems.size,
                                key = { index ->
                                    if (index < sectionItems.size) sectionItems[index].id
                                    else "loading-${section}"
                                }
                            ) { index ->
                                if (index < sectionItems.size) {
                                    val item = sectionItems[index]
                                    AnimatedItem(
                                        visible = true,
                                        enter = fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                delayMillis = index * 30
                                            )
                                        )
                                    ) {
                                        MediaListItem(
                                            item = item,
                                            isSelected = selectedItems.contains(item.id),
                                            onClick = { onItemClick(item) },
                                            onLongClick = { onItemLongClick(item) }
                                        )
                                    }
                                } else {
                                    LoadingNextItem()
                                }
                            }
                        }
                    }
                    
                    if (hasMore && listState.layoutInfo.totalItemsCount > 0) {
                        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        val totalItems = listState.layoutInfo.totalItemsCount
                        
                        LaunchedEffect(lastVisibleItem) {
                            if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 5) {
                                onLoadMore()
                            }
                        }
                    }
                }
            }
        }
        
        if (isRefreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center),
                    strokeWidth = 2.dp
                )
            }
        }
        
        val scrollToTopVisible = when (displayMode) {
            DisplayMode.Grid -> gridState.firstVisibleItemIndex > 0
            DisplayMode.List -> listState.firstVisibleItemIndex > 0
        }
        
        if (scrollToTopVisible) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        when (displayMode) {
                            DisplayMode.Grid -> gridState.animateScrollToItem(0)
                            DisplayMode.List -> listState.animateScrollToItem(0)
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

@Composable
private fun MediaGridItem(
    item: MediaItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.9f)
            .padding(4.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                AsyncImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    )
                    
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(24.dp)
                    )
                }
                
                if (item.duration > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = formatDuration(item.duration),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            IconButton(
                onClick = { /* Open context menu */ },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun MediaListItem(
    item: MediaItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                AsyncImage(
                    model = item.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                if (item.duration > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.dp),
                        shape = RoundedCornerShape(2.dp),
                        color = Color.Black.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = formatDuration(item.duration),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (item.fileSize > 0) {
                        DotDivider()
                        Text(
                            text = formatFileSize(item.fileSize),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (item.resolution.isNotBlank()) {
                        DotDivider()
                        Text(
                            text = item.resolution,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            IconButton(onClick = { /* Open context menu */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Loading videos...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyContent(message: String) {
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
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Pull to refresh",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun LoadingNextItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            strokeWidth = 2.dp
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        shadowElevation = 2.dp
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun DotDivider() {
    Box(
        modifier = Modifier
            .size(4.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(2.dp)
            )
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedItem(
    visible: Boolean,
    enter: EnterTransition = fadeIn() + expandIn(),
    exit: ExitTransition = fadeOut() + shrinkOut(),
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}

private fun groupItemsBySection(viewMode: ViewMode, items: List<MediaItem>): Map<String, List<MediaItem>> {
    return when (viewMode) {
        ViewMode.Folders -> items.groupBy { it.folderName }
        ViewMode.Videos -> items.groupBy { it.sectionDate }
        ViewMode.Recent -> items.groupBy { "Recent" }
        ViewMode.Favorites -> items.groupBy { "Favorites" }
    }
}

enum class ViewMode {
    Folders,
    Videos,
    Recent,
    Favorites
}

enum class DisplayMode {
    Grid,
    List
}

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val thumbnail: Any?,
    val duration: Long,
    val fileSize: Long,
    val resolution: String,
    val folderName: String,
    val sectionDate: String,
    val path: String,
    val dateAdded: Long,
    val isFavorite: Boolean = false
)

private fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%02d:%02d", minutes, secs)
    }
}

private fun formatFileSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    
    return when {
        gb >= 1 -> String.format("%.1f GB", gb)
        mb >= 1 -> String.format("%.1f MB", mb)
        kb >= 1 -> String.format("%.1f KB", kb)
        else -> "$bytes B"
    }
}
