package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.data.repository.PlaylistRepository
import dev.gokanaz.kplayer.core.domain.model.Playlist
import dev.gokanaz.kplayer.core.domain.model.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSortedPlaylistUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    operator fun invoke(
        sortOption: SortOption = SortOption.NAME_ASC
    ): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists().map { playlists ->
            when (sortOption) {
                SortOption.NAME_ASC -> playlists.sortedBy { it.name.lowercase() }
                SortOption.NAME_DESC -> playlists.sortedByDescending { it.name.lowercase() }
                SortOption.DATE_ASC -> playlists.sortedBy { it.dateCreated }
                SortOption.DATE_DESC -> playlists.sortedByDescending { it.dateCreated }
                SortOption.COUNT_ASC -> playlists.sortedBy { it.mediaCount }
                SortOption.COUNT_DESC -> playlists.sortedByDescending { it.mediaCount }
                else -> playlists.sortedBy { it.name.lowercase() }
            }
        }
    }
    
    fun getPlaylistWithMedia(playlistId: String): Flow<Playlist?> {
        return playlistRepository.getPlaylistWithMedia(playlistId)
    }
    
    suspend fun createPlaylist(name: String, description: String? = null): Playlist {
        return playlistRepository.createPlaylist(name, description)
    }
    
    suspend fun addMediaToPlaylist(playlistId: String, mediaId: String): Boolean {
        return playlistRepository.addMediaToPlaylist(playlistId, mediaId)
    }
    
    suspend fun removeMediaFromPlaylist(playlistId: String, mediaId: String): Boolean {
        return playlistRepository.removeMediaFromPlaylist(playlistId, mediaId)
    }
    
    suspend fun deletePlaylist(playlistId: String): Boolean {
        return playlistRepository.deletePlaylist(playlistId)
    }
}
