package dev.gokanaz.kplayer.feature.videopicker.screens

import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.Video

/**
 * Sealed class representing the state of media in the UI
 */
sealed class MediaState {
    object Loading : MediaState()
    data class Success(val data: List<MediaItem>) : MediaState()
    data class Error(val message: String, val retryAction: (() -> Unit)? = null) : MediaState()
    object Empty : MediaState()
    
    /**
     * Check if state is empty
     */
    fun isEmpty(): Boolean {
        return when (this) {
            is Empty -> true
            is Success -> data.isEmpty()
            else -> false
        }
    }
    
    /**
     * Get items if state is Success, empty list otherwise
     */
    fun getItems(): List<MediaItem> {
        return when (this) {
            is Success -> data
            else -> emptyList()
        }
    }
    
    /**
     * Filter items based on predicate
     */
    fun filter(predicate: (MediaItem) -> Boolean): MediaState {
        return when (this) {
            is Success -> Success(data.filter(predicate))
            is Error -> this
            isLoading() -> this
            isEmpty() -> this
        }
    }
    
    /**
     * Check if state is loading
     */
    fun isLoading(): Boolean = this is Loading
    
    /**
     * Check if state is error
     */
    fun isError(): Boolean = this is Error
    
    /**
     * Check if state is success
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Get error message if state is error
     */
    fun getErrorMessage(): String? {
        return when (this) {
            is Error -> message
            else -> null
        }
    }
}

/**
 * Sealed class representing different types of media items
 */
sealed class MediaItem {
    abstract val id: String
    abstract val title: String
    abstract val thumbnail: Any?
    
    data class VideoItem(
        val video: Video
    ) : MediaItem() {
        override val id: String = video.id
        override val title: String = video.title
        override val thumbnail: Any? = video.thumbnail
    }
    
    data class FolderItem(
        val folder: Folder
    ) : MediaItem() {
        override val id: String = folder.id
        override val title: String = folder.name
        override val thumbnail: Any? = folder.thumbnails.firstOrNull()
    }
}

/**
 * Extension functions for MediaItem
 */
fun MediaItem.isVideo(): Boolean = this is MediaItem.VideoItem

fun MediaItem.isFolder(): Boolean = this is MediaItem.FolderItem

fun MediaItem.asVideoOrNull(): Video? {
    return (this as? MediaItem.VideoItem)?.video
}

fun MediaItem.asFolderOrNull(): Folder? {
    return (this as? MediaItem.FolderItem)?.folder
}

/**
 * Extension functions for MediaState
 */
fun MediaState.getVideoItems(): List<MediaItem.VideoItem> {
    return getItems().filterIsInstance<MediaItem.VideoItem>()
}

fun MediaState.getFolderItems(): List<MediaItem.FolderItem> {
    return getItems().filterIsInstance<MediaItem.FolderItem>()
}

fun MediaState.map(transform: (MediaItem) -> MediaItem): MediaState {
    return when (this) {
        is Success -> Success(data.map(transform))
        is Error -> this
        isLoading() -> this
        isEmpty() -> this
    }
}

fun MediaState.onSuccess(block: (List<MediaItem>) -> Unit): MediaState {
    if (this is Success) {
        block(data)
    }
    return this
}

fun MediaState.onError(block: (String, (() -> Unit)?) -> Unit): MediaState {
    if (this is Error) {
        block(message, retryAction)
    }
    return this
}

fun MediaState.onLoading(block: () -> Unit): MediaState {
    if (this is Loading) {
        block()
    }
    return this
}

fun MediaState.onEmpty(block: () -> Unit): MediaState {
    if (this is Empty || (this is Success && data.isEmpty())) {
        block()
    }
    return this
}

/**
 * Create a success state from a list of videos
 */
fun MediaState.Companion.successFromVideos(videos: List<Video>): MediaState {
    return Success(videos.map { MediaItem.VideoItem(it) })
}

/**
 * Create a success state from a list of folders
 */
fun MediaState.Companion.successFromFolders(folders: List<Folder>): MediaState {
    return Success(folders.map { MediaItem.FolderItem(it) })
}

/**
 * Create a success state from mixed items
 */
fun MediaState.Companion.successFromItems(items: List<MediaItem>): MediaState {
    return Success(items)
}

/**
 * Create an error state
 */
fun MediaState.Companion.error(
    message: String,
    retryAction: (() -> Unit)? = null
): MediaState {
    return Error(message, retryAction)
}

/**
 * Create a loading state
 */
fun MediaState.Companion.loading(): MediaState = Loading

/**
 * Create an empty state
 */
fun MediaState.Companion.empty(): MediaState = Empty
