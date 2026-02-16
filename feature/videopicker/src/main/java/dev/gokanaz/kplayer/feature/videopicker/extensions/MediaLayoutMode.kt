package dev.gokanaz.kplayer.feature.videopicker.extensions

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.model.MediaLayoutMode

fun MediaLayoutMode.toDisplayName(context: Context): String {
    return when (this) {
        MediaLayoutMode.GRID -> "Grid"
        MediaLayoutMode.LIST -> "List"
    }
}

fun MediaLayoutMode.toIcon(): ImageVector {
    return when (this) {
        MediaLayoutMode.GRID -> Icons.Default.ViewModule
        MediaLayoutMode.LIST -> Icons.Default.ViewList
    }
}

fun MediaLayoutMode.isGrid(): Boolean = this == MediaLayoutMode.GRID

fun MediaLayoutMode.isList(): Boolean = this == MediaLayoutMode.LIST

fun MediaLayoutMode.toggle(): MediaLayoutMode {
    return when (this) {
        MediaLayoutMode.GRID -> MediaLayoutMode.LIST
        MediaLayoutMode.LIST -> MediaLayoutMode.GRID
    }
}

fun MediaLayoutMode.getSpanCount(
    orientation: Int,
    screenWidthDp: Int
): Int {
    if (this != MediaLayoutMode.GRID) return 1

    return when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            when {
                screenWidthDp < 360 -> 2
                screenWidthDp < 600 -> 3
                screenWidthDp < 840 -> 4
                else -> 5
            }
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            when {
                screenWidthDp < 600 -> 3
                screenWidthDp < 840 -> 4
                screenWidthDp < 1200 -> 5
                else -> 6
            }
        }
        else -> 2
    }
}

fun MediaLayoutMode.getItemSpacing(): Dp {
    return when (this) {
        MediaLayoutMode.GRID -> 4.dp
        MediaLayoutMode.LIST -> 8.dp
    }
}

fun MediaLayoutMode.getGridAspectRatio(isLandscape: Boolean): Float {
    return when (this) {
        MediaLayoutMode.GRID -> if (isLandscape) 1.2f else 0.9f
        MediaLayoutMode.LIST -> 1f
    }
}

fun MediaLayoutMode.getContentDescription(context: Context): String {
    return when (this) {
        MediaLayoutMode.GRID -> "Grid layout"
        MediaLayoutMode.LIST -> "List layout"
    }
}

fun MediaLayoutMode.toAnalyticsValue(): String {
    return when (this) {
        MediaLayoutMode.GRID -> "grid"
        MediaLayoutMode.LIST -> "list"
    }
}

fun MediaLayoutMode.opposite(): MediaLayoutMode {
    return when (this) {
        MediaLayoutMode.GRID -> MediaLayoutMode.LIST
        MediaLayoutMode.LIST -> MediaLayoutMode.GRID
    }
}

fun MediaLayoutMode.supportsGridFeatures(): Boolean = this == MediaLayoutMode.GRID

fun MediaLayoutMode.defaultSpanCount(): Int {
    return when (this) {
        MediaLayoutMode.GRID -> 3
        MediaLayoutMode.LIST -> 1
    }
}

fun MediaLayoutMode.minItemWidthDp(): Dp {
    return when (this) {
        MediaLayoutMode.GRID -> 120.dp
        MediaLayoutMode.LIST -> 300.dp
    }
}
