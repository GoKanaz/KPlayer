package dev.gokanaz.kplayer.feature.player.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun PlayerButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.onSurface
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PlayerButton(
        onClick = onClick,
        icon = if (isPlaying) {
            androidx.compose.material.icons.Icons.Default.Pause
        } else {
            androidx.compose.material.icons.Icons.Default.PlayArrow
        },
        contentDescription = if (isPlaying) "Pause" else "Play",
        modifier = modifier,
        enabled = enabled
    )
}

@Composable
fun PreviousButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PlayerButton(
        onClick = onClick,
        icon = androidx.compose.material.icons.Icons.Default.SkipPrevious,
        contentDescription = "Previous",
        modifier = modifier,
        enabled = enabled
    )
}

@Composable
fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PlayerButton(
        onClick = onClick,
        icon = androidx.compose.material.icons.Icons.Default.SkipNext,
        contentDescription = "Next",
        modifier = modifier,
        enabled = enabled
    )
}

@Composable
fun RepeatButton(
    repeatMode: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val (icon, description) = when (repeatMode) {
        0 -> androidx.compose.material.icons.Icons.Default.Repeat to "Repeat off"
        1 -> androidx.compose.material.icons.Icons.Default.RepeatOne to "Repeat one"
        else -> androidx.compose.material.icons.Icons.Default.Repeat to "Repeat all"
    }
    
    PlayerButton(
        onClick = onClick,
        icon = icon,
        contentDescription = description,
        modifier = modifier,
        enabled = enabled,
        tint = if (repeatMode > 0) AppTheme.colorScheme.primary else AppTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ShuffleButton(
    isShuffleOn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    PlayerButton(
        onClick = onClick,
        icon = androidx.compose.material.icons.Icons.Default.Shuffle,
        contentDescription = if (isShuffleOn) "Shuffle on" else "Shuffle off",
        modifier = modifier,
        enabled = enabled,
        tint = if (isShuffleOn) AppTheme.colorScheme.primary else AppTheme.colorScheme.onSurfaceVariant
    )
}
