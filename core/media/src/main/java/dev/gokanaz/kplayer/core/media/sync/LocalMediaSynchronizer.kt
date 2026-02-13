package dev.gokanaz.kplayer.core.media.sync

import dev.gokanaz.kplayer.core.media.services.LocalMediaService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaSynchronizer @Inject constructor(
    private val localMediaService: LocalMediaService
) : MediaSynchronizer {

    override suspend fun sync(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            localMediaService.getVideos()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
