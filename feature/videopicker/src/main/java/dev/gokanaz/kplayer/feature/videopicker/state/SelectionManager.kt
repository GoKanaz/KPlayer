package dev.gokanaz.kplayer.feature.videopicker.state

import android.content.Context
import androidx.compose.runtime.Stable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.gokanaz.kplayer.core.domain.usecase.DeleteVideosUseCase
import dev.gokanaz.kplayer.core.domain.usecase.ShareVideosUseCase
import dev.gokanaz.kplayer.core.model.Video
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Stable
class SelectionManager(
    private val deleteVideosUseCase: DeleteVideosUseCase,
    private val shareVideosUseCase: ShareVideosUseCase,
    private val context: Context
) {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    
    private val _selectedItems = MutableStateFlow<Set<String>>(emptySet())
    val selectedItems: StateFlow<Set<String>> = _selectedItems.asStateFlow()
    
    private val _selectionMode = MutableStateFlow(false)
    val selectionMode: StateFlow<Boolean> = _selectionMode.asStateFlow()
    
    private val _selectable = MutableStateFlow(true)
    val selectable: StateFlow<Boolean> = _selectable.asStateFlow()
    
    private val itemMap = mutableMapOf<String, Any>()
    
    val selectedCount: Int
        get() = _selectedItems.value.size
    
    val hasSelection: Boolean
        get() = _selectedItems.value.isNotEmpty()
    
    fun toggleSelection(itemId: String) {
        if (!_selectable.value) return
        
        val current = _selectedItems.value.toMutableSet()
        if (current.contains(itemId)) {
            current.remove(itemId)
        } else {
            current.add(itemId)
        }
        _selectedItems.value = current
        
        if (_selectedItems.value.isEmpty()) {
            _selectionMode.value = false
        } else {
            _selectionMode.value = true
        }
    }
    
    fun selectItem(itemId: String) {
        if (!_selectable.value) return
        
        val current = _selectedItems.value.toMutableSet()
        current.add(itemId)
        _selectedItems.value = current
        _selectionMode.value = true
    }
    
    fun deselectItem(itemId: String) {
        val current = _selectedItems.value.toMutableSet()
        current.remove(itemId)
        _selectedItems.value = current
        
        if (_selectedItems.value.isEmpty()) {
            _selectionMode.value = false
        }
    }
    
    fun selectAll(items: List<Any>) {
        if (!_selectable.value) return
        
        val ids = items.mapNotNull { item ->
            when (item) {
                is Video -> item.id
                else -> null
            }
        }
        _selectedItems.value = ids.toSet()
        _selectionMode.value = ids.isNotEmpty()
        
        items.forEach { item ->
            when (item) {
                is Video -> itemMap[item.id] = item
            }
        }
    }
    
    fun deselectAll() {
        _selectedItems.value = emptySet()
        _selectionMode.value = false
        itemMap.clear()
    }
    
    fun isSelected(itemId: String): Boolean {
        return _selectedItems.value.contains(itemId)
    }
    
    fun getSelectedItems(): List<Any> {
        return _selectedItems.value.mapNotNull { id ->
            itemMap[id]
        }
    }
    
    fun getSelectedVideos(): List<Video> {
        return getSelectedItems().filterIsInstance<Video>()
    }
    
    fun enterSelectionMode() {
        _selectionMode.value = true
    }
    
    fun exitSelectionMode() {
        _selectionMode.value = false
        deselectAll()
    }
    
    fun clearSelection() {
        deselectAll()
    }
    
    fun setSelectable(selectable: Boolean) {
        _selectable.value = selectable
        if (!selectable) {
            deselectAll()
        }
    }
    
    fun playSelected() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
        }
    }
    
    fun addToPlaylist(playlistId: String) {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
        }
    }
    
    fun shareSelected() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            scope.launch {
                try {
                    shareVideosUseCase(videos, context)
                } catch (e: Exception) {
                }
            }
        }
    }
    
    fun shareItem(video: Video) {
        scope.launch {
            try {
                shareVideosUseCase(listOf(video), context)
            } catch (e: Exception) {
            }
        }
    }
    
    fun deleteSelected(onConfirm: () -> Unit = {}) {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            scope.launch {
                try {
                    deleteVideosUseCase(videos.map { it.id })
                    deselectAll()
                    onConfirm()
                } catch (e: Exception) {
                }
            }
        }
    }
    
    fun deleteItem(itemId: String, video: Video) {
        scope.launch {
            try {
                deleteVideosUseCase(listOf(itemId))
                deselectItem(itemId)
            } catch (e: Exception) {
            }
        }
    }
    
    fun addToFavorites() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
        }
    }
    
    fun removeFromFavorites() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
        }
    }
    
    fun observeSelection(): Flow<Set<String>> = selectedItems
    
    fun observeSelectionMode(): Flow<Boolean> = selectionMode
    
    val selectedCountFlow: Flow<Int> = selectedItems.map { it.size }
    
    val hasSelectionFlow: Flow<Boolean> = selectedItems.map { it.isNotEmpty() }
    
    fun toggleAll(items: List<Any>) {
        if (areAllSelected(items)) {
            deselectAll()
        } else {
            selectAll(items)
        }
    }
    
    fun areAllSelected(items: List<Any>): Boolean {
        val itemIds = items.mapNotNull { item ->
            when (item) {
                is Video -> item.id
                else -> null
            }
        }
        return itemIds.isNotEmpty() && _selectedItems.value.containsAll(itemIds)
    }
    
    fun getSelectionStats(): SelectionStats {
        val videos = getSelectedVideos()
        return SelectionStats(
            count = videos.size,
            totalDuration = videos.sumOf { it.duration },
            totalSize = videos.sumOf { it.size }
        )
    }
    
    data class SelectionStats(
        val count: Int,
        val totalDuration: Long,
        val totalSize: Long
    )
}

@Singleton
class SelectionManagerFactory @Inject constructor(
    private val deleteVideosUseCase: DeleteVideosUseCase,
    private val shareVideosUseCase: ShareVideosUseCase,
    @ApplicationContext private val context: Context
) {
    fun create(): SelectionManager {
        return SelectionManager(
            deleteVideosUseCase = deleteVideosUseCase,
            shareVideosUseCase = shareVideosUseCase,
            context = context
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SelectionManagerModule {
    
    @Provides
    @Singleton
    fun provideSelectionManagerFactory(
        deleteVideosUseCase: DeleteVideosUseCase,
        shareVideosUseCase: ShareVideosUseCase,
        @ApplicationContext context: Context
    ): SelectionManagerFactory {
        return SelectionManagerFactory(
            deleteVideosUseCase = deleteVideosUseCase,
            shareVideosUseCase = shareVideosUseCase,
            context = context
        )
    }
}

fun SelectionManager.getSelectedIds(): Set<String> = selectedItems.value

fun SelectionManager.isInSelectionMode(): Boolean = selectionMode.value

fun SelectionManager.canSelect(): Boolean = selectable.value

fun SelectionManager.selectItems(itemIds: Collection<String>) {
    itemIds.forEach { selectItem(it) }
}

fun SelectionManager.deselectItems(itemIds: Collection<String>) {
    itemIds.forEach { deselectItem(it) }
}

fun SelectionManager.toggleItems(itemIds: Collection<String>) {
    itemIds.forEach { toggleSelection(it) }
}

suspend fun SelectionManager.awaitSelection(): Set<String> {
    return selectedItems.first { it.isNotEmpty() }
}

fun SelectionManager.withSelection(block: (Set<String>) -> Unit) {
    if (hasSelection) {
        block(selectedItems.value)
    }
}
