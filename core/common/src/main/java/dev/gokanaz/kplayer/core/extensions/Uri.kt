package dev.gokanaz.kplayer.core.extensions

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun Uri.toFile(context: Context): File? {
    return when {
        isFileUri() -> File(path ?: return null)
        isContentUri() -> getFileFromContentUri(context)
        else -> null
    }
}

fun Uri.getFileName(context: Context): String? {
    return when {
        isFileUri() -> path?.substringAfterLast("/")
        isContentUri() -> getFileNameFromContentUri(context)
        else -> null
    }
}

fun Uri.getFileSize(context: Context): Long {
    return when {
        isFileUri() -> File(path ?: return 0).length()
        isContentUri() -> getFileSizeFromContentUri(context)
        else -> 0
    }
}

fun Uri.getMimeType(context: Context): String? {
    return when {
        isFileUri() -> context.contentResolver.getType(this)
        isContentUri() -> context.contentResolver.getType(this)
        else -> null
    }
}

fun Uri.getFilePath(context: Context): String? {
    return toFile(context)?.absolutePath
}

fun Uri.isContentUri(): Boolean {
    return scheme == "content"
}

fun Uri.isFileUri(): Boolean {
    return scheme == "file"
}

fun Uri.createVideoThumbnail(context: Context): Bitmap? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, this)
        retriever.frameAtTime
    } catch (e: Exception) {
        null
    } finally {
        // MediaMetadataRetriever will be released automatically
    }
}

fun Uri.openInputStream(context: Context): InputStream? {
    return try {
        context.contentResolver.openInputStream(this)
    } catch (e: Exception) {
        null
    }
}

fun Uri.openOutputStream(context: Context): OutputStream? {
    return try {
        context.contentResolver.openOutputStream(this)
    } catch (e: Exception) {
        null
    }
}

fun Uri.copyTo(context: Context, destinationFile: File): Boolean {
    return try {
        openInputStream(context)?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        } != null
    } catch (e: Exception) {
        false
    }
}

private fun Uri.getFileFromContentUri(context: Context): File? {
    val fileName = getFileNameFromContentUri(context) ?: return null
    val cacheDir = File(context.cacheDir, "uri_cache")
    cacheDir.mkdirs()
    val file = File(cacheDir, fileName)
    
    return if (copyTo(context, file)) file else null
}

private fun Uri.getFileNameFromContentUri(context: Context): String? {
    var cursor: Cursor? = null
    return try {
        cursor = context.contentResolver.query(this, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    } catch (e: Exception) {
        null
    } finally {
        cursor?.close()
    }
}

private fun Uri.getFileSizeFromContentUri(context: Context): Long {
    var cursor: Cursor? = null
    return try {
        cursor = context.contentResolver.query(this, null, null, null, null)
        cursor?.use {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            it.moveToFirst()
            it.getLong(sizeIndex)
        } ?: 0
    } catch (e: Exception) {
        0
    } finally {
        cursor?.close()
    }
}
