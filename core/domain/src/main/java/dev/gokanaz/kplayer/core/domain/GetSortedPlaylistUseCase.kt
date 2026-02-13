package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.media.model.MediaVideo
import javax.inject.Inject

data class Playlist(
    val id: String,
    val name: String,
    val videoCount: Int,
    val totalDuration: Long,
    val videos: List<MediaVideo>,
    val previewVideos: List<MediaVideo>,
    val createdAt: Long,
    val updatedAt: Long
)

sealed class PlaylistSortType {
    object Name : PlaylistSortType()
    object VideoCount : PlaylistSortType()
    object TotalDuration : PlaylistSortType()
    object CreatedDate : PlaylistSortType()
    object UpdatedDate : PlaylistSortType()
}

class GetSortedPlaylistUseCase @Inject constructor() {
    operator fun invoke(
        playlists: List<Playlist>,
        sortType: PlaylistSortType = PlaylistSortType.UpdatedDate,
        order: SortOrder = SortOrder.Descending
    ): List<Playlist> {
        val sorted = when (sortType) {
            PlaylistSortType.Name -> playlists.sortedBy { it.name }
            PlaylistSortType.VideoCount -> playlists.sortedBy { it.videoCount }
            PlaylistSortType.TotalDuration -> playlists.sortedBy { it.totalDuration }
            PlaylistSortType.CreatedDate -> playlists.sortedBy { it.createdAt }
            PlaylistSortType.UpdatedDate -> playlists.sortedBy { it.updatedAt }
        }
        
        return when (order) {
            SortOrder.Ascending -> sorted
            SortOrder.Descending -> sorted.reversed()
        }
    }
    
    fun createPlaylist(
        id: String,
        name: String,
        videos: List<MediaVideo>
    ): Playlist {
        val now = System.currentTimeMillis()
        val previewLimit = 3
        
        return Playlist(
            id = id,
            name = name,
            videoCount = videos.size,
            totalDuration = videos.sumOf { it.duration },
            videos = videos,
            previewVideos = videos.take(previewLimit),
            createdAt = now,
            updatedAt = now
        )
    }
    
    fun addToPlaylist(
        playlist: Playlist,
        video: MediaVideo
    ): Playlist {
        val newVideos = playlist.videos + video
        val previewLimit = 3
        
        return playlist.copy(
            videos = newVideos,
            videoCount = newVideos.size,
            totalDuration = newVideos.sumOf { it.duration },
            previewVideos = newVideos.take(previewLimit),
            updatedAt = System.currentTimeMillis()
        )
    }
    
    fun removeFromPlaylist(
        playlist: Playlist,
        videoId: Long
    ): Playlist {
        val newVideos = playlist.videos.filter { it.id != videoId }
        val previewLimit = 3
        
        return playlist.copy(
            videos = newVideos,
            videoCount = newVideos.size,
            totalDuration = newVideos.sumOf { it.duration },
            previewVideos = newVideos.take(previewLimit),
            updatedAt = System.currentTimeMillis()
        )
    }
}
