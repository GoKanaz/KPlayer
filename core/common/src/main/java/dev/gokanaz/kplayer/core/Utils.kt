package dev.gokanaz.kplayer.core

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.*
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object Utils {
    private val random = Random.Default

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
        return "%.1f %s".format(bytes / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
    }

    fun formatDuration(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        
        return if (hours > 0) {
            "%02d:%02d:%02d".format(hours, minutes, seconds)
        } else {
            "%02d:%02d".format(minutes, seconds)
        }
    }

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return emailRegex.toRegex().matches(email)
    }

    fun generateVideoThumbnail(uri: Uri, context: Context): Bitmap? {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(context, uri)
            val bitmap = retriever.frameAtTime
            retriever.release()
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    fun getMimeType(url: String): String? {
        return try {
            val extension = url.substringAfterLast(".", "").lowercase()
            when (extension) {
                "mp4", "m4v", "mkv" -> "video/$extension"
                "mp3", "m4a", "wav" -> "audio/$extension"
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                "pdf" -> "application/pdf"
                "txt" -> "text/plain"
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun hashString(input: String): String {
        return try {
            val md = MessageDigest.getInstance("MD5")
            BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        } catch (e: Exception) {
            input.hashCode().toString()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun getDeviceInfo(): String {
        return """
            Model: ${Build.MODEL}
            Brand: ${Build.BRAND}
            Manufacturer: ${Build.MANUFACTURER}
            Android: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})
            Device: ${Build.DEVICE}
            Product: ${Build.PRODUCT}
            Display: ${Build.DISPLAY}
        """.trimIndent()
    }

    fun randomString(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars[random.nextInt(chars.length)] }
            .joinToString("")
    }

    fun debounce(delay: Long, coroutineScope: CoroutineScope, action: suspend () -> Unit): () -> Unit {
        var job: Job? = null
        return {
            job?.cancel()
            job = coroutineScope.launch {
                delay(delay)
                action()
            }
        }
    }

    fun throttle(delay: Long, coroutineScope: CoroutineScope, action: suspend () -> Unit): () -> Unit {
        var lastTime = 0L
        var job: Job? = null
        return {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTime >= delay) {
                job?.cancel()
                job = coroutineScope.launch {
                    lastTime = System.currentTimeMillis()
                    action()
                }
            }
        }
    }
}
