package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.data.repository.FolderRepository
import dev.gokanaz.kplayer.core.domain.model.Folder
import dev.gokanaz.kplayer.core.domain.model.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSortedFoldersUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    operator fun invoke(
        sortOption: SortOption = SortOption.NAME_ASC,
        filterHidden: Boolean = true
    ): Flow<List<Folder>> {
        return folderRepository.getAllFolders().map { folders ->
            var result = folders
            
            if (filterHidden) {
                result = result.filter { !it.isHidden }
            }
            
            when (sortOption) {
                SortOption.NAME_ASC -> result.sortedBy { it.name.lowercase() }
                SortOption.NAME_DESC -> result.sortedByDescending { it.name.lowercase() }
                SortOption.DATE_ASC -> result.sortedBy { it.dateModified }
                SortOption.DATE_DESC -> result.sortedByDescending { it.dateModified }
                SortOption.SIZE_ASC -> result.sortedBy { it.size }
                SortOption.SIZE_DESC -> result.sortedByDescending { it.size }
                SortOption.COUNT_ASC -> result.sortedBy { it.mediaCount }
                SortOption.COUNT_DESC -> result.sortedByDescending { it.mediaCount }
            }
        }
    }
    
    fun getFoldersByParent(parentId: String?): Flow<List<Folder>> {
        return folderRepository.getFoldersByParentId(parentId)
    }
}
