package dev.gokanaz.kplayer.core.media.sync

import dev.gokanaz.kplayer.core.media.model.MediaVideo

interface MediaInfoSynchronizer {
    suspend fun syncMediaInfo(mediaId: String): MediaVideo?
    suspend fun syncBatchMediaInfo(mediaIds: List<String>): List<MediaVideo>
    suspend fun updateMetadata(mediaId: String, metadata: Map<String, Any>): Boolean
    suspend fun fetchRemoteInfo(mediaId: String): Map<String, Any>?
}
