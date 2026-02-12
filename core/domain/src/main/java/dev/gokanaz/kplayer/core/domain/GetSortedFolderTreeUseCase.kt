package dev.gokanaz.kplayer.core.domain

import dev.gokanaz.kplayer.core.data.repository.FolderRepository
import dev.gokanaz.kplayer.core.data.repository.MediaRepository
import dev.gokanaz.kplayer.core.domain.model.FolderTree
import dev.gokanaz.kplayer.core.domain.model.Folder
import dev.gokanaz.kplayer.core.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSortedFolderTreeUseCase @Inject constructor(
    private val folderRepository: FolderRepository,
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(rootId: String? = null): Flow<FolderTree> {
        val foldersFlow = if (rootId == null) {
            folderRepository.getAllFolders()
        } else {
            folderRepository.getFoldersByParentId(rootId)
        }
        
        val mediaFlow = if (rootId == null) {
            mediaRepository.getAllMedia()
        } else {
            mediaRepository.getMediaByFolderId(rootId)
        }
        
        return combine(foldersFlow, mediaFlow) { folders, media ->
            val folderNodes = folders.map { folder ->
                FolderTree.FolderNode(
                    folder = folder,
                    children = emptyList()
                )
            }
            
            val mediaNodes = media.map { mediaItem ->
                FolderTree.MediaNode(
                    media = mediaItem
                )
            }
            
            FolderTree(
                rootId = rootId,
                folders = folderNodes,
                mediaItems = mediaNodes
            )
        }
    }
    
    fun buildFullTree(): Flow<FolderTree> {
        return folderRepository.getAllFolders().map { folders ->
            val rootFolders = folders.filter { it.parentId == null }
            
            val folderNodes = rootFolders.map { folder ->
                buildFolderNode(folder, folders)
            }
            
            FolderTree(
                rootId = null,
                folders = folderNodes,
                mediaItems = emptyList()
            )
        }
    }
    
    private fun buildFolderNode(
        folder: Folder,
        allFolders: List<Folder>
    ): FolderTree.FolderNode {
        val children = allFolders
            .filter { it.parentId == folder.id }
            .map { buildFolderNode(it, allFolders) }
        
        return FolderTree.FolderNode(
            folder = folder,
            children = children
        )
    }
}
