package dev.gokanaz.kplayer.core.media.sync

interface MediaSynchronizer {
    suspend fun sync(): Result<Unit>
}
