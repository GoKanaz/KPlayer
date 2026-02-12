package dev.gokanaz.kplayer.core.media.sync

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import kotlinx.coroutines.flow.Flow

interface MediaSynchronizer {
    suspend fun syncMedia(): Flow<SyncProgress>
    suspend fun syncMediaItem(mediaId: String): MediaVideo?
    suspend fun searchMedia(query: String): List<MediaVideo>
}

sealed class SyncProgress {
    object Started : SyncProgress()
    data class Progress(val current: Int, val total: Int) : SyncProgress()
    data class Completed(val total: Int) : SyncProgress()
    data class Error(val message: String) : SyncProgress()
}
