package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoChip(
    text: String,
    icon: ImageVector? = null,
    type: ChipType = ChipType.Default,
    size: ChipSize = ChipSize.Medium,
    isSelected: Boolean = false,
    isClickable: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val chipColors = type.getColors(
        isSelected = isSelected,
        isClickable = isClickable
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = chipColors.backgroundColor,
        label = "bg_color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = chipColors.contentColor,
        label = "content_color"
    )
    
    val height by animateDpAsState(
        targetValue = size.height,
        label = "height"
    )
    
    val horizontalPadding by animateDpAsState(
        targetValue = size.horizontalPadding,
        label = "padding"
    )
    
    val iconSize by animateDpAsState(
        targetValue = size.iconSize,
        label = "icon_size"
    )
    
    val interactionSource = remember { MutableInteractionSource() }
    
    Surface(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(size.cornerRadius))
            .then(
                if (isClickable) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            ),
        color = backgroundColor,
        tonalElevation = 0.dp,
        shadowElevation = if (isClickable) size.shadowElevation else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = horizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(size.spacing)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(iconSize)
                )
            }
            
            Text(
                text = text,
                color = contentColor,
                fontSize = size.fontSize,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
            )
            
            if (isSelected && isClickable) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = contentColor,
                    modifier = Modifier.size(iconSize)
                )
            }
        }
    }
}

enum class ChipType {
    Default,
    Duration,
    Resolution,
    FileSize,
    Codec,
    Audio,
    Favorite;
    
    fun getColors(isSelected: Boolean, isClickable: Boolean): ChipColors {
        return when (this) {
            Default -> ChipColors(
                backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Duration -> ChipColors(
                backgroundColor = if (isSelected) Color(0xFF4A90E2)
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Resolution -> ChipColors(
                backgroundColor = if (isSelected) Color(0xFF50C878)
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            FileSize -> ChipColors(
                backgroundColor = if (isSelected) Color(0xFFFF6B6B)
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Codec -> ChipColors(
                backgroundColor = if (isSelected) Color(0xFF9B59B6)
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Audio -> ChipColors(
                backgroundColor = if (isSelected) Color(0xFFFFA500)
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Favorite -> ChipColors(
                backgroundColor = if (isSelected) Color(0xFFE91E63)
                else MaterialTheme.colorScheme.secondaryContainer,
                contentColor = if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

enum class ChipSize(
    val height: Dp,
    val horizontalPadding: Dp,
    val iconSize: Dp,
    val fontSize: TextUnit,
    val spacing: Dp,
    val cornerRadius: Dp,
    val shadowElevation: Dp
) {
    Small(
        height = 24.dp,
        horizontalPadding = 8.dp,
        iconSize = 14.dp,
        fontSize = 11.sp,
        spacing = 4.dp,
        cornerRadius = 12.dp,
        shadowElevation = 2.dp
    ),
    
    Medium(
        height = 32.dp,
        horizontalPadding = 12.dp,
        iconSize = 16.dp,
        fontSize = 13.sp,
        spacing = 6.dp,
        cornerRadius = 16.dp,
        shadowElevation = 4.dp
    ),
    
    Large(
        height = 40.dp,
        horizontalPadding = 16.dp,
        iconSize = 20.dp,
        fontSize = 14.sp,
        spacing = 8.dp,
        cornerRadius = 20.dp,
        shadowElevation = 6.dp
    )
}

data class ChipColors(
    val backgroundColor: Color,
    val contentColor: Color
)

@Composable
fun DurationChip(
    seconds: Long,
    size: ChipSize = ChipSize.Medium,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    InfoChip(
        text = formatDuration(seconds),
        icon = Icons.Default.AccessTime,
        type = ChipType.Duration,
        size = size,
        isClickable = isClickable,
        onClick = onClick
    )
}

@Composable
fun ResolutionChip(
    width: Int,
    height: Int,
    size: ChipSize = ChipSize.Medium,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    val resolution = when {
        width >= 3840 || height >= 2160 -> "4K"
        width >= 1920 || height >= 1080 -> "1080p"
        width >= 1280 || height >= 720 -> "720p"
        width >= 854 || height >= 480 -> "480p"
        else -> "${height}p"
    }
    
    InfoChip(
        text = resolution,
        icon = Icons.Default.HighQuality,
        type = ChipType.Resolution,
        size = size,
        isClickable = isClickable,
        onClick = onClick
    )
}

@Composable
fun FileSizeChip(
    bytes: Long,
    size: ChipSize = ChipSize.Medium,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    InfoChip(
        text = formatFileSize(bytes),
        icon = Icons.Default.DataUsage,
        type = ChipType.FileSize,
        size = size,
        isClickable = isClickable,
        onClick = onClick
    )
}

@Composable
fun CodecChip(
    codec: String,
    size: ChipSize = ChipSize.Small,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    InfoChip(
        text = codec.uppercase(),
        icon = Icons.Default.Code,
        type = ChipType.Codec,
        size = size,
        isClickable = isClickable,
        onClick = onClick
    )
}

@Composable
fun AudioChip(
    channels: Int,
    size: ChipSize = ChipSize.Small,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    val text = when (channels) {
        1 -> "Mono"
        2 -> "Stereo"
        6 -> "5.1"
        8 -> "7.1"
        else -> "$channels ch"
    }
    
    InfoChip(
        text = text,
        icon = Icons.Default.Audiotrack,
        type = ChipType.Audio,
        size = size,
        isClickable = isClickable,
        onClick = onClick
    )
}

@Composable
fun FavoriteChip(
    isFavorite: Boolean,
    size: ChipSize = ChipSize.Medium,
    isClickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    InfoChip(
        text = if (isFavorite) "Favorited" else "Add to favorites",
        icon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        type = ChipType.Favorite,
        size = size,
        isSelected = isFavorite,
        isClickable = isClickable,
        onClick = onClick
    )
}

private fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else if (minutes > 0) {
        String.format("%d:%02d", minutes, secs)
    } else {
        String.format("0:%02d", secs)
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
