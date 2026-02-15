package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.model.media.AudioStreamInfo

fun android.media.MediaMetadataRetriever.toAudioStreamInfo(): AudioStreamInfo? {
    val bitrate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLongOrNull() ?: 0
    val sampleRate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_SAMPLERATE)?.toIntOrNull() ?: 44100
    val channels = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS)?.toIntOrNull() ?: 2

    return AudioStreamInfo(
        bitrate = bitrate,
        sampleRate = sampleRate,
        channels = channels,
        language = "und"
    )
}
