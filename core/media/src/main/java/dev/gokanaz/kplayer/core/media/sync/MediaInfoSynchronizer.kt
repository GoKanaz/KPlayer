package dev.gokanaz.kplayer.core.media.sync

interface MediaInfoSynchronizer {
    suspend fun syncInfo(mediaId: Long): Result<Unit>
}
