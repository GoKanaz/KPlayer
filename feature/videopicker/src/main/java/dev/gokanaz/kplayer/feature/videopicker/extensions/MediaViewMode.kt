package dev.gokanaz.kplayer.feature.videopicker.extensions

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.MediaViewMode

fun MediaViewMode.toDisplayName(context: Context): String {
    return when (this) {
        MediaViewMode.FOLDERS -> "Folders"
        MediaViewMode.VIDEOS -> "Videos"
        MediaViewMode.RECENT -> "Recent"
        MediaViewMode.FAVORITES -> "Favorites"
    }
}

fun MediaViewMode.toIcon(): ImageVector {
    return when (this) {
        MediaViewMode.FOLDERS -> Icons.Default.Folder
        MediaViewMode.VIDEOS -> Icons.Default.VideoLibrary
        MediaViewMode.RECENT -> Icons.Default.History
        MediaViewMode.FAVORITES -> Icons.Default.Favorite
    }
}

fun MediaViewMode.isFolderMode(): Boolean = this == MediaViewMode.FOLDERS

fun MediaViewMode.isVideoMode(): Boolean = this == MediaViewMode.VIDEOS

fun MediaViewMode.isRecentMode(): Boolean = this == MediaViewMode.RECENT

fun MediaViewMode.isFavoritesMode(): Boolean = this == MediaViewMode.FAVORITES

fun MediaViewMode.toggle(): MediaViewMode {
    return when (this) {
        MediaViewMode.FOLDERS -> MediaViewMode.VIDEOS
        MediaViewMode.VIDEOS -> MediaViewMode.FOLDERS
        MediaViewMode.RECENT -> MediaViewMode.RECENT
        MediaViewMode.FAVORITES -> MediaViewMode.FAVORITES
    }
}

fun MediaViewMode.next(): MediaViewMode {
    val values = MediaViewMode.entries.toTypedArray()
    val currentIndex = values.indexOf(this)
    return values[(currentIndex + 1) % values.size]
}

fun MediaViewMode.previous(): MediaViewMode {
    val values = MediaViewMode.entries.toTypedArray()
    val currentIndex = values.indexOf(this)
    return values[(currentIndex - 1 + values.size) % values.size]
}

fun MediaViewMode.supportsFolderNavigation(): Boolean {
    return this == MediaViewMode.FOLDERS || this == MediaViewMode.VIDEOS
}

fun MediaViewMode.defaultLayoutMode(): dev.gokanaz.kplayer.core.model.MediaLayoutMode {
    return when (this) {
        MediaViewMode.FOLDERS -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.GRID
        MediaViewMode.VIDEOS -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.LIST
        MediaViewMode.RECENT -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.LIST
        MediaViewMode.FAVORITES -> dev.gokanaz.kplayer.core.model.MediaLayoutMode.GRID
    }
}

fun MediaViewMode.getContentDescription(context: Context): String {
    return when (this) {
        MediaViewMode.FOLDERS -> "Folders view"
        MediaViewMode.VIDEOS -> "Videos view"
        MediaViewMode.RECENT -> "Recent view"
        MediaViewMode.FAVORITES -> "Favorites view"
    }
}

fun MediaViewMode.toAnalyticsValue(): String {
    return when (this) {
        MediaViewMode.FOLDERS -> "folders"
        MediaViewMode.VIDEOS -> "videos"
        MediaViewMode.RECENT -> "recent"
        MediaViewMode.FAVORITES -> "favorites"
    }
}

fun MediaViewMode.supportsSearch(): Boolean {
    return this != MediaViewMode.FOLDERS
}

fun MediaViewMode.supportsSorting(): Boolean {
    return this != MediaViewMode.FOLDERS
}

fun MediaViewMode.supportsFiltering(): Boolean {
    return this == MediaViewMode.VIDEOS
}

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

fun MediaViewMode.getTitleResId(): String {
    return when (this) {
        MediaViewMode.FOLDERS -> "Folders"
        MediaViewMode.VIDEOS -> "Videos"
        MediaViewMode.RECENT -> "Recent"
        MediaViewMode.FAVORITES -> "Favorites"
    }
}
