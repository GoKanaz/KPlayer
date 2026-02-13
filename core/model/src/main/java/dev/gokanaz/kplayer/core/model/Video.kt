package dev.gokanaz.kplayer.core.model

import dev.gokanaz.kplayer.core.model.media.VideoStreamInfo
import dev.gokanaz.kplayer.core.model.media.AudioStreamInfo
import dev.gokanaz.kplayer.core.model.media.SubtitleStreamInfo

data class Video(
    val id: String,
    val title: String,
    val fileName: String,
    val filePath: String,
    val uri: String,
    val mimeType: String = "video/*",
    val size: Long = 0,
    val duration: Long = 0,
    val dateAdded: Long = 0,
    val dateModified: Long = 0,
    val bucketId: String = "",
    val bucketDisplayName: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val resolution: String = "",
    val thumbnail: String = "",
    val isFavorite: Boolean = false,
    val lastPlayedPosition: Long = 0,
    val watchCount: Int = 0,
    val videoStreams: List<VideoStreamInfo> = emptyList(),
    val audioStreams: List<AudioStreamInfo> = emptyList(),
    val subtitleStreams: List<SubtitleStreamInfo> = emptyList()
)
