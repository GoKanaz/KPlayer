package dev.gokanaz.kplayer.core.media.sync

import android.content.ContentResolver
import android.graphics.Bitmap
import android.provider.MediaStore
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class LocalMediaInfoSynchronizer @Inject constructor(
    private val contentResolver: ContentResolver
) : MediaInfoSynchronizer {

    override suspend fun syncInfo(mediaId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val thumbnail = getThumbnail(mediaId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getThumbnail(mediaId: Long): Bitmap? {
        return MediaStore.Video.Thumbnails.getThumbnail(
            contentResolver,
            mediaId,
            MediaStore.Video.Thumbnails.MINI_KIND,
            null
        )
    }
}
