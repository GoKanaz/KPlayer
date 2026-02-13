package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.domain.SubtitleStreamInfo

fun android.media.MediaMetadataRetriever.toSubtitleStreamInfo(index: Int): SubtitleStreamInfo? {
    return SubtitleStreamInfo(
        index = index,
        language = "und",
        format = "unknown",
        isDefault = index == 0
    )
}
