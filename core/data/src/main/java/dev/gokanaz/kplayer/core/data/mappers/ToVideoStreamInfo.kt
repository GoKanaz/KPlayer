package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.model.media.VideoStreamInfo

fun android.media.MediaMetadataRetriever.toVideoStreamInfo(): VideoStreamInfo? {
    val videoWidth = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: return null
    val videoHeight = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: return null
    val bitrate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLongOrNull() ?: 0
    val frameRate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)?.toDoubleOrNull() ?: 0.0

    return VideoStreamInfo(
        width = videoWidth,
        height = videoHeight,
        bitrate = bitrate,
        frameRate = frameRate
    )
}
