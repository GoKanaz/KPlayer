package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class MetadataState(
    val videoId: String = "",
    val title: String = "Unknown",
    val fileName: String = "",
    val filePath: String = "",
    val duration: Long = 0,
    val currentPosition: Long = 0,
    val bufferedPosition: Long = 0,
    val bitrate: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val frameRate: Float = 0f,
    val creationDate: Long = 0,
    val modificationDate: Long = 0,
    val fileSize: Long = 0,
    val mimeType: String = "video/*",
    val thumbnailUri: String = "",
    val artist: String = "",
    val album: String = "",
    val customMetadata: Map<String, String> = emptyMap()
) {
    companion object {
        val Initial = MetadataState()

        val Sample = MetadataState(
            videoId = "123",
            title = "Sample Video",
            fileName = "sample.mp4",
            filePath = "/storage/emulated/0/Video/sample.mp4",
            duration = 3600000,
            currentPosition = 125000,
            bufferedPosition = 180000,
            bitrate = 2500000,
            width = 1920,
            height = 1080,
            frameRate = 24f,
            creationDate = System.currentTimeMillis() - 86400000,
            modificationDate = System.currentTimeMillis() - 3600000,
            fileSize = 1500000000,
            mimeType = "video/mp4",
            thumbnailUri = "content://media/external/video/media/123/thumbnail"
        )
    }

    fun withPosition(positionMs: Long): MetadataState {
        return copy(
            currentPosition = positionMs.coerceIn(0, duration)
        )
    }

    fun withBufferedPosition(positionMs: Long): MetadataState {
        return copy(
            bufferedPosition = positionMs.coerceIn(0, duration)
        )
    }

    fun withVideoInfo(video: dev.gokanaz.kplayer.core.model.Video): MetadataState {
        return copy(
            videoId = video.id,
            title = video.title,
            fileName = video.fileName,
            filePath = video.filePath,
            duration = video.duration,
            width = video.width,
            height = video.height,
            thumbnailUri = video.thumbnail,
            fileSize = video.size,
            mimeType = video.mimeType
        )
    }

    fun formatDuration(): String {
        return duration.formatDuration()
    }

    fun formatCurrentPosition(): String {
        return currentPosition.formatDuration()
    }

    fun formatRemainingTime(): String {
        return (duration - currentPosition).coerceAtLeast(0).formatDuration()
    }

    fun progressPercentage(): Float {
        return if (duration > 0) {
            (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
        } else 0f
    }

    fun bufferedPercentage(): Int {
        return if (duration > 0) {
            ((bufferedPosition.toFloat() / duration.toFloat()) * 100).toInt().coerceIn(0, 100)
        } else 0
    }

    fun isNearEnd(thresholdMs: Long = 10000): Boolean {
        return duration - currentPosition <= thresholdMs
    }

    fun getResolution(): String {
        return if (width > 0 && height > 0) {
            "${width}x${height}"
        } else {
            "Unknown"
        }
    }

    fun getShortResolution(): String {
        return when {
            height >= 2160 -> "4K"
            height >= 1440 -> "2K"
            height >= 1080 -> "1080p"
            height >= 720 -> "720p"
            height >= 480 -> "480p"
            height >= 360 -> "360p"
            else -> "SD"
        }
    }

    fun reset(): MetadataState {
        return copy(
            currentPosition = 0,
            bufferedPosition = 0
        )
    }
}

fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
