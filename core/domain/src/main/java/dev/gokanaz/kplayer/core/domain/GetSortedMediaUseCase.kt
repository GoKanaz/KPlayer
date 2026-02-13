package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import javax.inject.Inject

sealed class MediaType {
    object Video : MediaType()
    object Audio : MediaType()
    object Image : MediaType()
}

interface MediaItem {
    val id: Long
    val title: String
    val uri: String
    val dateAdded: Long
    val mediaType: MediaType
}

data class VideoItem(
    override val id: Long,
    override val title: String,
    override val uri: String,
    override val dateAdded: Long,
    override val mediaType: MediaType = MediaType.Video,
    val duration: Long,
    val size: Long,
    val resolution: String
) : MediaItem

class GetSortedMediaUseCase @Inject constructor() {
    fun fromVideos(
        videos: List<MediaVideo>,
        sortType: SortType = SortType.Date,
        order: SortOrder = SortOrder.Descending
    ): List<VideoItem> {
        val items = videos.map { video ->
            VideoItem(
                id = video.id,
                title = video.title,
                uri = video.uri.toString(),
                dateAdded = video.dateAdded,
                duration = video.duration,
                size = video.size,
                resolution = video.resolution
            )
        }
        
        val sorted = when (sortType) {
            SortType.Name -> items.sortedBy { it.title }
            SortType.Date -> items.sortedBy { it.dateAdded }
            SortType.Size -> items.sortedBy { it.size }
            SortType.Duration -> items.sortedBy { it.duration }
        }
        
        return when (order) {
            SortOrder.Ascending -> sorted
            SortOrder.Descending -> sorted.reversed()
        }
    }
}
