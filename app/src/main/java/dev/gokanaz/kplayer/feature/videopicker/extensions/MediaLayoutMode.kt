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
import dev.gokanaz.kplayer.feature.videopicker.R

/**
 * Extension functions for MediaLayoutMode enum
 */

/**
 * Get display name for the layout mode based on current context
 */
fun MediaLayoutMode.toDisplayName(context: Context): String {
    return when (this) {
        MediaLayoutMode.GRID -> context.getString(R.string.layout_mode_grid)
        MediaLayoutMode.LIST -> context.getString(R.string.layout_mode_list)
    }
}

/**
 * Get icon representing the layout mode
 */
fun MediaLayoutMode.toIcon(): ImageVector {
    return when (this) {
        MediaLayoutMode.GRID -> Icons.Default.ViewModule
        MediaLayoutMode.LIST -> Icons.Default.ViewList
    }
}

/**
 * Check if current mode is grid
 */
fun MediaLayoutMode.isGrid(): Boolean = this == MediaLayoutMode.GRID

/**
 * Check if current mode is list
 */
fun MediaLayoutMode.isList(): Boolean = this == MediaLayoutMode.LIST

/**
 * Toggle between grid and list modes
 */
fun MediaLayoutMode.toggle(): MediaLayoutMode {
    return when (this) {
        MediaLayoutMode.GRID -> MediaLayoutMode.LIST
        MediaLayoutMode.LIST -> MediaLayoutMode.GRID
    }
}

/**
 * Get optimal span count for grid based on screen orientation and width
 */
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

/**
 * Get spacing between items based on layout mode
 */
fun MediaLayoutMode.getItemSpacing(): Dp {
    return when (this) {
        MediaLayoutMode.GRID -> 4.dp
        MediaLayoutMode.LIST -> 8.dp
    }
}

/**
 * Get grid cell aspect ratio based on mode
 */
fun MediaLayoutMode.getGridAspectRatio(isLandscape: Boolean): Float {
    return when (this) {
        MediaLayoutMode.GRID -> if (isLandscape) 1.2f else 0.9f
        MediaLayoutMode.LIST -> 1f // Not used for list
    }
}

/**
 * Get description for accessibility
 */
fun MediaLayoutMode.getContentDescription(context: Context): String {
    return when (this) {
        MediaLayoutMode.GRID -> context.getString(R.string.content_desc_grid_layout)
        MediaLayoutMode.LIST -> context.getString(R.string.content_desc_list_layout)
    }
}

/**
 * Convert to analytics tracking value
 */
fun MediaLayoutMode.toAnalyticsValue(): String {
    return when (this) {
        MediaLayoutMode.GRID -> "grid"
        MediaLayoutMode.LIST -> "list"
    }
}

/**
 * Get opposite layout mode
 */
fun MediaLayoutMode.opposite(): MediaLayoutMode {
    return when (this) {
        MediaLayoutMode.GRID -> MediaLayoutMode.LIST
        MediaLayoutMode.LIST -> MediaLayoutMode.GRID
    }
}

/**
 * Check if mode supports grid features
 */
fun MediaLayoutMode.supportsGridFeatures(): Boolean = this == MediaLayoutMode.GRID

/**
 * Get default span count for this mode
 */
fun MediaLayoutMode.defaultSpanCount(): Int {
    return when (this) {
        MediaLayoutMode.GRID -> 3
        MediaLayoutMode.LIST -> 1
    }
}

/**
 * Get minimum required width for each item in dp
 */
fun MediaLayoutMode.minItemWidthDp(): Dp {
    return when (this) {
        MediaLayoutMode.GRID -> 120.dp
        MediaLayoutMode.LIST -> 300.dp
    }
}
