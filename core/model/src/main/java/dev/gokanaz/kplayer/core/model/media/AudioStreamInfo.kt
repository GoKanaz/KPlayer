package dev.gokanaz.kplayer.core.model.media

data class AudioStreamInfo(
    val streamIndex: Int = 0,
    val codec: String = "",
    val profile: String = "",
    val bitrate: Long = 0,
    val sampleRate: Int = 0,
    val channels: Int = 0,
    val channelMask: String = "",
    val language: String = "und",
    val isDefault: Boolean = false,
    val isForced: Boolean = false
)
