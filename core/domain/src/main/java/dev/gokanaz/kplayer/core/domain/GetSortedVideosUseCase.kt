package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.media.services.LocalMediaService
import dev.gokanaz.kplayer.core.domain.model.MediaItem
import dev.gokanaz.kplayer.core.domain.model.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSortedVideosUseCase @Inject constructor(
    private val localMediaService: LocalMediaService
) {
    operator fun invoke(
        sortOption: SortOption = SortOption.NAME_ASC
    ): Flow<List<MediaItem>> {
        return flowOf(localMediaService.allVideos.value).map { videos ->
            val mediaItems = videos.map { video ->
                MediaItem(
                    id = video.id,
                    title = video.title,
                    artist = video.artist,
                    album = video.album,
                    duration = video.duration,
                    size = video.size,
                    path = video.path,
                    uri = video.uri.toString(),
                    mimeType = video.mimeType,
                    dateAdded = video.dateAdded,
                    dateModified = video.dateModified,
                    lastPlayedPosition = video.lastPlayedPosition,
                    playCount = video.playCount,
                    isFavorite = video.isFavorite,
                    thumbnail = video.thumbnail
                )
            }
            
            when (sortOption) {
                SortOption.NAME_ASC -> mediaItems.sortedBy { it.title.lowercase() }
                SortOption.NAME_DESC -> mediaItems.sortedByDescending { it.title.lowercase() }
                SortOption.DATE_ASC -> mediaItems.sortedBy { it.dateAdded }
                SortOption.DATE_DESC -> mediaItems.sortedByDescending { it.dateAdded }
                SortOption.SIZE_ASC -> mediaItems.sortedBy { it.size }
                SortOption.SIZE_DESC -> mediaItems.sortedByDescending { it.size }
                SortOption.DURATION_ASC -> mediaItems.sortedBy { it.duration }
                SortOption.DURATION_DESC -> mediaItems.sortedByDescending { it.duration }
                SortOption.ARTIST_ASC -> mediaItems.sortedBy { it.artist ?: "" }
                SortOption.ARTIST_DESC -> mediaItems.sortedByDescending { it.artist ?: "" }
            }
        }
    }
    
    fun searchVideos(query: String): Flow<List<MediaItem>> {
        return flowOf(localMediaService.searchVideos(query)).map { videos ->
            videos.map { video ->
                MediaItem(
                    id = video.id,
                    title = video.title,
                    artist = video.artist,
                    album = video.album,
                    duration = video.duration,
                    size = video.size,
                    path = video.path,
                    uri = video.uri.toString(),
                    mimeType = video.mimeType,
                    dateAdded = video.dateAdded,
                    dateModified = video.dateModified,
                    lastPlayedPosition = video.lastPlayedPosition,
                    playCount = video.playCount,
                    isFavorite = video.isFavorite,
                    thumbnail = video.thumbnail
                )
            }
        }
    }
    
    fun getVideoById(mediaId: String): MediaItem? {
        val video = localMediaService.getVideoById(mediaId) ?: return null
        return MediaItem(
            id = video.id,
            title = video.title,
            artist = video.artist,
            album = video.album,
            duration = video.duration,
            size = video.size,
            path = video.path,
            uri = video.uri.toString(),
            mimeType = video.mimeType,
            dateAdded = video.dateAdded,
            dateModified = video.dateModified,
            lastPlayedPosition = video.lastPlayedPosition,
            playCount = video.playCount,
            isFavorite = video.isFavorite,
            thumbnail = video.thumbnail
        )
    }
}
