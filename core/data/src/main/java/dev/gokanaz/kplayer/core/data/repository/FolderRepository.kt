package dev.gokanaz.kplayer.core.data.repository

import dev.gokanaz.kplayer.core.domain.model.Folder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository @Inject constructor() {
    
    fun getAllFolders(): Flow<List<Folder>> {
        return flowOf(emptyList())
    }
    
    fun getFoldersByParentId(parentId: String?): Flow<List<Folder>> {
        return flowOf(emptyList())
    }
    
    suspend fun getFolderById(folderId: String): Folder? {
        return null
    }
    
    suspend fun createFolder(name: String, path: String, parentId: String? = null): Folder {
        return Folder(
            id = "folder_${System.currentTimeMillis()}",
            name = name,
            path = path,
            parentId = parentId
        )
    }
    
    suspend fun updateFolder(folder: Folder): Boolean {
        return true
    }
    
    suspend fun deleteFolder(folderId: String): Boolean {
        return true
    }
}
