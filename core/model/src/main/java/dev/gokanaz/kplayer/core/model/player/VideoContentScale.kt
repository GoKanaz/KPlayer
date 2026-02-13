package dev.gokanaz.kplayer.core.model.player

enum class VideoContentScale {
    FIT,
    FILL,
    ZOOM,
    STRETCH,
    CROP,
    ORIGINAL
}

data class CustomContentScale(
    val scaleFactor: Float = 1.0f,
    val isAspectRatioLocked: Boolean = true
)
