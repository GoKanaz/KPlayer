package dev.gokanaz.kplayer.feature.player.extensions

import dev.gokanaz.kplayer.core.model.media.VideoStreamInfo

data class VideoSize(
    val width: Int,
    val height: Int
)

fun VideoSize.getAspectRatio(): Float {
    if (height == 0) return 1f
    return width.toFloat() / height.toFloat()
}

fun VideoSize.isPortrait(): Boolean {
    return height > width
}

fun VideoSize.isLandscape(): Boolean {
    return width > height
}

fun VideoSize.isSquare(): Boolean {
    return width == height
}

fun VideoSize.toDisplayResolution(): String {
    return "${width}x${height}"
}

fun VideoSize.toShortDisplayResolution(): String {
    return when {
        height >= 2160 -> "4K"
        height >= 1440 -> "2K"
        height >= 1080 -> "1080p"
        height >= 720 -> "720p"
        height >= 480 -> "480p"
        height >= 360 -> "360p"
        height >= 240 -> "240p"
        height >= 144 -> "144p"
        else -> "SD"
    }
}

fun VideoSize.getPixelCount(): Int {
    return width * height
}

fun VideoStreamInfo.toVideoSize(): VideoSize {
    return VideoSize(
        width = this.width,
        height = this.height
    )
}

fun VideoSize.isValid(): Boolean {
    return width > 0 && height > 0
}
