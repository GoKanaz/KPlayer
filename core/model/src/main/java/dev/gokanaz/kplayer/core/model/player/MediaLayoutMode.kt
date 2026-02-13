package dev.gokanaz.kplayer.core.model.player

sealed class MediaLayoutMode {
    object FullScreen : MediaLayoutMode()
    object PictureInPicture : MediaLayoutMode()
    data class MiniPlayer(
        val width: Int = 320,
        val height: Int = 180,
        val positionX: Int = 0,
        val positionY: Int = 0
    ) : MediaLayoutMode()
    object Background : MediaLayoutMode()
}
