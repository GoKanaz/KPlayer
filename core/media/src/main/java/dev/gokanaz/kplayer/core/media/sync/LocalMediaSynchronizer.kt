package dev.gokanaz.kplayer.core.media.sync

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.services.LocalMediaService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaSynchronizer @Inject constructor(
    private val localMediaService: LocalMediaService
) : MediaSynchronizer {
    
    override suspend fun syncMedia(): Flow<SyncProgress> = flow {
        emit(SyncProgress.Started)
        
        try {
            localMediaService.refreshMedia()
            val videos = localMediaService.allVideos.value
            
            emit(SyncProgress.Progress(0, videos.size))
            emit(SyncProgress.Completed(videos.size))
        } catch (e: Exception) {
            emit(SyncProgress.Error(e.message ?: "Sync failed"))
        }
    }
    
    override suspend fun syncMediaItem(mediaId: String): MediaVideo? {
        return localMediaService.getVideoById(mediaId)
    }
    
    override suspend fun searchMedia(query: String): List<MediaVideo> {
        return localMediaService.searchVideos(query)
    }
}
