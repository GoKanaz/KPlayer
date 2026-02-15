package dev.gokanaz.kplayer.core.data.repository

import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.FolderNode
import dev.gokanaz.kplayer.core.model.FolderSortType
import dev.gokanaz.kplayer.core.model.Playlist
import dev.gokanaz.kplayer.core.model.Result
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.Video
import dev.gokanaz.kplayer.core.model.VideoFilter
import dev.gokanaz.kplayer.core.model.media.VideoStreamInfo
import dev.gokanaz.kplayer.core.model.media.AudioStreamInfo
import dev.gokanaz.kplayer.core.model.media.SubtitleStreamInfo
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getVideos(
        sortType: SortType = SortType.DATE,
        sortOrder: SortOrder = SortOrder.DESCENDING,
        filter: VideoFilter? = null
    ): Flow<Result<List<Video>>>
    
    suspend fun getVideoById(id: String): Result<Video>
    
    suspend fun searchVideos(query: String): Result<List<Video>>
    
    suspend fun getRecentVideos(limit: Int): Result<List<Video>>
    
    fun getFolders(sortType: FolderSortType = FolderSortType.NAME): Flow<Result<List<Folder>>>
    
    suspend fun getFolderTree(): Result<FolderNode>
    
    suspend fun getVideosInFolder(folderId: String): Result<List<Video>>
    
    fun getPlaylists(): Flow<Result<List<Playlist>>>
    
    suspend fun createPlaylist(name: String, videoIds: List<String> = emptyList()): Result<Playlist>
    
    suspend fun addToPlaylist(playlistId: String, videoId: String): Result<Playlist>
    
    suspend fun removeFromPlaylist(playlistId: String, videoId: String): Result<Playlist>
    
    suspend fun deletePlaylist(playlistId: String): Result<Unit>
    
    suspend fun updateVideoState(videoId: String, positionMs: Long, isFavorite: Boolean? = null): Result<Unit>
    
    suspend fun getVideoState(videoId: String): Result<dev.gokanaz.kplayer.core.data.models.VideoState>
    
    suspend fun toggleFavorite(videoId: String): Result<Boolean>
    
    suspend fun getFavoriteVideos(): Result<List<Video>>
    
    suspend fun syncMedia(): Result<Unit>
    
    suspend fun syncMediaInfo(videoId: String): Result<Unit>
    
    suspend fun getVideoStreamInfo(videoId: String): Result<VideoStreamInfo>
    
    suspend fun getAudioStreamInfo(videoId: String): Result<List<AudioStreamInfo>>
    
    suspend fun getSubtitleStreamInfo(videoId: String): Result<List<SubtitleStreamInfo>>
    
    suspend fun clearCache()
}
