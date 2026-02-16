package dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.domain.usecase.*
import dev.gokanaz.kplayer.core.model.*
import dev.gokanaz.kplayer.feature.videopicker.screens.MediaItem
import dev.gokanaz.kplayer.feature.videopicker.screens.MediaState
import dev.gokanaz.kplayer.feature.videopicker.state.SelectionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

val Video.folderId: String
    get() = bucketId

@HiltViewModel
class MediaPickerViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val getFoldersUseCase: GetFoldersUseCase,
    private val searchVideosUseCase: SearchVideosUseCase,
    private val getRecentVideosUseCase: GetRecentVideosUseCase,
    private val getFavoriteVideosUseCase: GetFavoriteVideosUseCase,
    private val deleteVideosUseCase: DeleteVideosUseCase,
    private val shareVideosUseCase: ShareVideosUseCase,
    val selectionManager: SelectionManager
) : ViewModel() {

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Loading)
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    private val _viewMode = MutableStateFlow(MediaViewMode.VIDEOS)
    val viewMode: StateFlow<MediaViewMode> = _viewMode.asStateFlow()

    private val _layoutMode = MutableStateFlow(MediaLayoutMode.GRID)
    val layoutMode: StateFlow<MediaLayoutMode> = _layoutMode.asStateFlow()

    private val _sortOption = MutableStateFlow(SortType.DATE)
    val sortOption: StateFlow<SortType> = _sortOption.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DESCENDING)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _filterOptions = MutableStateFlow(FilterOptions())
    val filterOptions: StateFlow<FilterOptions> = _filterOptions.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentFolder = MutableStateFlow<Folder?>(null)
    val currentFolder: StateFlow<Folder?> = _currentFolder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val searchQueryDebounced = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .filter { it.isNotEmpty() }

    init {
        loadMedia()

        viewModelScope.launch {
            searchQueryDebounced.collect { query ->
                performSearch(query)
            }
        }

        viewModelScope.launch {
            combine(
                _viewMode,
                _sortOption,
                _sortOrder,
                _filterOptions,
                _currentFolder
            ) { _, _, _, _, _ ->
                Unit
            }.debounce(300)
                .collect {
                    loadMedia()
                }
        }
    }

    fun loadMedia() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val items = when (_viewMode.value) {
                    MediaViewMode.FOLDERS -> loadFolders()
                    MediaViewMode.VIDEOS -> loadVideos()
                    MediaViewMode.RECENT -> loadRecentVideos()
                    MediaViewMode.FAVORITES -> loadFavoriteVideos()
                }

                _mediaState.value = if (items.isEmpty()) {
                    MediaState.Empty
                } else {
                    MediaState.Success(items)
                }
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(
                    message = e.message ?: "Unknown error occurred",
                    retryAction = { loadMedia() }
                )
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadVideosInFolder(folderId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val videos = emptyList<Video>()
                _mediaState.value = if (videos.isEmpty()) {
                    MediaState.Empty
                } else {
                    MediaState.Success(videos.map { MediaItem.VideoItem(it) })
                }
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(
                    message = e.message ?: "Failed to load videos in folder",
                    retryAction = { loadVideosInFolder(folderId) }
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadFolders(): List<MediaItem> {
        val folders = getFoldersUseCase(
            sortType = _sortOption.value,
            sortOrder = _sortOrder.value
        )
        return folders.map { MediaItem.FolderItem(it) }
    }

    private suspend fun loadVideos(): List<MediaItem> {
        val videos = getVideosUseCase(
            sortType = _sortOption.value,
            sortOrder = _sortOrder.value
        )
        return applyFilterAndSort(videos)
            .map { MediaItem.VideoItem(it) }
    }

    private suspend fun loadRecentVideos(): List<MediaItem> {
        val videos = getRecentVideosUseCase(
            limit = 50,
            sortOrder = SortOrder.DESCENDING
        )
        return videos.map { MediaItem.VideoItem(it) }
    }

    private suspend fun loadFavoriteVideos(): List<MediaItem> {
        val videos = getFavoriteVideosUseCase(
            sortType = _sortOption.value,
            sortOrder = _sortOrder.value
        )
        return videos.map { MediaItem.VideoItem(it) }
    }

    private suspend fun performSearch(query: String) {
        _isLoading.value = true

        try {
            val results = searchVideosUseCase(query)
            _mediaState.value = if (results.isEmpty()) {
                MediaState.Empty
            } else {
                MediaState.Success(results.map { MediaItem.VideoItem(it) })
            }
        } catch (e: Exception) {
            _mediaState.value = MediaState.Error(
                message = "Search failed: ${e.message}",
                retryAction = { viewModelScope.launch { performSearch(query) } }
            )
        } finally {
            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            loadMedia()
        }
    }

    fun toggleLayoutMode() {
        _layoutMode.value = when (_layoutMode.value) {
            MediaLayoutMode.GRID -> MediaLayoutMode.LIST
            MediaLayoutMode.LIST -> MediaLayoutMode.GRID
        }
    }

    fun setViewMode(mode: MediaViewMode) {
        _viewMode.value = mode
        selectionManager.clearSelection()
        loadMedia()
    }

    fun updateSort(sortType: SortType, sortOrder: SortOrder) {
        _sortOption.value = sortType
        _sortOrder.value = sortOrder
    }

    fun updateFilter(filter: FilterOptions) {
        _filterOptions.value = filter
    }

    fun refresh() {
        selectionManager.clearSelection()
        loadMedia()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun showVideoInfo(video: Video) {
    }

    private fun applyFilterAndSort(videos: List<Video>): List<Video> {
        return videos
            .filter { video ->
                val filter = _filterOptions.value

                val durationMatch = when (filter.durationFilter) {
                    DurationFilter.All -> true
                    DurationFilter.LessThan5Min -> video.duration < 300
                    DurationFilter.Between5And15Min -> video.duration in 300..900
                    DurationFilter.Between15And30Min -> video.duration in 901..1800
                    DurationFilter.Between30And60Min -> video.duration in 1801..3600
                    DurationFilter.MoreThan60Min -> video.duration > 3600
                }

                val resolutionMatch = when (filter.resolutionFilter) {
                    ResolutionFilter.All -> true
                    ResolutionFilter.Resolution480p -> video.height <= 480
                    ResolutionFilter.Resolution720p -> video.height in 481..720
                    ResolutionFilter.Resolution1080p -> video.height in 721..1080
                    ResolutionFilter.Resolution4K -> video.height > 1080
                }

                val dateMatch = when (filter.dateFilter) {
                    DateFilter.All -> true
                    DateFilter.Today -> isToday(video.dateAdded)
                    DateFilter.ThisWeek -> isThisWeek(video.dateAdded)
                    DateFilter.ThisMonth -> isThisMonth(video.dateAdded)
                    DateFilter.ThisYear -> isThisYear(video.dateAdded)
                    DateFilter.Custom -> true
                }

                val folderMatch = if (filter.selectedFolders.isEmpty()) {
                    true
                } else {
                    video.bucketId in filter.selectedFolders
                }

                durationMatch && resolutionMatch && dateMatch && folderMatch
            }
            .sortedWith(
                when (_sortOption.value) {
                    SortType.NAME -> compareBy<Video> { it.title }
                    SortType.DATE -> compareBy<Video> { it.dateAdded }
                    SortType.SIZE -> compareBy<Video> { it.size }
                    SortType.DURATION -> compareBy<Video> { it.duration }
                    SortType.RESOLUTION -> compareBy<Video> { it.height * it.width }
                    SortType.TYPE -> compareBy<Video> { it.mimeType }
                    SortType.ARTIST -> compareBy<Video> { it.bucketDisplayName }
                    SortType.ALBUM -> compareBy<Video> { it.fileName }
                }.let { comparator: Comparator<Video> ->
                    if (_sortOrder.value == SortOrder.DESCENDING) {
                        comparator.reversed()
                    } else {
                        comparator
                    }
                }
            )
    }

    private fun isToday(timestamp: Long): Boolean {
        val calendar = java.util.Calendar.getInstance()
        val today = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(java.util.Calendar.YEAR) == today.get(java.util.Calendar.YEAR) &&
                calendar.get(java.util.Calendar.DAY_OF_YEAR) == today.get(java.util.Calendar.DAY_OF_YEAR)
    }

    private fun isThisWeek(timestamp: Long): Boolean {
        val calendar = java.util.Calendar.getInstance()
        val today = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(java.util.Calendar.YEAR) == today.get(java.util.Calendar.YEAR) &&
                calendar.get(java.util.Calendar.WEEK_OF_YEAR) == today.get(java.util.Calendar.WEEK_OF_YEAR)
    }

    private fun isThisMonth(timestamp: Long): Boolean {
        val calendar = java.util.Calendar.getInstance()
        val today = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(java.util.Calendar.YEAR) == today.get(java.util.Calendar.YEAR) &&
                calendar.get(java.util.Calendar.MONTH) == today.get(java.util.Calendar.MONTH)
    }

    private fun isThisYear(timestamp: Long): Boolean {
        val calendar = java.util.Calendar.getInstance()
        val today = java.util.Calendar.getInstance()
        calendar.timeInMillis = timestamp
        return calendar.get(java.util.Calendar.YEAR) == today.get(java.util.Calendar.YEAR)
    }
}

data class FilterOptions(
    val durationFilter: DurationFilter = DurationFilter.All,
    val resolutionFilter: ResolutionFilter = ResolutionFilter.All,
    val dateFilter: DateFilter = DateFilter.All,
    val selectedFolders: Set<String> = emptySet()
)

enum class DurationFilter {
    All, LessThan5Min, Between5And15Min, Between15And30Min, Between30And60Min, MoreThan60Min
}

enum class ResolutionFilter {
    All, Resolution480p, Resolution720p, Resolution1080p, Resolution4K
}

enum class DateFilter {
    All, Today, ThisWeek, ThisMonth, ThisYear, Custom
}
