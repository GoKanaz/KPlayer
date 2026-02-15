package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.data.models.VideoState
import dev.gokanaz.kplayer.core.datastore.datasource.PlaybackHistoryItem

fun PlaybackHistoryItem.toVideoState(): VideoState {
    return VideoState(
        videoId = this.videoId,
        lastPlayedPositionMs = this.positionMs,
        lastPlayedAt = this.watchedAt,
        watchCount = 1
    )
}

fun Map.Entry<String, Long>.toVideoState(): VideoState {
    return VideoState(
        videoId = this.key,
        lastPlayedPositionMs = this.value
    )
}
