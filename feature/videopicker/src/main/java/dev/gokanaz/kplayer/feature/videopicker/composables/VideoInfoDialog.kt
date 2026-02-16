package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoInfoDialog(
    videoInfo: VideoInfo,
    onDismiss: () -> Unit,
    onPlay: () -> Unit = {},
    onShare: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRename: () -> Unit = {},
    onAddToPlaylist: () -> Unit = {},
    onShowInFolder: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val clipboardManager = LocalClipboardManager.current
    var expandedSections by remember { mutableStateOf(setOf("basic", "technical")) }

    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(videoInfo.thumbnail)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = videoInfo.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = videoInfo.filename,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionButton(
                        icon = Icons.Default.PlayArrow,
                        text = "Play",
                        onClick = onPlay
                    )

                    ActionButton(
                        icon = Icons.Default.Share,
                        text = "Share",
                        onClick = onShare
                    )

                    ActionButton(
                        icon = Icons.Default.Delete,
                        text = "Delete",
                        onClick = onDelete
                    )

                    ActionButton(
                        icon = Icons.Default.Edit,
                        text = "Rename",
                        onClick = onRename
                    )

                    ActionButton(
                        icon = Icons.Default.PlaylistAdd,
                        text = "Playlist",
                        onClick = onAddToPlaylist
                    )
                }

                Divider()

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ExpandableSection(
                        title = "Basic Information",
                        icon = Icons.Default.Info,
                        isExpanded = "basic" in expandedSections,
                        onToggle = {
                            expandedSections = if ("basic" in expandedSections) {
                                expandedSections - "basic"
                            } else {
                                expandedSections + "basic"
                            }
                        }
                    ) {
                        InfoRow(
                            label = "Duration",
                            value = formatDuration(videoInfo.duration),
                            icon = Icons.Default.AccessTime
                        ) {
                            clipboardManager.setText(AnnotatedString(formatDuration(videoInfo.duration)))
                        }

                        InfoRow(
                            label = "File size",
                            value = formatFileSize(videoInfo.fileSize),
                            icon = Icons.Default.DataUsage
                        ) {
                            clipboardManager.setText(AnnotatedString(videoInfo.fileSize.toString()))
                        }

                        InfoRow(
                            label = "Path",
                            value = videoInfo.path,
                            icon = Icons.Default.Folder
                        ) {
                            clipboardManager.setText(AnnotatedString(videoInfo.path))
                        }

                        InfoRow(
                            label = "Added",
                            value = formatDate(videoInfo.dateAdded),
                            icon = Icons.Default.DateRange
                        )

                        InfoRow(
                            label = "Modified",
                            value = formatDate(videoInfo.dateModified),
                            icon = Icons.Default.Update
                        )
                    }

                    ExpandableSection(
                        title = "Technical Details",
                        icon = Icons.Default.Settings,
                        isExpanded = "technical" in expandedSections,
                        onToggle = {
                            expandedSections = if ("technical" in expandedSections) {
                                expandedSections - "technical"
                            } else {
                                expandedSections + "technical"
                            }
                        }
                    ) {
                        InfoRow(
                            label = "Resolution",
                            value = "${videoInfo.width}x${videoInfo.height}",
                            icon = Icons.Default.HighQuality
                        )

                        InfoRow(
                            label = "Frame rate",
                            value = "${videoInfo.frameRate} fps",
                            icon = Icons.Default.SlowMotionVideo
                        )

                        InfoRow(
                            label = "Bitrate",
                            value = formatBitrate(videoInfo.bitrate),
                            icon = Icons.Default.Speed
                        )

                        InfoRow(
                            label = "Video codec",
                            value = videoInfo.videoCodec,
                            icon = Icons.Default.Code
                        )

                        InfoRow(
                            label = "Audio codec",
                            value = videoInfo.audioCodec,
                            icon = Icons.Default.Audiotrack
                        )

                        InfoRow(
                            label = "Audio channels",
                            value = formatAudioChannels(videoInfo.audioChannels),
                            icon = Icons.Default.SurroundSound
                        )
                    }

                    if (videoInfo.subtitles.isNotEmpty()) {
                        ExpandableSection(
                            title = "Subtitles (${videoInfo.subtitles.size})",
                            icon = Icons.Default.Subtitles,
                            isExpanded = "subtitles" in expandedSections,
                            onToggle = {
                                expandedSections = if ("subtitles" in expandedSections) {
                                    expandedSections - "subtitles"
                                } else {
                                    expandedSections + "subtitles"
                                }
                            }
                        ) {
                            videoInfo.subtitles.forEach { subtitle ->
                                InfoRow(
                                    label = subtitle.language,
                                    value = subtitle.type,
                                    icon = Icons.Default.TextFields
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = onShowInFolder,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Show in folder")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
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
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun ExpandableSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                modifier = Modifier.size(20.dp)
            )
        }

        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 28.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onCopy: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(100.dp)
        )

        Text(
            text = ":",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        if (onCopy != null) {
            IconButton(
                onClick = onCopy,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

data class VideoInfo(
    val id: String,
    val title: String,
    val filename: String,
    val path: String,
    val thumbnail: Any?,
    val duration: Long,
    val fileSize: Long,
    val width: Int,
    val height: Int,
    val frameRate: Double,
    val bitrate: Long,
    val videoCodec: String,
    val audioCodec: String,
    val audioChannels: Int,
    val dateAdded: Long,
    val dateModified: Long,
    val subtitles: List<SubtitleInfo> = emptyList()
)

data class SubtitleInfo(
    val language: String,
    val type: String,
    val path: String
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
        gb >= 1 -> String.format("%.2f GB", gb)
        mb >= 1 -> String.format("%.2f MB", mb)
        kb >= 1 -> String.format("%.2f KB", kb)
        else -> "$bytes B"
    }
}

private fun formatBitrate(bitrate: Long): String {
    return if (bitrate >= 1_000_000) {
        String.format("%.2f Mbps", bitrate / 1_000_000.0)
    } else if (bitrate >= 1_000) {
        String.format("%.2f Kbps", bitrate / 1_000.0)
    } else {
        "$bitrate bps"
    }
}

private fun formatAudioChannels(channels: Int): String {
    return when (channels) {
        1 -> "Mono"
        2 -> "Stereo"
        6 -> "5.1 Surround"
        8 -> "7.1 Surround"
        else -> "$channels channels"
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
