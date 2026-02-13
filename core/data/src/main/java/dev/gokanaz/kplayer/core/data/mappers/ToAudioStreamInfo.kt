package dev.gokanaz.kplayer.core.data.mappers

import dev.gokanaz.kplayer.core.domain.AudioStreamInfo

fun android.media.MediaMetadataRetriever.toAudioStreamInfo(): AudioStreamInfo? {
    val bitrate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_BITRATE)?.toLongOrNull() ?: 0
    val sampleRate = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_SAMPLERATE)?.toIntOrNull()
    val codec = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_AUDIO_CODEC) ?: ""
    val channelCount = this.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS)?.toIntOrNull() ?: 2
    
    return AudioStreamInfo(
        bitrate = bitrate,
        sampleRate = sampleRate ?: 44100,
        codec = codec,
        channelCount = channelCount,
        language = "und"
    )
}
