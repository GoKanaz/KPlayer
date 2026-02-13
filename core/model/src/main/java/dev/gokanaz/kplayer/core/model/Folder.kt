package dev.gokanaz.kplayer.core.model

data class Folder(
    val id: String,
    val name: String,
    val path: String,
    val bucketId: String = "",
    val dateAdded: Long = 0,
    val dateModified: Long = 0,
    val mediaCount: Int = 0,
    val totalSize: Long = 0,
    val totalDuration: Long = 0,
    val thumbnailUris: List<String> = emptyList()
)

data class FolderTree(
    val root: FolderNode
)

data class FolderNode(
    val folder: Folder,
    val subFolders: List<FolderNode> = emptyList(),
    val videos: List<Video> = emptyList()
)
