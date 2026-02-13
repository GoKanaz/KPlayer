package dev.gokanaz.kplayer.core.model.media

data class SubtitleStreamInfo(
    val streamIndex: Int = 0,
    val codec: String = "",
    val language: String = "und",
    val title: String = "",
    val isDefault: Boolean = false,
    val isForced: Boolean = false,
    val isExternal: Boolean = false,
    val externalPath: String = ""
)
