package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.gokanaz.kplayer.core.model.VideoFolder
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderItem(
    folder: VideoFolder,
    isSelected: Boolean = false,
    isGridMode: Boolean = false,
    onFolderClick: (VideoFolder) -> Unit = {},
    onFolderLongClick: (VideoFolder) -> Unit = {},
    onContextMenuClick: (VideoFolder, ContextMenuItem) -> Unit = { _, _ -> }
) {
    if (isGridMode) {
        FolderGridItem(
            folder = folder,
            isSelected = isSelected,
            onFolderClick = onFolderClick,
            onFolderLongClick = onFolderLongClick,
            onContextMenuClick = onContextMenuClick
        )
    } else {
        FolderListItem(
            folder = folder,
            isSelected = isSelected,
            onFolderClick = onFolderClick,
            onFolderLongClick = onFolderLongClick,
            onContextMenuClick = onContextMenuClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderListItem(
    folder: VideoFolder,
    isSelected: Boolean,
    onFolderClick: (VideoFolder) -> Unit,
    onFolderLongClick: (VideoFolder) -> Unit,
    onContextMenuClick: (VideoFolder, ContextMenuItem) -> Unit
) {
    val context = LocalContext.current
    var showContextMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(
                onClick = { onFolderClick(folder) },
                onLongClick = { onFolderLongClick(folder) }
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
                if (folder.thumbnails.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(folder.thumbnails.take(4)) { thumbnail ->
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(thumbnail)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = folder.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (folder.isNew) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "NEW",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${folder.videoCount} videos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    if (folder.totalDuration > 0) {
                        DotDivider()
                        Text(
                            text = formatDuration(folder.totalDuration),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (folder.totalSize > 0) {
                        DotDivider()
                        Text(
                            text = formatFileSize(folder.totalSize),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            if (folder.isScanning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = { showContextMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
            }
        }
    }
    
    if (showContextMenu) {
        FolderContextMenu(
            folder = folder,
            onDismiss = { showContextMenu = false },
            onItemClick = { item ->
                onContextMenuClick(folder, item)
                showContextMenu = false
            }
        )
    }
}

@Composable
private fun FolderGridItem(
    folder: VideoFolder,
    isSelected: Boolean,
    onFolderClick: (VideoFolder) -> Unit,
    onFolderLongClick: (VideoFolder) -> Unit,
    onContextMenuClick: (VideoFolder, ContextMenuItem) -> Unit
) {
    val context = LocalContext.current
    var showContextMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable(
                onClick = { onFolderClick(folder) },
                onLongClick = { onFolderLongClick(folder) }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    if (folder.thumbnails.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(folder.thumbnails.take(4)) { thumbnail ->
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(thumbnail)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Folder,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
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
                    
                    if (folder.isNew) {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp),
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "NEW",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    
                    if (folder.isScanning) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                        )
                        
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            color = Color.White
                        )
                    }
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = folder.name,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = "${folder.videoCount} videos",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
    
    if (showContextMenu) {
        FolderContextMenu(
            folder = folder,
            onDismiss = { showContextMenu = false },
            onItemClick = { item ->
                onContextMenuClick(folder, item)
                showContextMenu = false
            }
        )
    }
}

@Composable
private fun FolderContextMenu(
    folder: VideoFolder,
    onDismiss: () -> Unit,
    onItemClick: (ContextMenuItem) -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        ContextMenuItem.entries.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.displayName) },
                onClick = { onItemClick(item) },
                leadingIcon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

enum class ContextMenuItem(
    val displayName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Play("Play", Icons.Default.PlayArrow),
    Rename("Rename", Icons.Default.Edit),
    Delete("Delete", Icons.Default.Delete),
    Share("Share", Icons.Default.Share),
    Info("Info", Icons.Default.Info),
    AddToPlaylist("Add to playlist", Icons.Default.PlaylistAdd),
    MarkAsWatched("Mark as watched", Icons.Default.Visibility),
    Move("Move", Icons.Default.DriveFileMove)
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
