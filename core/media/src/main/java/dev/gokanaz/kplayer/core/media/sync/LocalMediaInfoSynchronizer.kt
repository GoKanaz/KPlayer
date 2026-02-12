package dev.gokanaz.kplayer.core.media.sync

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalMediaInfoSynchronizer @Inject constructor() : MediaInfoSynchronizer {
    
    override suspend fun syncMediaInfo(mediaId: String): MediaVideo? {
        return null
    }
    
    override suspend fun syncBatchMediaInfo(mediaIds: List<String>): List<MediaVideo> {
        return emptyList()
    }
    
    override suspend fun updateMetadata(mediaId: String, metadata: Map<String, Any>): Boolean {
        return false
    }
    
    override suspend fun fetchRemoteInfo(mediaId: String): Map<String, Any>? {
        return null
    }
}
