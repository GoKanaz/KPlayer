package dev.gokanaz.kplayer.core.domain.model

import java.util.Date

enum class SortOption {
    NAME_ASC,
    NAME_DESC,
    DATE_ASC,
    DATE_DESC,
    SIZE_ASC,
    SIZE_DESC,
    DURATION_ASC,
    DURATION_DESC,
    ARTIST_ASC,
    ARTIST_DESC,
    COUNT_ASC,
    COUNT_DESC
}

data class MediaItem(
    val id: String,
    val title: String,
    val artist: String? = null,
    val album: String? = null,
    val duration: Long = 0,
    val size: Long = 0,
    val path: String,
    val uri: String,
    val mimeType: String,
    val dateAdded: Date,
    val dateModified: Date,
    val lastPlayedPosition: Long = 0,
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val thumbnail: String? = null
)

data class Folder(
    val id: String,
    val name: String,
    val path: String,
    val parentId: String? = null,
    val mediaCount: Int = 0,
    val size: Long = 0,
    val dateModified: Date = Date(),
    val isHidden: Boolean = false
)

data class Playlist(
    val id: String,
    val name: String,
    val description: String? = null,
    val mediaIds: List<String> = emptyList(),
    val mediaCount: Int = 0,
    val dateCreated: Date = Date(),
    val dateModified: Date = Date(),
    val artwork: String? = null
)

data class FolderTree(
    val rootId: String? = null,
    val folders: List<FolderNode>,
    val mediaItems: List<MediaNode>
) {
    data class FolderNode(
        val folder: Folder,
        val children: List<FolderNode>
    )
    
    data class MediaNode(
        val media: MediaItem
    )
}
