package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.service.MediaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

sealed class SortType {
    object Name : SortType()
    object Date : SortType()
    object Size : SortType()
    object Duration : SortType()
}

sealed class SortOrder {
    object Ascending : SortOrder()
    object Descending : SortOrder()
}

data class VideoFilter(
    val bucketId: String? = null,
    val minDuration: Long? = null,
    val maxDuration: Long? = null,
    val minSize: Long? = null,
    val maxSize: Long? = null,
    val startDate: Long? = null,
    val endDate: Long? = null
)

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class GetSortedVideosUseCase @Inject constructor(
    private val mediaService: MediaService
) {
    operator fun invoke(
        sortType: SortType = SortType.Date,
        order: SortOrder = SortOrder.Descending,
        filter: VideoFilter? = null
    ): Flow<Result<List<MediaVideo>>> {
        return mediaService.getVideos()
            .map { videos ->
                try {
                    val filtered = filterVideos(videos, filter)
                    val sorted = sortVideos(filtered, sortType, order)
                    Result.Success(sorted)
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
    }

    private fun filterVideos(
        videos: List<MediaVideo>,
        filter: VideoFilter?
    ): List<MediaVideo> {
        if (filter == null) return videos
        
        return videos.filter { video ->
            var matches = true
            
            filter.bucketId?.let {
                matches = matches && video.bucketId == it
            }
            
            filter.minDuration?.let {
                matches = matches && video.duration >= it
            }
            
            filter.maxDuration?.let {
                matches = matches && video.duration <= it
            }
            
            filter.minSize?.let {
                matches = matches && video.size >= it
            }
            
            filter.maxSize?.let {
                matches = matches && video.size <= it
            }
            
            filter.startDate?.let {
                matches = matches && video.dateAdded >= it
            }
            
            filter.endDate?.let {
                matches = matches && video.dateAdded <= it
            }
            
            matches
        }
    }

    private fun sortVideos(
        videos: List<MediaVideo>,
        sortType: SortType,
        order: SortOrder
    ): List<MediaVideo> {
        val sorted = when (sortType) {
            SortType.Name -> videos.sortedBy { it.title }
            SortType.Date -> videos.sortedBy { it.dateAdded }
            SortType.Size -> videos.sortedBy { it.size }
            SortType.Duration -> videos.sortedBy { it.duration }
        }
        
        return when (order) {
            SortOrder.Ascending -> sorted
            SortOrder.Descending -> sorted.reversed()
        }
    }
}
