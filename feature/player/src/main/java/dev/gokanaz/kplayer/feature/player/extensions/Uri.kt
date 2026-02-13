package dev.gokanaz.kplayer.feature.player.extensions

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.media3.common.MediaItem
import java.io.File

fun Uri.isLocalFile(): Boolean {
    return this.scheme == "file" || this.scheme == "content"
}

fun Uri.isNetworkStream(): Boolean {
    return this.scheme == "http" || this.scheme == "https" || this.scheme == "rtsp"
}

fun Uri.getFileName(context: Context): String? {
    return when {
        this.scheme == "file" -> this.path?.substringAfterLast("/")
        this.scheme == "content" -> {
            val cursor = context.contentResolver.query(this, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                if (it.moveToFirst()) {
                    return it.getString(nameIndex)
                }
            }
            null
        }
        else -> this.lastPathSegment
    }
}

fun Uri.getFileSize(context: Context): Long {
    return when {
        this.scheme == "file" -> File(this.path ?: return 0).length()
        this.scheme == "content" -> {
            val cursor = context.contentResolver.query(this, null, null, null, null)
            cursor?.use {
                val sizeIndex = it.getColumnIndex(MediaStore.MediaColumns.SIZE)
                if (it.moveToFirst()) {
                    return it.getLong(sizeIndex)
                }
            }
            0
        }
        else -> 0
    }
}

fun Uri.getMimeType(context: Context): String? {
    return when {
        this.scheme == "file" -> {
            val extension = this.path?.substringAfterLast(".", "")
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        this.scheme == "content" -> context.contentResolver.getType(this)
        else -> null
    }
}

fun Uri.toMediaItem(): MediaItem {
    return MediaItem.fromUri(this)
}

fun Uri.isPlaylist(): Boolean {
    val path = this.toString().lowercase()
    return path.endsWith(".m3u") || 
           path.endsWith(".m3u8") || 
           path.endsWith(".pls") || 
           path.endsWith(".wpl") || 
           path.endsWith(".asx")
}

fun Uri.getCacheKey(): String {
    return when {
        this.scheme == "content" -> {
            val id = this.lastPathSegment ?: this.toString()
            "content_$id"
        }
        else -> {
            val path = this.toString()
            "uri_${path.hashCode()}"
        }
    }
}
