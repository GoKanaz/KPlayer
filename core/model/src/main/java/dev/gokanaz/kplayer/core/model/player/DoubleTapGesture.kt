package dev.gokanaz.kplayer.core.model.player

enum class DoubleTapGesture {
    SEEK_FORWARD_BACKWARD,
    PLAY_PAUSE,
    ZOOM,
    NONE
}

data class DoubleTapConfig(
    val gesture: DoubleTapGesture = DoubleTapGesture.SEEK_FORWARD_BACKWARD,
    val seekIntervalSeconds: Int = 10,
    val leftAreaSeekBackward: Boolean = true,
    val rightAreaSeekForward: Boolean = true
)
