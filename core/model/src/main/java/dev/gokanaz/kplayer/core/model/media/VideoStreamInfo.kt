package dev.gokanaz.kplayer.core.model.media

data class VideoStreamInfo(
    val streamIndex: Int = 0,
    val codec: String = "",
    val profile: String = "",
    val level: Int = 0,
    val bitrate: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val frameRate: Double = 0.0,
    val displayAspectRatio: String = "",
    val pixelAspectRatio: String = "",
    val rotation: Int = 0,
    val isDefault: Boolean = false,
    val isForced: Boolean = false
)
