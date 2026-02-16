package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import dev.gokanaz.kplayer.core.model.MediaLayoutMode
import dev.gokanaz.kplayer.core.model.Video
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ContextMenuItem {
    Play, Delete, Share, Info, Rename, AddToPlaylist, ShowInFolder
}

@Composable
private fun VideoContextMenu(
    video: Video,
    onDismiss: () -> Unit,
    onItemClick: (ContextMenuItem) -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("Play") },
            onClick = { onItemClick(ContextMenuItem.Play) },
            leadingIcon = { Icon(Icons.Default.PlayArrow, null) }
        )
        DropdownMenuItem(
            text = { Text("Info") },
            onClick = { onItemClick(ContextMenuItem.Info) },
            leadingIcon = { Icon(Icons.Default.Info, null) }
        )
        DropdownMenuItem(
            text = { Text("Share") },
            onClick = { onItemClick(ContextMenuItem.Share) },
            leadingIcon = { Icon(Icons.Default.Share, null) }
        )
        DropdownMenuItem(
            text = { Text("Rename") },
            onClick = { onItemClick(ContextMenuItem.Rename) },
            leadingIcon = { Icon(Icons.Default.Edit, null) }
        )
        DropdownMenuItem(
            text = { Text("Add to playlist") },
            onClick = { onItemClick(ContextMenuItem.AddToPlaylist) },
            leadingIcon = { Icon(Icons.Default.PlaylistAdd, null) }
        )
        DropdownMenuItem(
            text = { Text("Show in folder") },
            onClick = { onItemClick(ContextMenuItem.ShowInFolder) },
            leadingIcon = { Icon(Icons.Default.FolderOpen, null) }
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            onClick = { onItemClick(ContextMenuItem.Delete) },
            leadingIcon = { Icon(Icons.Default.Delete, null) }
        )
    }
}

@Composable
private fun ShimmerEffect(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    Box(
        modifier = modifier.background(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)
        )
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun VideoItem(
    video: Video,
    layoutMode: MediaLayoutMode,
    isSelected: Boolean = false,
    isSelectionMode: Boolean = false,
    isFavorite: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onContextMenuItemClick: (ContextMenuItem) -> Unit = {}
) {
    when (layoutMode) {
        MediaLayoutMode.GRID -> {
            GridVideoItem(
                video = video,
                isSelected = isSelected,
                isSelectionMode = isSelectionMode,
                isFavorite = isFavorite,
                onClick = onClick,
                onLongClick = onLongClick,
                onFavoriteClick = onFavoriteClick,
                onContextMenuItemClick = onContextMenuItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        }
        MediaLayoutMode.LIST -> {
            ListVideoItem(
                video = video,
                isSelected = isSelected,
                isSelectionMode = isSelectionMode,
                isFavorite = isFavorite,
                onClick = onClick,
                onLongClick = onLongClick,
                onFavoriteClick = onFavoriteClick,
                onContextMenuItemClick = onContextMenuItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GridVideoItem(
    video: Video,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onContextMenuItemClick: (ContextMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showShimmer by remember { mutableStateOf(true) }
    var imageLoadError by remember { mutableStateOf(false) }
    var showContextMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(video.thumbnail) {
        showShimmer = true
        imageLoadError = false
    }

    Card(
        modifier = modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = {
                    if (showShimmer) {
                        ShimmerEffect(modifier = Modifier.fillMaxSize())
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoFile,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                },
                error = {
                    imageLoadError = true
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Failed to load",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                onSuccess = {
                    scope.launch {
                        delay(300)
                        showShimmer = false
                    }
                }
            )

            if (!imageLoadError && !showShimmer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 0.7f
                            )
                        )
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                shape = RoundedCornerShape(4.dp),
                color = Color.Black.copy(alpha = 0.7f)
            ) {
                Text(
                    text = formatDuration(video.duration),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            if (video.width >= 1920 || video.height >= 1080) {
                val resolutionText = when {
                    video.width >= 3840 || video.height >= 2160 -> "4K"
                    video.width >= 1920 || video.height >= 1080 -> "1080p"
                    video.width >= 1280 || video.height >= 720 -> "720p"
                    else -> ""
                }
                if (resolutionText.isNotBlank()) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = resolutionText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (isFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp),
                    tint = Color.Red
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = if (imageLoadError || showShimmer) Color.Transparent else Color.White
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoChip(text = formatFileSize(video.size), size = ChipSize.Small)
                    if (video.width > 0 && video.height > 0) {
                        InfoChip(text = "${video.height}p", size = ChipSize.Small)
                    }
                }
            }

            if (isSelectionMode) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else Color.Black.copy(alpha = 0.1f)
                        )
                )
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .size(24.dp)
                )
            }

            IconButton(
                onClick = { showContextMenu = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    if (showContextMenu) {
        VideoContextMenu(
            video = video,
            onDismiss = { showContextMenu = false },
            onItemClick = { item ->
                onContextMenuItemClick(item)
                showContextMenu = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListVideoItem(
    video: Video,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onContextMenuItemClick: (ContextMenuItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showContextMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
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
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = null,
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
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(video.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoFile,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp),
                    shape = RoundedCornerShape(2.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = formatDurationShort(video.duration),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 1.dp)
                    )
                }

                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                            .size(16.dp),
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
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
                    InfoChip(
                        text = formatDuration(video.duration),
                        size = ChipSize.Small,
                        type = ChipType.Duration
                    )
                    InfoChip(
                        text = formatFileSize(video.size),
                        size = ChipSize.Small,
                        type = ChipType.FileSize
                    )
                    if (video.width > 0 && video.height > 0) {
                        InfoChip(
                            text = "${video.height}p",
                            size = ChipSize.Small,
                            type = ChipType.Resolution
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = video.filePath,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = { showContextMenu = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    if (showContextMenu) {
        VideoContextMenu(
            video = video,
            onDismiss = { showContextMenu = false },
            onItemClick = { item ->
                onContextMenuItemClick(item)
                showContextMenu = false
            }
        )
    }
}

private fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, secs)
    else String.format("%02d:%02d", minutes, secs)
}

private fun formatDurationShort(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", minutes, secs)
}

private fun formatFileSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1 -> String.format("%.1fGB", gb)
        mb >= 1 -> String.format("%.1fMB", mb)
        kb >= 1 -> String.format("%.0fKB", kb)
        else -> "${bytes}B"
    }
}
