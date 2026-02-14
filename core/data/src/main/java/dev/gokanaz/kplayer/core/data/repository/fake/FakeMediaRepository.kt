package dev.gokanaz.kplayer.core.data.repository.fake

import android.net.Uri
import dev.gokanaz.kplayer.core.data.models.VideoState
import dev.gokanaz.kplayer.core.data.repository.MediaRepository
import dev.gokanaz.kplayer.core.domain.Folder
import dev.gokanaz.kplayer.core.domain.FolderNode
import dev.gokanaz.kplayer.core.domain.FolderSortType
import dev.gokanaz.kplayer.core.domain.Playlist
import dev.gokanaz.kplayer.core.domain.Result
import dev.gokanaz.kplayer.core.domain.SortOrder
import dev.gokanaz.kplayer.core.domain.SortType
import dev.gokanaz.kplayer.core.domain.Video
import dev.gokanaz.kplayer.core.domain.VideoFilter
import dev.gokanaz.kplayer.core.model.media.VideoQuality
import dev.gokanaz.kplayer.core.domain.VideoStreamInfo
import dev.gokanaz.kplayer.core.domain.AudioStreamInfo
import dev.gokanaz.kplayer.core.domain.SubtitleStreamInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeMediaRepository @Inject constructor() : MediaRepository {
    
    private val videosFlow = MutableStateFlow<List<Video>>(emptyList())
    private val playlistsFlow = MutableStateFlow<List<Playlist>>(emptyList())
    private val videoStates = mutableMapOf<String, VideoState>()
    
    private var shouldFail = false
    
    init {
        populateDummyData()
    }
    
    fun setShouldFail(fail: Boolean) {
        shouldFail = fail
    }
    
    override fun getVideos(
        sortType: SortType,
        sortOrder: SortOrder,
        filter: VideoFilter?
    ): Flow<Result<List<Video>>> = flow {
        if (shouldFail) {
            emit(Result.Error(Exception("Fake repository error")))
            return@flow
        }
        
        emit(Result.Loading)
        delay(100)
        
        val videos = applyFilter(videosFlow.value, filter)
        val sorted = applySorting(videos, sortType, sortOrder)
        emit(Result.Success(sorted))
    }
    
    override suspend fun getVideoById(id: String): Result<Video> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val video = videosFlow.value.find { it.id == id }
        return if (video != null) {
            Result.Success(video)
        } else {
            Result.Error(NoSuchElementException("Video not found"))
        }
    }
    
    override suspend fun searchVideos(query: String): Result<List<Video>> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val results = videosFlow.value.filter { 
            it.title.contains(query, ignoreCase = true) 
        }
        return Result.Success(results)
    }
    
    override suspend fun getRecentVideos(limit: Int): Result<List<Video>> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val recent = videosFlow.value.sortedByDescending { it.dateAdded }.take(limit)
        return Result.Success(recent)
    }
    
    override fun getFolders(sortType: FolderSortType): Flow<Result<List<Folder>>> = flow {
        if (shouldFail) {
            emit(Result.Error(Exception("Fake repository error")))
            return@flow
        }
        
        emit(Result.Loading)
        delay(100)
        
        val folderMap = videosFlow.value.groupBy { it.bucketId }
        val folders = folderMap.map { (bucketId, videos) ->
            Folder(
                id = bucketId,
                name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
                path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
                videoCount = videos.size,
                totalDuration = videos.sumOf { it.duration },
                totalSize = videos.sumOf { it.size },
                thumbnailUri = videos.firstOrNull()?.uri,
                videos = videos,
                subFolders = emptyList()
            )
        }.sortedBy { it.name }
        
        emit(Result.Success(folders))
    }
    
    override suspend fun getFolderTree(): Result<FolderNode> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val folderMap = videosFlow.value.groupBy { it.bucketId }
        val root = FolderNode(
            id = "root",
            name = "Root",
            path = "/",
            children = folderMap.map { (bucketId, videos) ->
                FolderNode(
                    id = bucketId,
                    name = videos.firstOrNull()?.bucketDisplayName ?: "Unknown",
                    path = "/${videos.firstOrNull()?.bucketDisplayName ?: "Unknown"}",
                    children = emptyList(),
                    videoCount = videos.size,
                    isExpanded = false
                )
            },
            videoCount = videosFlow.value.size,
            isExpanded = true
        )
        
        return Result.Success(root)
    }
    
    override suspend fun getVideosInFolder(folderId: String): Result<List<Video>> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val videos = videosFlow.value.filter { it.bucketId == folderId }
        return Result.Success(videos)
    }
    
    override fun getPlaylists(): Flow<Result<List<Playlist>>> = flow {
        emit(Result.Success(playlistsFlow.value))
    }
    
    override suspend fun createPlaylist(name: String, videoIds: List<String>): Result<Playlist> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val playlist = Playlist(
            id = System.currentTimeMillis().toString(),
            name = name,
            videoCount = videoIds.size,
            totalDuration = 0,
            videos = emptyList(),
            previewVideos = emptyList(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        val currentPlaylists = playlistsFlow.value.toMutableList()
        currentPlaylists.add(playlist)
        playlistsFlow.value = currentPlaylists
        
        return Result.Success(playlist)
    }
    
    override suspend fun addToPlaylist(playlistId: String, videoId: String): Result<Playlist> {
        return Result.Error(UnsupportedOperationException("Not implemented in fake"))
    }
    
    override suspend fun removeFromPlaylist(playlistId: String, videoId: String): Result<Playlist> {
        return Result.Error(UnsupportedOperationException("Not implemented in fake"))
    }
    
    override suspend fun deletePlaylist(playlistId: String): Result<Unit> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val currentPlaylists = playlistsFlow.value.toMutableList()
        currentPlaylists.removeAll { it.id == playlistId }
        playlistsFlow.value = currentPlaylists
        
        return Result.Success(Unit)
    }
    
    override suspend fun updateVideoState(videoId: String, positionMs: Long, isFavorite: Boolean?): Result<Unit> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val currentState = videoStates[videoId] ?: VideoState(videoId = videoId)
        val updatedState = currentState.copy(
            lastPlayedPositionMs = positionMs,
            isFavorite = isFavorite ?: currentState.isFavorite,
            lastPlayedAt = System.currentTimeMillis(),
            watchCount = currentState.watchCount + 1
        )
        videoStates[videoId] = updatedState
        
        val currentVideos = videosFlow.value.toMutableList()
        val videoIndex = currentVideos.indexOfFirst { it.id == videoId }
        if (videoIndex >= 0) {
            val video = currentVideos[videoIndex]
            currentVideos[videoIndex] = video.copy(
                lastPlayedPosition = positionMs,
                isFavorite = updatedState.isFavorite,
                lastPlayedAt = updatedState.lastPlayedAt,
                watchCount = updatedState.watchCount
            )
            videosFlow.value = currentVideos
        }
        
        return Result.Success(Unit)
    }
    
    override suspend fun getVideoState(videoId: String): Result<VideoState> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val state = videoStates[videoId] ?: VideoState(videoId = videoId)
        return Result.Success(state)
    }
    
    override suspend fun toggleFavorite(videoId: String): Result<Boolean> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val currentState = videoStates[videoId] ?: VideoState(videoId = videoId)
        val newFavorite = !currentState.isFavorite
        updateVideoState(videoId, currentState.lastPlayedPositionMs, newFavorite)
        return Result.Success(newFavorite)
    }
    
    override suspend fun getFavoriteVideos(): Result<List<Video>> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        
        val favorites = videosFlow.value.filter { it.isFavorite }
        return Result.Success(favorites)
    }
    
    override suspend fun syncMedia(): Result<Unit> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        return Result.Success(Unit)
    }
    
    override suspend fun syncMediaInfo(videoId: String): Result<Unit> {
        if (shouldFail) return Result.Error(Exception("Fake repository error"))
        return Result.Success(Unit)
    }
    
    override suspend fun getVideoStreamInfo(videoId: String): Result<VideoStreamInfo> {
        return Result.Success(
            VideoStreamInfo(
                width = 1920,
                height = 1080,
                bitrate = 5000000,
                frameRate = 30f,
                codec = "h264",
                quality = VideoQuality.P1080
            )
        )
    }
    
    override suspend fun getAudioStreamInfo(videoId: String): Result<List<AudioStreamInfo>> {
        return Result.Success(
            listOf(
                AudioStreamInfo(
                    bitrate = 128000,
                    sampleRate = 44100,
                    codec = "aac",
                    channelCount = 2,
                    language = "en"
                )
            )
        )
    }
    
    override suspend fun getSubtitleStreamInfo(videoId: String): Result<List<SubtitleStreamInfo>> {
        return Result.Success(emptyList())
    }
    
    override suspend fun clearCache() {
    }
    
    private fun populateDummyData() {
        val dummyVideos = listOf(
            Video(
                id = "1",
                title = "Sample Video 1",
                uri = Uri.parse("content://media/video/1"),
                duration = 120000,
                size = 15000000,
                dateAdded = System.currentTimeMillis() - 86400000,
                dateModified = System.currentTimeMillis() - 43200000,
                mimeType = "video/mp4",
                resolution = "1920x1080",
                bucketId = "bucket1",
                bucketDisplayName = "Camera",
                lastPlayedPosition = 30000,
                isFavorite = true,
                watchCount = 5,
                lastPlayedAt = System.currentTimeMillis() - 3600000,
                tags = setOf("vacation", "family"),
                availableQualities = listOf(VideoQuality.P360, VideoQuality.P720, VideoQuality.P1080),
                subtitleTracks = emptyList(),
                audioTracks = emptyList()
            ),
            Video(
                id = "2",
                title = "Sample Video 2",
                uri = Uri.parse("content://media/video/2"),
                duration = 180000,
                size = 22000000,
                dateAdded = System.currentTimeMillis() - 172800000,
                dateModified = System.currentTimeMillis() - 86400000,
                mimeType = "video/mp4",
                resolution = "1280x720",
                bucketId = "bucket1",
                bucketDisplayName = "Camera",
                lastPlayedPosition = 0,
                isFavorite = false,
                watchCount = 2,
                lastPlayedAt = System.currentTimeMillis() - 86400000,
                tags = setOf("tutorial"),
                availableQualities = listOf(VideoQuality.P360, VideoQuality.P720),
                subtitleTracks = emptyList(),
                audioTracks = emptyList()
            ),
            Video(
                id = "3",
                title = "Sample Video 3",
                uri = Uri.parse("content://media/video/3"),
                duration = 300000,
                size = 35000000,
                dateAdded = System.currentTimeMillis() - 259200000,
                dateModified = System.currentTimeMillis() - 172800000,
                mimeType = "video/mp4",
                resolution = "3840x2160",
                bucketId = "bucket2",
                bucketDisplayName = "Downloads",
                lastPlayedPosition = 0,
                isFavorite = true,
                watchCount = 1,
                lastPlayedAt = System.currentTimeMillis() - 172800000,
                tags = setOf("movie"),
                availableQualities = listOf(VideoQuality.P720, VideoQuality.P1080, VideoQuality.P2160),
                subtitleTracks = emptyList(),
                audioTracks = emptyList()
            )
        )
        
        videosFlow.value = dummyVideos
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
}
