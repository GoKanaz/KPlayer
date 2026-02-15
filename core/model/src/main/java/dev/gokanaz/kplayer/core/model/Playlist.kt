package dev.gokanaz.kplayer.core.model

data class Playlist(
    val id: String,
    val name: String,
    val description: String = "",
    val videoIds: List<String> = emptyList(),
    val videoCount: Int = 0,
    val totalDuration: Long = 0,
    val thumbnailUri: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
)
