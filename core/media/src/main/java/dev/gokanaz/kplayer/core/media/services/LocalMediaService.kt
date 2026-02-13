package dev.gokanaz.kplayer.core.media.services

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import dagger.hilt.android.scopes.ViewModelScoped
import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.service.MediaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class LocalMediaService @Inject constructor(
    private val contentResolver: ContentResolver
) : MediaService {

    override fun getVideos(): Flow<List<MediaVideo>> = flow {
        val videos = queryVideos()
        emit(videos)
    }

    override suspend fun getVideo(id: Long): MediaVideo? = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )
        
        val selection = "${MediaStore.Video.Media._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return@withContext cursorToMediaVideo(cursor)
            }
        }
        return@withContext null
    }

    override suspend fun search(query: String): List<MediaVideo> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )
        
        val selection = "${MediaStore.Video.Media.TITLE} LIKE ?"
        val selectionArgs = arrayOf("%$query%")
        
        queryVideos(selection, selectionArgs)
    }

    override suspend fun getRecent(limit: Int): List<MediaVideo> = withContext(Dispatchers.IO) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )
        
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC LIMIT $limit"
        
        queryVideos(null, null, sortOrder)
    }

    private fun queryVideos(
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): List<MediaVideo> {
        val videos = mutableListOf<MediaVideo>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        )
        
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
            val resolutionColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn) ?: ""
                val data = cursor.getString(dataColumn) ?: ""
                val duration = cursor.getLongOrNull(durationColumn) ?: 0
                val size = cursor.getLongOrNull(sizeColumn) ?: 0
                val dateAdded = cursor.getLongOrNull(dateAddedColumn) ?: 0
                val dateModified = cursor.getLongOrNull(dateModifiedColumn) ?: 0
                val mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: "video/*"
                val resolution = cursor.getStringOrNull(resolutionColumn) ?: ""
                val bucketId = cursor.getStringOrNull(bucketIdColumn) ?: ""
                val bucketDisplayName = cursor.getStringOrNull(bucketDisplayNameColumn) ?: ""
                
                val video = MediaVideo(
                    id = id,
                    title = title,
                    uri = Uri.parse(data),
                    duration = duration,
                    size = size,
                    dateAdded = dateAdded,
                    dateModified = dateModified,
                    mimeType = mimeType,
                    resolution = resolution,
                    bucketId = bucketId,
                    bucketDisplayName = bucketDisplayName
                )
                videos.add(video)
            }
        }
        return videos
    }

    private fun cursorToMediaVideo(cursor: android.database.Cursor): MediaVideo {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
        val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
        val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
        val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
        val resolutionColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)
        val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
        val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        
        return MediaVideo(
            id = cursor.getLong(idColumn),
            title = cursor.getString(titleColumn) ?: "",
            uri = Uri.parse(cursor.getString(dataColumn) ?: ""),
            duration = cursor.getLongOrNull(durationColumn) ?: 0,
            size = cursor.getLongOrNull(sizeColumn) ?: 0,
            dateAdded = cursor.getLongOrNull(dateAddedColumn) ?: 0,
            dateModified = cursor.getLongOrNull(dateModifiedColumn) ?: 0,
            mimeType = cursor.getStringOrNull(mimeTypeColumn) ?: "video/*",
            resolution = cursor.getStringOrNull(resolutionColumn) ?: "",
            bucketId = cursor.getStringOrNull(bucketIdColumn) ?: "",
            bucketDisplayName = cursor.getStringOrNull(bucketDisplayNameColumn) ?: ""
        )
    }
}
