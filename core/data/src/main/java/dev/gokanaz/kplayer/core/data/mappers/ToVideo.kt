package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.data.models.VideoState
import dev.gokanaz.kplayer.core.model.Video
import dev.gokanaz.kplayer.core.media.model.MediaVideo

fun MediaVideo.toVideo(state: VideoState? = null): Video {
    return Video(
        id = this.id.toString(),
        title = this.title,
        fileName = this.title,
        filePath = this.uri.toString(),
        uri = this.uri.toString(),
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
        watchCount = state?.watchCount ?: 0
    )
}

fun List<MediaVideo>.toVideos(stateMap: Map<String, VideoState>): List<Video> {
    return this.map { mediaVideo ->
        val state = stateMap[mediaVideo.id.toString()]
        mediaVideo.toVideo(state)
    }
}
