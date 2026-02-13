package dev.gokanaz.kplayer.feature.videopicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.domain.usecase.*
import dev.gokanaz.kplayer.feature.videopicker.composables.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPickerViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val searchVideosUseCase: SearchVideosUseCase,
    private val getRecentVideosUseCase: GetRecentVideosUseCase,
    private val getFavoriteVideosUseCase: GetFavoriteVideosUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(VideoPickerUiState())
    val uiState: StateFlow<VideoPickerUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery = _searchQuery.asStateFlow()
        .debounce(300)
        .distinctUntilChanged()
    
    init {
        loadVideos()
        
        viewModelScope.launch {
            searchQuery.collect { query ->
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    loadVideos()
                }
            }
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { it.copy(isSearching = query.isNotEmpty()) }
    }
    
    fun toggleDisplayMode() {
        _uiState.update { 
            it.copy(displayMode = if (it.displayMode == DisplayMode.Grid) DisplayMode.List else DisplayMode.Grid)
        }
    }
    
    fun setViewMode(mode: ViewMode) {
        _uiState.update { it.copy(viewMode = mode) }
        loadVideos()
    }
    
    fun startSelection(itemId: String) {
        _uiState.update { 
            it.copy(
                isSelectionMode = true,
                selectedItems = setOf(itemId)
            )
        }
    }
    
    fun toggleItemSelection(itemId: String) {
        _uiState.update { state ->
            val newSelection = if (state.selectedItems.contains(itemId)) {
                state.selectedItems - itemId
            } else {
                state.selectedItems + itemId
            }
            
            state.copy(
                selectedItems = newSelection,
                isSelectionMode = newSelection.isNotEmpty()
            )
        }
    }
    
    fun clearSelection() {
        _uiState.update { 
            it.copy(
                isSelectionMode = false,
                selectedItems = emptySet()
            )
        }
    }
    
    fun applySortAndFilter(
        sort: SortOption,
        order: SortOrder,
        filters: FilterOptions
    ) {
        _uiState.update {
            it.copy(
                sortOption = sort,
                sortOrder = order,
                filters = filters
            )
        }
        loadVideos()
    }
    
    fun refreshVideos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            delay(1000) // Simulate network delay
            loadVideos()
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
    
    fun loadMoreVideos() {
        if (_uiState.value.isLoading || !_uiState.value.hasMore) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val currentPage = _uiState.value.currentPage
            val newItems = when (_uiState.value.viewMode) {
                ViewMode.Folders -> loadFolders(currentPage + 1)
                ViewMode.Videos -> loadAllVideos(currentPage + 1)
                ViewMode.Recent -> loadRecentVideos(currentPage + 1)
                ViewMode.Favorites -> loadFavoriteVideos(currentPage + 1)
            }
            
            _uiState.update { state ->
                state.copy(
                    items = state.items + newItems,
                    currentPage = currentPage + 1,
                    hasMore = newItems.isNotEmpty(),
                    isLoading = false
                )
            }
        }
    }
    
    private fun loadVideos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val items = when (_uiState.value.viewMode) {
                ViewMode.Folders -> loadFolders(1)
                ViewMode.Videos -> loadAllVideos(1)
                ViewMode.Recent -> loadRecentVideos(1)
                ViewMode.Favorites -> loadFavoriteVideos(1)
            }
            
            _uiState.update { state ->
                state.copy(
                    items = items,
                    currentPage = 1,
                    hasMore = items.isNotEmpty(),
                    isLoading = false
                )
            }
        }
    }
    
    private suspend fun loadFolders(page: Int): List<MediaItem> {
        // Implementation would use getFoldersUseCase
        return emptyList()
    }
    
    private suspend fun loadAllVideos(page: Int): List<MediaItem> {
        // Implementation would use getVideosUseCase with sort/filter
        return emptyList()
    }
    
    private suspend fun loadRecentVideos(page: Int): List<MediaItem> {
        // Implementation would use getRecentVideosUseCase
        return emptyList()
    }
    
    private suspend fun loadFavoriteVideos(page: Int): List<MediaItem> {
        // Implementation would use getFavoriteVideosUseCase
        return emptyList()
    }
    
    private suspend fun performSearch(query: String) {
        // Implementation would use searchVideosUseCase
    }
}

data class VideoPickerUiState(
    val items: List<MediaItem> = emptyList(),
    val viewMode: ViewMode = ViewMode.Videos,
    val displayMode: DisplayMode = DisplayMode.Grid,
    val isSelectionMode: Boolean = false,
    val selectedItems: Set<String> = emptySet(),
    val sortOption: SortOption = SortOption.Name,
    val sortOrder: SortOrder = SortOrder.Ascending,
    val filters: FilterOptions = FilterOptions(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSearching: Boolean = false,
    val currentPage: Int = 1,
    val hasMore: Boolean = true
)
