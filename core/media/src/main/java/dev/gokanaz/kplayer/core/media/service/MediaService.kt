package dev.gokanaz.kplayer.core.media.service

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import kotlinx.coroutines.flow.Flow

interface MediaService {
    fun getVideos(): Flow<List<MediaVideo>>
    suspend fun getVideo(id: Long): MediaVideo?
    suspend fun search(query: String): List<MediaVideo>
    suspend fun getRecent(limit: Int): List<MediaVideo>
}
