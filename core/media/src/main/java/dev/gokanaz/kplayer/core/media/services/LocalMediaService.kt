package dev.gokanaz.kplayer.core.media.services

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import dev.gokanaz.kplayer.core.media.model.MediaVideo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaService @Inject constructor(
    private val contentResolver: ContentResolver
) {
    
    private val _allVideos = MutableStateFlow<List<MediaVideo>>(emptyList())
    val allVideos: StateFlow<List<MediaVideo>> = _allVideos.asStateFlow()
    
    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            refreshMedia()
        }
    }
    
    init {
        contentResolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver
        )
        refreshMedia()
    }
    
    fun refreshMedia() {
        val videos = queryVideos()
        _allVideos.value = videos
    }
    
    private fun queryVideos(): List<MediaVideo> {
        val videos = mutableListOf<MediaVideo>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.ARTIST,
            MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED
        )
        
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )
        
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val mimeTypeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
            val dateAddedColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val dateModifiedColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
            
            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn) ?: "Unknown"
                val artist = it.getStringOrNull(artistColumn)
                val album = it.getStringOrNull(albumColumn)
                val duration = it.getLongOrNull(durationColumn) ?: 0
                val size = it.getLongOrNull(sizeColumn) ?: 0
                val path = it.getString(dataColumn) ?: continue
                val mimeType = it.getString(mimeTypeColumn) ?: "video/*"
                val dateAdded = it.getLongOrNull(dateAddedColumn) ?: 0
                val dateModified = it.getLongOrNull(dateModifiedColumn) ?: 0
                
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                
                val mediaVideo = MediaVideo(
                    id = "local_$id",
                    uri = contentUri,
                    title = title,
                    artist = artist,
                    album = album,
                    duration = duration,
                    size = size,
                    path = path,
                    mimeType = mimeType,
                    dateAdded = java.util.Date(dateAdded * 1000),
                    dateModified = java.util.Date(dateModified * 1000)
                )
                
                videos.add(mediaVideo)
            }
        }
        
        return videos
    }
    
    fun getVideoById(id: String): MediaVideo? {
        return _allVideos.value.find { it.id == id }
    }
    
    fun searchVideos(query: String): List<MediaVideo> {
        return _allVideos.value.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.artist?.contains(query, ignoreCase = true) == true ||
            it.album?.contains(query, ignoreCase = true) == true
        }
    }
}
