package dev.gokanaz.kplayer.core.model.player

enum class FastSeek {
    ALWAYS,
    WIFI_ONLY,
    NEVER
}

data class FastSeekConfig(
    val mode: FastSeek = FastSeek.WIFI_ONLY,
    val seekIntervalSeconds: Int = 10
)
