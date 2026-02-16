package dev.gokanaz.kplayer.feature.videopicker.screens

import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.Video

sealed class MediaState {
    object Loading : MediaState()
    data class Success(val data: List<MediaItem>) : MediaState()
    data class Error(val message: String, val retryAction: (() -> Unit)? = null) : MediaState()
    object Empty : MediaState()

    companion object

    fun isEmpty(): Boolean {
        return when (this) {
            is Empty -> true
            is Success -> data.isEmpty()
            else -> false
        }
    }

    fun getItems(): List<MediaItem> {
        return when (this) {
            is Success -> data
            else -> emptyList()
        }
    }

    fun filter(predicate: (MediaItem) -> Boolean): MediaState {
        return when (this) {
            is Success -> Success(data.filter(predicate))
            is Error -> this
            is Loading -> this
            is Empty -> this
        }
    }

    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    fun getErrorMessage(): String? {
        return when (this) {
            is Error -> message
            else -> null
        }
    }
}

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
        override val thumbnail: Any? = folder.thumbnailUris.firstOrNull()
    }
}

fun MediaItem.isVideo(): Boolean = this is MediaItem.VideoItem
fun MediaItem.isFolder(): Boolean = this is MediaItem.FolderItem
fun MediaItem.asVideoOrNull(): Video? = (this as? MediaItem.VideoItem)?.video
fun MediaItem.asFolderOrNull(): Folder? = (this as? MediaItem.FolderItem)?.folder

fun MediaState.getVideoItems(): List<MediaItem.VideoItem> {
    return getItems().filterIsInstance<MediaItem.VideoItem>()
}

fun MediaState.getFolderItems(): List<MediaItem.FolderItem> {
    return getItems().filterIsInstance<MediaItem.FolderItem>()
}

fun MediaState.map(transform: (MediaItem) -> MediaItem): MediaState {
    return when (this) {
        is MediaState.Success -> MediaState.Success(data.map(transform))
        is MediaState.Error -> this
        is MediaState.Loading -> this
        is MediaState.Empty -> this
    }
}

fun MediaState.onSuccess(block: (List<MediaItem>) -> Unit): MediaState {
    if (this is MediaState.Success) {
        block(data)
    }
    return this
}

fun MediaState.onError(block: (String, (() -> Unit)?) -> Unit): MediaState {
    if (this is MediaState.Error) {
        block(message, retryAction)
    }
    return this
}

fun MediaState.onLoading(block: () -> Unit): MediaState {
    if (this is MediaState.Loading) {
        block()
    }
    return this
}

fun MediaState.onEmpty(block: () -> Unit): MediaState {
    if (this is MediaState.Empty || (this is MediaState.Success && data.isEmpty())) {
        block()
    }
    return this
}

fun MediaState.Companion.successFromVideos(videos: List<Video>): MediaState {
    return MediaState.Success(videos.map { MediaItem.VideoItem(it) })
}

fun MediaState.Companion.successFromFolders(folders: List<Folder>): MediaState {
    return MediaState.Success(folders.map { MediaItem.FolderItem(it) })
}

fun MediaState.Companion.successFromItems(items: List<MediaItem>): MediaState {
    return MediaState.Success(items)
}

fun MediaState.Companion.error(
    message: String,
    retryAction: (() -> Unit)? = null
): MediaState {
    return MediaState.Error(message, retryAction)
}

fun MediaState.Companion.loading(): MediaState = MediaState.Loading
fun MediaState.Companion.empty(): MediaState = MediaState.Empty
