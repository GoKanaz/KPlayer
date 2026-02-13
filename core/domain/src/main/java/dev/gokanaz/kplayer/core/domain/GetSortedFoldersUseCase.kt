package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.service.MediaService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class MediaFolder(
    val id: String,
    val name: String,
    val videoCount: Int,
    val totalDuration: Long,
    val totalSize: Long,
    val latestVideoDate: Long,
    val thumbnailUri: String? = null
)

sealed class FolderSortType {
    object Name : FolderSortType()
    object VideoCount : FolderSortType()
    object TotalDuration : FolderSortType()
    object LatestDate : FolderSortType()
}

class GetSortedFoldersUseCase @Inject constructor(
    private val mediaService: MediaService
) {
    suspend operator fun invoke(
        sortType: FolderSortType = FolderSortType.Name,
        order: SortOrder = SortOrder.Ascending
    ): Result<List<MediaFolder>> {
        return try {
            val videos = mediaService.getVideos()
                .map { it }
                .first()
            
            val folderMap = videos.groupBy { it.bucketId }
            
            val folders = folderMap.map { (bucketId, videoList) ->
                MediaFolder(
                    id = bucketId,
                    name = videoList.firstOrNull()?.bucketDisplayName ?: "Unknown",
                    videoCount = videoList.size,
                    totalDuration = videoList.sumOf { it.duration },
                    totalSize = videoList.sumOf { it.size },
                    latestVideoDate = videoList.maxOfOrNull { it.dateAdded } ?: 0,
                    thumbnailUri = videoList.firstOrNull()?.uri?.toString()
                )
            }
            
            val sorted = when (sortType) {
                FolderSortType.Name -> folders.sortedBy { it.name }
                FolderSortType.VideoCount -> folders.sortedBy { it.videoCount }
                FolderSortType.TotalDuration -> folders.sortedBy { it.totalDuration }
                FolderSortType.LatestDate -> folders.sortedBy { it.latestVideoDate }
            }
            
            val result = when (order) {
                SortOrder.Ascending -> sorted
                SortOrder.Descending -> sorted.reversed()
            }
            
            Result.Success(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
