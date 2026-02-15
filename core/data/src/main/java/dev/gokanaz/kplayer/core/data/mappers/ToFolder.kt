package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.FolderNode
import dev.gokanaz.kplayer.core.model.Video

fun List<Video>.toFolders(): List<Folder> {
    val folderMap = this.groupBy { it.bucketId }

    return folderMap.map { (bucketId, videos) ->
        Folder(
            id = bucketId,
            name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
            path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
            bucketId = bucketId,
            mediaCount = videos.size,
            totalDuration = videos.sumOf { it.duration },
            totalSize = videos.sumOf { it.size }
        )
    }.sortedBy { it.name }
}

fun Map<String, List<Video>>.toFolderTree(): FolderNode {
    val rootFolder = Folder(
        id = "root",
        name = "Root",
        path = "/",
        bucketId = "root"
    )

    val children = this.map { (bucketId, videos) ->
        val folder = Folder(
            id = bucketId,
            name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
            path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
            bucketId = bucketId,
            mediaCount = videos.size,
            totalDuration = videos.sumOf { it.duration },
            totalSize = videos.sumOf { it.size }
        )
        FolderNode(
            folder = folder,
            videos = videos
        )
    }

    return FolderNode(
        folder = rootFolder,
        subFolders = children,
        videos = this.values.flatten()
    )
}
