package dev.gokanaz.kplayer.core.data.repository

import dev.gokanaz.kplayer.core.data.mappers.toFolderTree
import dev.gokanaz.kplayer.core.data.mappers.toFolders
import dev.gokanaz.kplayer.core.data.mappers.toVideo
import dev.gokanaz.kplayer.core.data.mappers.toVideoState
import dev.gokanaz.kplayer.core.data.mappers.toVideos
import dev.gokanaz.kplayer.core.data.mappers.toVideoStreamInfo
import dev.gokanaz.kplayer.core.data.mappers.toAudioStreamInfo
import dev.gokanaz.kplayer.core.data.mappers.toSubtitleStreamInfo
import dev.gokanaz.kplayer.core.data.models.VideoState
import dev.gokanaz.kplayer.core.datastore.datasource.PlayerPreferencesDataSource
import dev.gokanaz.kplayer.core.model.Folder
import dev.gokanaz.kplayer.core.model.FolderNode
import dev.gokanaz.kplayer.core.model.FolderSortType
import dev.gokanaz.kplayer.core.model.Playlist
import dev.gokanaz.kplayer.core.model.Result
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.Video
import dev.gokanaz.kplayer.core.model.VideoFilter
import dev.gokanaz.kplayer.core.model.VideoStreamInfo
import dev.gokanaz.kplayer.core.model.media.AudioStreamInfo
import dev.gokanaz.kplayer.core.model.media.SubtitleStreamInfo
import dev.gokanaz.kplayer.core.media.service.MediaService
import dev.gokanaz.kplayer.core.media.sync.LocalMediaInfoSynchronizer
import dev.gokanaz.kplayer.core.media.sync.LocalMediaSynchronizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import android.media.MediaMetadataRetriever

