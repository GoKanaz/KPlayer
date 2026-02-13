package dev.gokanaz.kplayer.core.model.player

enum class DecoderPriority {
    HARDWARE,
    SOFTWARE,
    AUTO
}

data class DecoderConfig(
    val priority: DecoderPriority = DecoderPriority.AUTO,
    val fallbackToSoftware: Boolean = true
)
