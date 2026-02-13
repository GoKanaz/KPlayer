package dev.gokanaz.kplayer.core.model

enum class SortType {
    NAME,
    DATE,
    SIZE,
    DURATION,
    TYPE,
    ARTIST,
    ALBUM
}

enum class SortOrder {
    ASCENDING,
    DESCENDING
}

enum class FolderSortType {
    NAME,
    DATE,
    MEDIA_COUNT,
    SIZE
}

enum class PlaylistSortType {
    NAME,
    DATE_CREATED,
    DATE_MODIFIED,
    MEDIA_COUNT
}

data class SortOption(
    val type: SortType = SortType.NAME,
    val order: SortOrder = SortOrder.ASCENDING
)
