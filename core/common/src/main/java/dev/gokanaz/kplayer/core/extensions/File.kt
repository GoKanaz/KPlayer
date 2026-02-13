package dev.gokanaz.kplayer.core.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.ZoneId

fun File.copyTo(destination: File, overwrite: Boolean = false): File {
    if (destination.exists() && !overwrite) {
        return destination
    }
    return this.inputStream().use { input ->
        destination.outputStream().use { output ->
            input.copyTo(output)
        }
    }.let { destination }
}

fun File.deleteDirectory(): Boolean {
    return if (isDirectory) {
        listFiles()?.forEach { it.deleteDirectory() }
        delete()
    } else {
        delete()
    }
}

fun File.getFileSize(): Long {
    return if (isDirectory) {
        listFiles()?.sumOf { it.getFileSize() } ?: 0
    } else {
        length()
    }
}

fun File.getFileCount(): Int {
    return if (isDirectory) {
        listFiles()?.size ?: 0
    } else {
        1
    }
}

fun File.getFileExtension(): String {
    val name = name
    return name.substringAfterLast(".", "")
}

fun File.getFileNameWithoutExtension(): String {
    val name = name
    return name.substringBeforeLast(".", name)
}

fun File.isImage(): Boolean {
    val extension = getFileExtension().lowercase()
    return extension in listOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
}

fun File.isVideo(): Boolean {
    val extension = getFileExtension().lowercase()
    return extension in listOf("mp4", "mkv", "avi", "mov", "wmv", "flv", "webm")
}

fun File.isAudio(): Boolean {
    val extension = getFileExtension().lowercase()
    return extension in listOf("mp3", "wav", "ogg", "m4a", "flac", "aac")
}

fun File.compressImage(quality: Int, format: Bitmap.CompressFormat): File {
    val bitmap = BitmapFactory.decodeFile(this.absolutePath)
    val outputFile = File(this.parent, "compressed_${this.name}")
    FileOutputStream(outputFile).use { out ->
        bitmap.compress(format, quality, out)
    }
    return outputFile
}

fun File.toBase64(): String {
    val bytes = FileInputStream(this).use { it.readBytes() }
    return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
}

fun File.uri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        context.contentResolver.insert(collection, values) ?: Uri.fromFile(this)
    } else {
        Uri.fromFile(this)
    }
}

fun File.md5(): String {
    return try {
        val md = MessageDigest.getInstance("MD5")
        val digest = FileInputStream(this).use { md.digest(it.readBytes()) }
        digest.joinToString("") { "%02x".format(it) }
    } catch (e: Exception) {
        ""
    }
}

fun File.lastModifiedDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(lastModified()),
            ZoneId.systemDefault()
        )
    } catch (e: Exception) {
        null
    }
}

fun File.listFilesRecursively(): List<File> {
    val files = mutableListOf<File>()
    if (isDirectory) {
        listFiles()?.forEach {
            if (it.isDirectory) {
                files.addAll(it.listFilesRecursively())
            } else {
                files.add(it)
            }
        }
    } else {
        files.add(this)
    }
    return files
}
