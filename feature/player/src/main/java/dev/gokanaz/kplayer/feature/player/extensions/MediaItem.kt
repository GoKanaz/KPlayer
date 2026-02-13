package dev.gokanaz.kplayer.feature.player.extensions

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import dev.gokanaz.kplayer.core.model.Video

fun MediaItem.toVideo(): Video? {
    val uri = this.localConfiguration?.uri?.toString() ?: return null
    
    return Video(
        id = uri.hashCode().toString(),
        title = this.mediaMetadata.title?.toString() ?: "Unknown",
        fileName = uri.substringAfterLast("/"),
        filePath = uri,
        uri = uri,
        mimeType = this.mediaMetadata.mimeType ?: MimeTypes.VIDEO_UNKNOWN,
        duration = this.mediaMetadata.extras?.getLong("duration", 0) ?: 0
    )
}

fun MediaItem.getDuration(): Long {
    return this.mediaMetadata.extras?.getLong("duration", 0) ?: 0
}

fun MediaItem.getTitle(): String {
    return this.mediaMetadata.title?.toString() ?: "Unknown"
}

fun MediaItem.getArtworkUri(): Uri? {
    return this.mediaMetadata.artworkUri
}

fun MediaItem.isHls(): Boolean {
    return this.localConfiguration?.uri.toString().contains(".m3u8", ignoreCase = true)
}

fun MediaItem.isDash(): Boolean {
    return this.localConfiguration?.uri.toString().contains(".mpd", ignoreCase = true)
}

fun MediaItem.isLiveStream(): Boolean {
    return this.mediaMetadata.extras?.getBoolean("isLive", false) ?: false
}

fun MediaItem.buildUponWithUri(uri: Uri): MediaItem {
    return this.buildUpon()
        .setUri(uri)
        .build()
}
