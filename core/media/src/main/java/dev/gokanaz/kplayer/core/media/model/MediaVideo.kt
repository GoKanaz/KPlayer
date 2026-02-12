package dev.gokanaz.kplayer.core.media.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import java.util.Date

@Serializable
@Entity(tableName = "media_video")
data class MediaVideo(
    @PrimaryKey
    val id: String = generateMediaId(),
    @Contextual
    val uri: Uri,
    val title: String,
    val artist: String? = null,
    val album: String? = null,
    val duration: Long = 0,
    val size: Long = 0,
    val path: String,
    val mimeType: String,
    val dateAdded: Date = Date(),
    val dateModified: Date = Date(),
    val lastPlayedPosition: Long = 0,
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val thumbnail: String? = null
) {
    companion object {
        fun generateMediaId(): String {
            return "media_${System.currentTimeMillis()}_${(Math.random() * 1000000).toInt()}"
        }
    }
}

@Serializable
data class MediaPlaylist(
    @PrimaryKey
    val id: String = generatePlaylistId(),
    val name: String,
    val description: String? = null,
    val mediaIds: List<String> = emptyList(),
    val dateCreated: Date = Date(),
    val dateModified: Date = Date(),
    val artwork: String? = null
) {
    companion object {
        fun generatePlaylistId(): String {
            return "playlist_${System.currentTimeMillis()}_${(Math.random() * 1000000).toInt()}"
        }
    }
}

@Serializable
enum class MediaType {
    AUDIO,
    VIDEO,
    PLAYLIST
}

@Serializable
data class MediaMetadata(
    val mediaId: String,
    val bitrate: Int = 0,
    val sampleRate: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val codec: String? = null,
    val container: String? = null
)
