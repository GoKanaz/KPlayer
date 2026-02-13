package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import androidx.media3.common.Format
import java.util.Locale

@Immutable
data class TracksState(
    val videoTracks: List<TrackInfo> = emptyList(),
    val audioTracks: List<TrackInfo> = emptyList(),
    val subtitleTracks: List<TrackInfo> = emptyList(),
    val selectedVideoTrackIndex: Int = 0,
    val selectedAudioTrackIndex: Int = 0,
    val selectedSubtitleTrackIndex: Int = -1,
    val isSubtitleEnabled: Boolean = true
) {
    companion object {
        val Initial = TracksState(
            isSubtitleEnabled = true
        )
        
        val Sample = TracksState(
            videoTracks = listOf(
                TrackInfo(
                    id = "1",
                    type = TrackType.VIDEO,
                    language = "eng",
                    label = "1080p",
                    mimeType = "video/avc",
                    bitrate = 5000000,
                    width = 1920,
                    height = 1080,
                    isDefault = true
                ),
                TrackInfo(
                    id = "2",
                    type = TrackType.VIDEO,
                    language = "eng",
                    label = "720p",
                    mimeType = "video/avc",
                    bitrate = 2500000,
                    width = 1280,
                    height = 720
                )
            ),
            audioTracks = listOf(
                TrackInfo(
                    id = "3",
                    type = TrackType.AUDIO,
                    language = "eng",
                    label = "English 5.1",
                    mimeType = "audio/eac3",
                    bitrate = 640000,
                    channelCount = 6,
                    sampleRate = 48000,
                    isDefault = true
                ),
                TrackInfo(
                    id = "4",
                    type = TrackType.AUDIO,
                    language = "spa",
                    label = "Spanish",
                    mimeType = "audio/mp4a-latm",
                    bitrate = 192000,
                    channelCount = 2,
                    sampleRate = 44100
                )
            ),
            subtitleTracks = listOf(
                TrackInfo(
                    id = "5",
                    type = TrackType.SUBTITLE,
                    language = "eng",
                    label = "English",
                    mimeType = "text/x-srt",
                    isDefault = true,
                    isForced = false
                ),
                TrackInfo(
                    id = "6",
                    type = TrackType.SUBTITLE,
                    language = "spa",
                    label = "Spanish",
                    mimeType = "text/x-srt",
                    isDefault = false,
                    isForced = false
                )
            ),
            selectedVideoTrackIndex = 0,
            selectedAudioTrackIndex = 0,
            selectedSubtitleTrackIndex = -1,
            isSubtitleEnabled = true
        )
    }
    
    fun withVideoTrackSelected(index: Int): TracksState {
        return if (index in videoTracks.indices) {
            copy(
                selectedVideoTrackIndex = index
            )
        } else this
    }
    
    fun withAudioTrackSelected(index: Int): TracksState {
        return if (index in audioTracks.indices) {
            copy(
                selectedAudioTrackIndex = index
            )
        } else this
    }
    
    fun withSubtitleTrackSelected(index: Int): TracksState {
        return if (index in subtitleTracks.indices) {
            copy(
                selectedSubtitleTrackIndex = index,
                isSubtitleEnabled = true
            )
        } else this
    }
    
    fun withSubtitleEnabled(enabled: Boolean): TracksState {
        return copy(
            isSubtitleEnabled = enabled,
            selectedSubtitleTrackIndex = if (enabled) selectedSubtitleTrackIndex else -1
        )
    }
    
    fun withTracks(
        video: List<TrackInfo>,
        audio: List<TrackInfo>,
        subtitle: List<TrackInfo>
    ): TracksState {
        return copy(
            videoTracks = video,
            audioTracks = audio,
            subtitleTracks = subtitle
        )
    }
    
    fun getSelectedVideoTrack(): TrackInfo? {
        return videoTracks.getOrNull(selectedVideoTrackIndex)
    }
    
    fun getSelectedAudioTrack(): TrackInfo? {
        return audioTracks.getOrNull(selectedAudioTrackIndex)
    }
    
    fun getSelectedSubtitleTrack(): TrackInfo? {
        return subtitleTracks.getOrNull(selectedSubtitleTrackIndex)
    }
    
    fun getPreferredTrackByLanguage(tracks: List<TrackInfo>, preferredLanguage: String): Int {
        return tracks.indexOfFirst { it.language == preferredLanguage }
            .takeIf { it >= 0 } ?: 0
    }
    
    fun getTrackDisplayName(track: TrackInfo): String {
        return when {
            track.label.isNotEmpty() -> track.label
            track.language.isNotEmpty() -> Locale(track.language).displayLanguage
            else -> "Unknown"
        }
    }
}

data class TrackInfo(
    val id: String,
    val type: TrackType,
    val language: String = "",
    val label: String = "",
    val mimeType: String = "",
    val bitrate: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val frameRate: Float = 0f,
    val channelCount: Int = 0,
    val sampleRate: Int = 0,
    val isDefault: Boolean = false,
    val isForced: Boolean = false
)

enum class TrackType {
    VIDEO,
    AUDIO,
    SUBTITLE
}
