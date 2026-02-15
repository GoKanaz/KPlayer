package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.model.VideoQuality
import dev.gokanaz.kplayer.core.model.VideoStreamInfo

fun android.media.MediaMetadataRetriever.toVideoStreamInfo(): VideoStreamInfo? {
    val videoWidth = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: return null
    val videoHeight = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: return null
    val bitrate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLongOrNull() ?: 0
    val frameRate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)?.toFloatOrNull() ?: 0f
    val codec = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_CODEC) ?: ""
    
    val quality = when {
        videoHeight >= 2160 -> VideoQuality.P2160
        videoHeight >= 1440 -> VideoQuality.P1440
        videoHeight >= 1080 -> VideoQuality.P1080
        videoHeight >= 720 -> VideoQuality.P720
        videoHeight >= 480 -> VideoQuality.P480
        videoHeight >= 360 -> VideoQuality.P360
        videoHeight >= 240 -> VideoQuality.P240
        videoHeight >= 144 -> VideoQuality.P144
        else -> VideoQuality.AUTO
    }
    
    return VideoStreamInfo(
        width = videoWidth,
        height = videoHeight,
        bitrate = bitrate,
        frameRate = frameRate,
        codec = codec,
        quality = quality
    )
}
