package dev.gokanaz.kplayer.core.data.mappers

import android.net.Uri
import dev.gokanaz.kplayer.core.domain.Folder
import dev.gokanaz.kplayer.core.domain.FolderNode
import dev.gokanaz.kplayer.core.domain.FolderSortType
import dev.gokanaz.kplayer.core.domain.Video

fun List<Video>.toFolders(sortType: FolderSortType = FolderSortType.Name): List<Folder> {
    val folderMap = this.groupBy { it.bucketId }
    
    return folderMap.map { (bucketId, videos) ->
        Folder(
            id = bucketId,
            name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
            path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
            videoCount = videos.size,
            totalDuration = videos.sumOf { it.duration },
            totalSize = videos.sumOf { it.size },
            thumbnailUri = videos.firstOrNull()?.uri,
            videos = videos,
            subFolders = emptyList()
        )
    }.sortedBy { it.name }
}

fun Map<String, List<Video>>.toFolderTree(): FolderNode {
    val root = FolderNode(
        id = "root",
        name = "Root",
        path = "/",
        children = this.map { (bucketId, videos) ->
            FolderNode(
                id = bucketId,
                name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
                path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
                children = emptyList(),
                videoCount = videos.size,
                isExpanded = false
            )
        },
        videoCount = this.values.sumOf { it.size },
        isExpanded = true
    )
    return root
}
