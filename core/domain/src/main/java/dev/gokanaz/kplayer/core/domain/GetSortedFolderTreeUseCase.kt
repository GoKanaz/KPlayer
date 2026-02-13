package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.media.service.MediaService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class FolderNode(
    val id: String,
    val name: String,
    val path: String,
    val children: List<FolderNode> = emptyList(),
    val videoCount: Int = 0,
    val isExpanded: Boolean = false
)

class GetSortedFolderTreeUseCase @Inject constructor(
    private val mediaService: MediaService
) {
    suspend operator fun invoke(): Result<FolderNode> {
        return try {
            val videos = mediaService.getVideos()
                .map { it }
                .first()
            
            val folderMap = videos.groupBy { it.bucketId }
            
            val root = FolderNode(
                id = "root",
                name = "Root",
                path = "/",
                children = folderMap.map { (bucketId, videoList) ->
                    FolderNode(
                        id = bucketId,
                        name = videoList.firstOrNull()?.bucketDisplayName ?: "Unknown",
                        path = "/${videoList.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
                        children = emptyList(),
                        videoCount = videoList.size
                    )
                }
            )
            
            Result.Success(root)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
