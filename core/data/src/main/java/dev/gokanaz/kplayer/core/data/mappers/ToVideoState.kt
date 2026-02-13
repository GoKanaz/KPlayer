package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.data.models.VideoState
import dev.gokanaz.kplayer.core.datastore.datasource.PlaybackHistoryItem
import dev.gokanaz.kplayer.core.datastore.proto.PlayerSettingsProto

fun PlayerSettingsProto.ResumePositionsEntry.toVideoState(): VideoState {
    return VideoState(
        videoId = this.key,
        lastPlayedPositionMs = this.value
    )
}

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
