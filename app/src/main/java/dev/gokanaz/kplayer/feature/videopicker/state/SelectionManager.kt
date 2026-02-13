package dev.gokanaz.kplayer.feature.videopicker.state

import android.content.Context
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.gokanaz.kplayer.core.domain.usecase.DeleteVideosUseCase
import dev.gokanaz.kplayer.core.domain.usecase.ShareVideosUseCase
import dev.gokanaz.kplayer.core.model.Video
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for handling selection state of items
 */
@Stable
class SelectionManager(
    private val deleteVideosUseCase: DeleteVideosUseCase,
    private val shareVideosUseCase: ShareVideosUseCase,
    private val context: Context
) {
    
    private val _selectedItems = MutableStateFlow<Set<String>>(emptySet())
    val selectedItems: StateFlow<Set<String>> = _selectedItems.asStateFlow()
    
    private val _selectionMode = MutableStateFlow(false)
    val selectionMode: StateFlow<Boolean> = _selectionMode.asStateFlow()
    
    private val _selectable = MutableStateFlow(true)
    val selectable: StateFlow<Boolean> = _selectable.asStateFlow()
    
    private val itemMap = mutableStateMapOf<String, Any>()
    
    val selectedCount: Int
        get() = _selectedItems.value.size
    
    val hasSelection: Boolean
        get() = _selectedItems.value.isNotEmpty()
    
    /**
     * Toggle selection of an item
     */
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
    
    /**
     * Select a single item
     */
    fun selectItem(itemId: String) {
        if (!_selectable.value) return
        
        val current = _selectedItems.value.toMutableSet()
        current.add(itemId)
        _selectedItems.value = current
        _selectionMode.value = true
    }
    
    /**
     * Deselect a single item
     */
    fun deselectItem(itemId: String) {
        val current = _selectedItems.value.toMutableSet()
        current.remove(itemId)
        _selectedItems.value = current
        
        if (_selectedItems.value.isEmpty()) {
            _selectionMode.value = false
        }
    }
    
    /**
     * Select all items
     */
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
        
        // Store items for later use
        items.forEach { item ->
            when (item) {
                is Video -> itemMap[item.id] = item
            }
        }
    }
    
    /**
     * Deselect all items
     */
    fun deselectAll() {
        _selectedItems.value = emptySet()
        _selectionMode.value = false
        itemMap.clear()
    }
    
    /**
     * Check if an item is selected
     */
    fun isSelected(itemId: String): Boolean {
        return _selectedItems.value.contains(itemId)
    }
    
    /**
     * Get selected items as list
     */
    fun getSelectedItems(): List<Any> {
        return _selectedItems.value.mapNotNull { id ->
            itemMap[id]
        }
    }
    
    /**
     * Get selected videos
     */
    fun getSelectedVideos(): List<Video> {
        return getSelectedItems().filterIsInstance<Video>()
    }
    
    /**
     * Enter selection mode
     */
    fun enterSelectionMode() {
        _selectionMode.value = true
    }
    
    /**
     * Exit selection mode
     */
    fun exitSelectionMode() {
        _selectionMode.value = false
        deselectAll()
    }
    
    /**
     * Clear selection
     */
    fun clearSelection() {
        deselectAll()
    }
    
    /**
     * Set selectable state
     */
    fun setSelectable(selectable: Boolean) {
        _selectable.value = selectable
        if (!selectable) {
            deselectAll()
        }
    }
    
    /**
     * Play selected videos
     */
    fun playSelected() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            // Implementation would navigate to player with playlist
        }
    }
    
    /**
     * Add selected videos to playlist
     */
    fun addToPlaylist(playlistId: String) {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            // Implementation would add to playlist
        }
    }
    
    /**
     * Share selected videos
     */
    fun shareSelected() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    shareVideosUseCase(videos, context)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
    
    /**
     * Share a single item
     */
    fun shareItem(video: Video) {
        viewModelScope.launch {
            try {
                shareVideosUseCase(listOf(video), context)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    /**
     * Delete selected videos
     */
    fun deleteSelected(onConfirm: () -> Unit = {}) {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    deleteVideosUseCase(videos.map { it.id })
                    deselectAll()
                    onConfirm()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
    
    /**
     * Delete a single item
     */
    fun deleteItem(itemId: String, video: Video) {
        viewModelScope.launch {
            try {
                deleteVideosUseCase(listOf(itemId))
                deselectItem(itemId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    
    /**
     * Add selected to favorites
     */
    fun addToFavorites() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            // Implementation would add to favorites
        }
    }
    
    /**
     * Remove selected from favorites
     */
    fun removeFromFavorites() {
        val videos = getSelectedVideos()
        if (videos.isNotEmpty()) {
            // Implementation would remove from favorites
        }
    }
    
    /**
     * Observe selection changes
     */
    fun observeSelection(): Flow<Set<String>> = selectedItems
    
    /**
     * Observe selection mode changes
     */
    fun observeSelectionMode(): Flow<Boolean> = selectionMode
    
    /**
     * Get selected items count as flow
     */
    val selectedCountFlow: Flow<Int> = selectedItems.map { it.size }
    
    /**
     * Check if any items are selected
     */
    val hasSelectionFlow: Flow<Boolean> = selectedItems.map { it.isNotEmpty() }
    
    /**
     * Toggle all items
     */
    fun toggleAll(items: List<Any>) {
        if (areAllSelected(items)) {
            deselectAll()
        } else {
            selectAll(items)
        }
    }
    
    /**
     * Check if all items are selected
     */
    fun areAllSelected(items: List<Any>): Boolean {
        val itemIds = items.mapNotNull { item ->
            when (item) {
                is Video -> item.id
                else -> null
            }
        }
        return itemIds.isNotEmpty() && _selectedItems.value.containsAll(itemIds)
    }
    
    /**
     * Get selection statistics
     */
    fun getSelectionStats(): SelectionStats {
        val videos = getSelectedVideos()
        return SelectionStats(
            count = videos.size,
            totalDuration = videos.sumOf { it.duration },
            totalSize = videos.sumOf { it.size }
        )
    }
    
    /**
     * ViewModel scope for coroutines
     */
    private val viewModelScope = androidx.lifecycle.viewModelScope
    
    data class SelectionStats(
        val count: Int,
        val totalDuration: Long,
        val totalSize: Long
    )
}

/**
 * Factory for creating SelectionManager instances
 */
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

/**
 * Hilt module for SelectionManager
 */
@dagger.Module
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
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

/**
 * Extension functions for SelectionManager
 */
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
