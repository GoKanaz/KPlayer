package dev.gokanaz.kplayer.core.data.mappers

import android.net.Uri
import dev.gokanaz.kplayer.core.data.models.VideoState
import dev.gokanaz.kplayer.core.model.Video
import dev.gokanaz.kplayer.core.model.VideoQuality
import dev.gokanaz.kplayer.core.media.model.MediaVideo

fun MediaVideo.toVideo(state: VideoState? = null): Video {
    return Video(
        id = this.id.toString(),
        title = this.title,
        uri = this.uri,
        duration = this.duration,
        size = this.size,
        dateAdded = this.dateAdded,
        dateModified = this.dateModified,
        mimeType = this.mimeType,
        resolution = this.resolution,
        bucketId = this.bucketId,
        bucketDisplayName = this.bucketDisplayName,
        lastPlayedPosition = state?.lastPlayedPositionMs ?: 0,
        isFavorite = state?.isFavorite ?: false,
        watchCount = state?.watchCount ?: 0,
        lastPlayedAt = state?.lastPlayedAt ?: 0,
        tags = state?.tags ?: emptySet(),
        availableQualities = listOf(
            VideoQuality.P144,
            VideoQuality.P240,
            VideoQuality.P360,
            VideoQuality.P480,
            VideoQuality.P720,
            VideoQuality.P1080
        ),
        subtitleTracks = emptyList(),
        audioTracks = emptyList()
    )
}

fun List<MediaVideo>.toVideos(stateMap: Map<String, VideoState>): List<Video> {
    return this.map { mediaVideo ->
        val state = stateMap[mediaVideo.id.toString()]
        mediaVideo.toVideo(state)
    }
}
