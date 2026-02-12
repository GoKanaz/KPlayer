package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.data.repository.MediaRepository
import dev.gokanaz.kplayer.core.domain.model.MediaItem
import dev.gokanaz.kplayer.core.domain.model.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSortedMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(
        folderId: String? = null,
        sortOption: SortOption = SortOption.NAME_ASC,
        filterType: String? = null
    ): Flow<List<MediaItem>> {
        val mediaFlow = if (folderId == null) {
            mediaRepository.getAllMedia()
        } else {
            mediaRepository.getMediaByFolderId(folderId)
        }
        
        return mediaFlow.map { mediaItems ->
            var result = mediaItems
            
            if (filterType != null) {
                result = result.filter { it.mimeType.startsWith(filterType) }
            }
            
            when (sortOption) {
                SortOption.NAME_ASC -> result.sortedBy { it.title.lowercase() }
                SortOption.NAME_DESC -> result.sortedByDescending { it.title.lowercase() }
                SortOption.DATE_ASC -> result.sortedBy { it.dateAdded }
                SortOption.DATE_DESC -> result.sortedByDescending { it.dateAdded }
                SortOption.SIZE_ASC -> result.sortedBy { it.size }
                SortOption.SIZE_DESC -> result.sortedByDescending { it.size }
                SortOption.DURATION_ASC -> result.sortedBy { it.duration }
                SortOption.DURATION_DESC -> result.sortedByDescending { it.duration }
                SortOption.ARTIST_ASC -> result.sortedBy { it.artist ?: "" }
                SortOption.ARTIST_DESC -> result.sortedByDescending { it.artist ?: "" }
            }
        }
    }
    
    fun searchMedia(query: String): Flow<List<MediaItem>> {
        return mediaRepository.searchMedia(query)
    }
    
    fun getFavorites(): Flow<List<MediaItem>> {
        return mediaRepository.getFavoriteMedia()
    }
    
    fun getRecentlyPlayed(limit: Int = 50): Flow<List<MediaItem>> {
        return mediaRepository.getRecentlyPlayed(limit)
    }
    
    fun getMostPlayed(limit: Int = 50): Flow<List<MediaItem>> {
        return mediaRepository.getMostPlayed(limit)
    }
}