@Singleton
class LocalMediaRepository @Inject constructor(
    private val mediaService: MediaService,
    private val mediaSynchronizer: LocalMediaSynchronizer,
    private val mediaInfoSynchronizer: LocalMediaInfoSynchronizer,
    private val playerPreferences: PlayerPreferencesDataSource
) : MediaRepository {
    
    private val videoCache = ConcurrentHashMap<String, Video>()
    private val stateCache = ConcurrentHashMap<String, VideoState>()
    private var lastCacheUpdate = 0L
    private val cacheTimeout = 300000L
    
    override fun getVideos(
        sortType: SortType,
        sortOrder: SortOrder,
        filter: VideoFilter?
    ): Flow<Result<List<Video>>> = flow {
        emit(Result.Loading)
        
        try {
            val resumePositions = playerPreferences.observeResumePositions().first()
            val stateMap = resumePositions.mapKeys { it.key }
                .mapValues { it.value.toVideoState() }
            
            val videosFlow = mediaService.getVideos()
                .catch { e -> emit(Result.Error(e)) }
                .map { mediaVideos ->
                    val videos = if (videoCache.isEmpty() || isCacheExpired()) {
                        val newVideos = mediaVideos.toVideos(stateMap)
                        updateCache(newVideos)
                        newVideos
                    } else {
                        videoCache.values.toList()
                    }
                    
                    val filtered = applyFilter(videos, filter)
                    val sorted = applySorting(filtered, sortType, sortOrder)
                    Result.Success(sorted)
                }
            
            combine(videosFlow, playerPreferences.observeResumePositions()) { result, _ ->
                result
            }.collect { result ->
                emit(result)
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    override suspend fun getVideoById(id: String): Result<Video> = withContext(Dispatchers.IO) {
        try {
            val cachedVideo = videoCache[id]
            if (cachedVideo != null) {
                return@withContext Result.Success(cachedVideo)
            }
            
            val videoId = id.toLongOrNull()
            if (videoId == null) {
                return@withContext Result.Error(IllegalArgumentException("Invalid video ID"))
            }
            
            val mediaVideo = mediaService.getVideo(videoId)
            if (mediaVideo == null) {
                return@withContext Result.Error(NoSuchElementException("Video not found"))
            }
            
            val resumePosition = playerPreferences.getResumePosition(id)
            val videoState = VideoState(
                videoId = id,
                lastPlayedPositionMs = resumePosition ?: 0
            )
            
            val video = mediaVideo.toVideo(videoState)
            videoCache[id] = video
            stateCache[id] = videoState
            
            Result.Success(video)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun searchVideos(query: String): Result<List<Video>> = withContext(Dispatchers.IO) {
        try {
            val mediaVideos = mediaService.search(query)
            val resumePositions = playerPreferences.observeResumePositions().first()
            val stateMap = resumePositions.mapKeys { it.key }
                .mapValues { it.value.toVideoState() }
            
            val videos = mediaVideos.toVideos(stateMap)
            Result.Success(videos)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getRecentVideos(limit: Int): Result<List<Video>> = withContext(Dispatchers.IO) {
        try {
            val mediaVideos = mediaService.getRecent(limit)
            val resumePositions = playerPreferences.observeResumePositions().first()
            val stateMap = resumePositions.mapKeys { it.key }
                .mapValues { it.value.toVideoState() }
            
            val videos = mediaVideos.toVideos(stateMap)
            Result.Success(videos)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getFolders(sortType: FolderSortType): Flow<Result<List<Folder>>> = flow {
        emit(Result.Loading)
        
        try {
            val videosResult = getVideos().first()
            
            when (videosResult) {
                is Result.Success -> {
                    val folders = videosResult.data.toFolders(sortType)
                    emit(Result.Success(folders))
                }
                is Result.Error -> emit(videosResult)
                Result.Loading -> {}
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
    
    override suspend fun getFolderTree(): Result<FolderNode> = withContext(Dispatchers.IO) {
        try {
            val videosResult = getVideos().first()
            
            when (videosResult) {
                is Result.Success -> {
                    val folderMap = videosResult.data.groupBy { it.bucketId }
                    val tree = folderMap.toFolderTree()
                    Result.Success(tree)
                }
                is Result.Error -> videosResult
                Result.Loading -> Result.Error(IllegalStateException("Still loading"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getVideosInFolder(folderId: String): Result<List<Video>> = withContext(Dispatchers.IO) {
        try {
            val filter = VideoFilter(bucketId = folderId)
            val videosResult = getVideos(filter = filter).first()
            
            when (videosResult) {
                is Result.Success -> Result.Success(videosResult.data)
                is Result.Error -> videosResult
                Result.Loading -> Result.Error(IllegalStateException("Still loading"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getPlaylists(): Flow<Result<List<Playlist>>> = flow {
        emit(Result.Success(emptyList()))
    }
    
    override suspend fun createPlaylist(name: String, videoIds: List<String>): Result<Playlist> = withContext(Dispatchers.IO) {
        Result.Error(UnsupportedOperationException("Playlist creation not implemented"))
    }
    
    override suspend fun addToPlaylist(playlistId: String, videoId: String): Result<Playlist> = withContext(Dispatchers.IO) {
        Result.Error(UnsupportedOperationException("Playlist modification not implemented"))
    }
    
    override suspend fun removeFromPlaylist(playlistId: String, videoId: String): Result<Playlist> = withContext(Dispatchers.IO) {
        Result.Error(UnsupportedOperationException("Playlist modification not implemented"))
    }
    
    override suspend fun deletePlaylist(playlistId: String): Result<Unit> = withContext(Dispatchers.IO) {
        Result.Error(UnsupportedOperationException("Playlist deletion not implemented"))
    }
    
    override suspend fun updateVideoState(videoId: String, positionMs: Long, isFavorite: Boolean?): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            playerPreferences.saveResumePosition(videoId, positionMs)
            
            isFavorite?.let { favorite ->
                val currentState = stateCache[videoId] ?: VideoState(videoId = videoId)
                val updatedState = currentState.copy(
                    isFavorite = favorite,
                    lastPlayedPositionMs = positionMs,
                    lastPlayedAt = System.currentTimeMillis(),
                    watchCount = currentState.watchCount + 1
                )
                stateCache[videoId] = updatedState
                
                videoCache[videoId]?.let { video ->
                    videoCache[videoId] = video.copy(
                        isFavorite = favorite,
                        lastPlayedPosition = positionMs,
                        lastPlayedAt = System.currentTimeMillis(),
                        watchCount = updatedState.watchCount
                    )
                }
            }
            
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getVideoState(videoId: String): Result<VideoState> = withContext(Dispatchers.IO) {
        try {
            val state = stateCache[videoId] ?: VideoState(videoId = videoId)
            Result.Success(state)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun toggleFavorite(videoId: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val currentState = stateCache[videoId] ?: VideoState(videoId = videoId)
            val newFavorite = !currentState.isFavorite
            updateVideoState(videoId, currentState.lastPlayedPositionMs, newFavorite)
            Result.Success(newFavorite)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getFavoriteVideos(): Result<List<Video>> = withContext(Dispatchers.IO) {
        try {
            val favoriteVideos = videoCache.values.filter { it.isFavorite }.toList()
            Result.Success(favoriteVideos)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun syncMedia(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val syncResult = mediaSynchronizer.sync()
            if (syncResult.isSuccess) {
                clearCache()
                Result.Success(Unit)
            } else {
                Result.Error(syncResult.exceptionOrNull() ?: Exception("Sync failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun syncMediaInfo(videoId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val videoIdLong = videoId.toLongOrNull()
            if (videoIdLong == null) {
                return@withContext Result.Error(IllegalArgumentException("Invalid video ID"))
            }
            
            val syncResult = mediaInfoSynchronizer.syncInfo(videoIdLong)
            if (syncResult.isSuccess) {
                Result.Success(Unit)
            } else {
                Result.Error(syncResult.exceptionOrNull() ?: Exception("Sync info failed"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getVideoStreamInfo(videoId: String): Result<VideoStreamInfo> = withContext(Dispatchers.IO) {
        try {
            val video = videoCache[videoId] ?: return@withContext Result.Error(NoSuchElementException("Video not found"))
            
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(video.uri.toString(), HashMap())
            
            val streamInfo = retriever.toVideoStreamInfo()
            retriever.release()
            
            if (streamInfo != null) {
                Result.Success(streamInfo)
            } else {
                Result.Error(Exception("Failed to extract video stream info"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getAudioStreamInfo(videoId: String): Result<List<AudioStreamInfo>> = withContext(Dispatchers.IO) {
        try {
            val video = videoCache[videoId] ?: return@withContext Result.Error(NoSuchElementException("Video not found"))
            
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(video.uri.toString(), HashMap())
            
            val streamInfo = retriever.toAudioStreamInfo()
            retriever.release()
            
            Result.Success(listOfNotNull(streamInfo))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getSubtitleStreamInfo(videoId: String): Result<List<SubtitleStreamInfo>> = withContext(Dispatchers.IO) {
        Result.Success(emptyList())
    }
    
    override suspend fun clearCache() {
        videoCache.clear()
        stateCache.clear()
        lastCacheUpdate = System.currentTimeMillis()
    }
    
    private fun applyFilter(videos: List<Video>, filter: VideoFilter?): List<Video> {
        if (filter == null) return videos
        
        return videos.filter { video ->
            var matches = true
            
            filter.bucketId?.let {
                matches = matches && video.bucketId == it
            }
            
            filter.minDuration?.let {
                matches = matches && video.duration >= it
            }
            
            filter.maxDuration?.let {
                matches = matches && video.duration <= it
            }
            
            filter.minSize?.let {
                matches = matches && video.size >= it
            }
            
            filter.maxSize?.let {
                matches = matches && video.size <= it
            }
            
            filter.startDate?.let {
                matches = matches && video.dateAdded >= it
            }
            
            filter.endDate?.let {
                matches = matches && video.dateAdded <= it
            }
            
            matches
        }
    }
    
    private fun applySorting(videos: List<Video>, sortType: SortType, sortOrder: SortOrder): List<Video> {
        val sorted = when (sortType) {
            SortType.Name -> videos.sortedBy { it.title }
            SortType.Date -> videos.sortedBy { it.dateAdded }
            SortType.Size -> videos.sortedBy { it.size }
            SortType.Duration -> videos.sortedBy { it.duration }
        }
        
        return when (sortOrder) {
            SortOrder.Ascending -> sorted
            SortOrder.Descending -> sorted.reversed()
        }
    }
    
    private fun updateCache(videos: List<Video>) {
        videos.forEach { video ->
            videoCache[video.id] = video
        }
        lastCacheUpdate = System.currentTimeMillis()
    }
    
    private fun isCacheExpired(): Boolean {
        return System.currentTimeMillis() - lastCacheUpdate > cacheTimeout
    }
}
