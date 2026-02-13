package dev.gokanaz.kplayer.core.data.models

data class VideoState(
    val videoId: String,
    val lastPlayedPositionMs: Long = 0,
    val isFavorite: Boolean = false,
    val watchCount: Int = 0,
    val lastPlayedAt: Long = 0,
    val playlistIds: Set<String> = emptySet(),
    val tags: Set<String> = emptySet(),
    val customMetadata: Map<String, String> = emptyMap()
)
