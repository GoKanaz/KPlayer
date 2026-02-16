package dev.gokanaz.kplayer.feature.videopicker.extensions

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.MediaViewMode
import dev.gokanaz.kplayer.feature.videopicker.R

/**
 * Extension functions for MediaViewMode enum
 */

/**
 * Get display name for the view mode based on current context
 */
fun MediaViewMode.toDisplayName(context: Context): String {
    return when (this) {
        MediaViewMode.FOLDERS -> context.getString(R.string.view_mode_folders)
        MediaViewMode.VIDEOS -> context.getString(R.string.view_mode_videos)
        MediaViewMode.RECENT -> context.getString(R.string.view_mode_recent)
        MediaViewMode.FAVORITES -> context.getString(R.string.view_mode_favorites)
    }
}

/**
 * Get icon representing the view mode
 */
fun MediaViewMode.toIcon(): ImageVector {
    return when (this) {
        MediaViewMode.FOLDERS -> Icons.Default.Folder
        MediaViewMode.VIDEOS -> Icons.Default.VideoLibrary
        MediaViewMode.RECENT -> Icons.Default.History
        MediaViewMode.FAVORITES -> Icons.Default.Favorite
    }
}

/**
 * Check if current mode is folders view
 */
fun MediaViewMode.isFolderMode(): Boolean = this == MediaViewMode.FOLDERS

/**
 * Check if current mode is videos view
 */
fun MediaViewMode.isVideoMode(): Boolean = this == MediaViewMode.VIDEOS

/**
 * Check if current mode is recent view
 */
fun MediaViewMode.isRecentMode(): Boolean = this == MediaViewMode.RECENT

/**
 * Check if current mode is favorites view
 */
fun MediaViewMode.isFavoritesMode(): Boolean = this == MediaViewMode.FAVORITES

/**
 * Toggle between folder and video modes
 */
fun MediaViewMode.toggle(): MediaViewMode {
    return when (this) {
        MediaViewMode.FOLDERS -> MediaViewMode.VIDEOS
        MediaViewMode.VIDEOS -> MediaViewMode.FOLDERS
        MediaViewMode.RECENT -> MediaViewMode.RECENT // No toggle for recent
        MediaViewMode.FAVORITES -> MediaViewMode.FAVORITES // No toggle for favorites
    }
}

/**
 * Get next mode in cyclic order
 */
fun MediaViewMode.next(): MediaViewMode {
    val values = MediaViewMode.entries.toTypedArray()
    val currentIndex = values.indexOf(this)
    return values[(currentIndex + 1) % values.size]
}

/**
 * Get previous mode in cyclic order
 */
fun MediaViewMode.previous(): MediaViewMode {
    val values = MediaViewMode.entries.toTypedArray()
    val currentIndex = values.indexOf(this)
    return values[(currentIndex - 1 + values.size) % values.size]
}

/**
 * Check if mode supports folder navigation
 */
fun MediaViewMode.supportsFolderNavigation(): Boolean {
    return this == MediaViewMode.FOLDERS || this == MediaViewMode.VIDEOS
}

/**
 * Get default layout mode for this view mode
 */
fun MediaViewMode.defaultLayoutMode(): dev.gokanaz.kplayer.core.model.MediaLayoutMode {
    return when (this) {
        MediaViewMode.FOLDERS -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.GRID
        MediaViewMode.VIDEOS -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.LIST
        MediaViewMode.RECENT -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.LIST
        MediaViewMode.FAVORITES -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.GRID
    }
}

/**
 * Get description for accessibility
 */
fun MediaViewMode.getContentDescription(context: Context): String {
    return when (this) {
        MediaViewMode.FOLDERS -> context.getString(R.string.content_desc_folders_view)
        MediaViewMode.VIDEOS -> context.getString(R.string.content_desc_videos_view)
        MediaViewMode.RECENT -> context.getString(R.string.content_desc_recent_view)
        MediaViewMode.FAVORITES -> context.getString(R.string.content_desc_favorites_view)
    }
}

/**
 * Convert to analytics tracking value
 */
fun MediaViewMode.toAnalyticsValue(): String {
    return when (this) {
        MediaViewMode.FOLDERS -> "folders"
        MediaViewMode.VIDEOS -> "videos"
        MediaViewMode.RECENT -> "recent"
        MediaViewMode.FAVORITES -> "favorites"
    }
}

/**
 * Check if mode supports search
 */
fun MediaViewMode.supportsSearch(): Boolean {
    return this != MediaViewMode.FOLDERS
}

/**
 * Check if mode supports sorting
 */
fun MediaViewMode.supportsSorting(): Boolean {
    return this != MediaViewMode.FOLDERS
}

/**
 * Check if mode supports filtering
 */
fun MediaViewMode.supportsFiltering(): Boolean {
    return this == MediaViewMode.VIDEOS
}

/**
 * Get available sort types for this mode
 */
fun MediaViewMode.availableSortTypes(): List<dev.gokanaz.kplayer.core.model.SortType> {
    return when (this) {
        MediaViewMode.FOLDERS -> listOf(
            dev.gokanaz.kplayer.core.model.SortType.NAME,
            dev.gokanaz.kplayer.core.model.SortType.DATE,
            dev.gokanaz.kplayer.core.model.SortType.SIZE
        )
        MediaViewMode.VIDEOS -> dev.gokanaz.kplayer.core.model.SortType.entries
        MediaViewMode.RECENT -> listOf(
            dev.gokanaz.kplayer.core.model.SortType.DATE,
            dev.gokanaz.kplayer.core.model.SortType.NAME
        )
        MediaViewMode.FAVORITES -> listOf(
            dev.gokanaz.kplayer.core.model.SortType.NAME,
            dev.gokanaz.kplayer.core.model.SortType.DATE
        )
    }
}

/**
 * Get title resource ID for this mode
 */
fun MediaViewMode.getTitleResId(): Int {
    return when (this) {
        MediaViewMode.FOLDERS -> R.string.title_folders
        MediaViewMode.VIDEOS -> R.string.title_videos
        MediaViewMode.RECENT -> R.string.title_recent
        MediaViewMode.FAVORITES -> R.string.title_favorites
    }
}
