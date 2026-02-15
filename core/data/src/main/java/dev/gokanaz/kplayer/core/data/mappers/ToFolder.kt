package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.FolderNode
import dev.gokanaz.kplayer.core.model.FolderSortType
import dev.gokanaz.kplayer.core.model.Video

fun List<Video>.toFolders(sortType: FolderSortType = FolderSortType.NAME): List<Folder> {
    val folderMap = this.groupBy { it.bucketId }

    val folders = folderMap.map { (bucketId, videos) ->
        Folder(
            id = bucketId,
            name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
            path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
            bucketId = bucketId,
            mediaCount = videos.size,
            totalDuration = videos.sumOf { it.duration },
            totalSize = videos.sumOf { it.size }
        )
    }

    return when (sortType) {
        FolderSortType.NAME -> folders.sortedBy { it.name }
        FolderSortType.DATE -> folders.sortedByDescending { it.id }
        FolderSortType.SIZE -> folders.sortedByDescending { it.totalSize }
        FolderSortType.MEDIA_COUNT -> folders.sortedByDescending { it.mediaCount }
    }
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
