package dev.gokanaz.kplayer.feature.settings.screens.medialibrary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.data.repository.MediaRepository
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MediaLibraryUiState(
    val storagePaths: List<StoragePathInfo> = emptyList(),
    val totalVideos: Int = 0,
    val totalSize: Long = 0,
    val scanOnStartup: Boolean = true,
    val scanPeriodically: Boolean = true,
    val scanInterval: String = "Daily",
    val scanOnlyOnWifi: Boolean = true,
    val scanOnlyWhenCharging: Boolean = false,
    val scanHiddenFolders: Boolean = false,
    val fileExtensions: List<String> = listOf("mp4", "mkv", "avi", "mov", "wmv", "flv", "webm", "m4v"),
    val maxScanDepth: Int = 5,
    val excludedFolders: List<String> = emptyList(),
    val showThumbnails: Boolean = true,
    val thumbnailQuality: String = "Medium",
    val thumbnailCacheSize: String = "45.2 MB",
    val metadataCacheSize: String = "12.8 MB",
    val autoSyncOnResume: Boolean = true,
    val autoSyncOnNewFiles: Boolean = true,
    val lastSyncTime: Long = System.currentTimeMillis(),
    val isSyncing: Boolean = false,
    val syncProgress: Float = 0f,
    val syncCurrentFile: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MediaLibraryPreferencesViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MediaLibraryUiState())
    val uiState: StateFlow<MediaLibraryUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
        loadStoragePaths()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesRepository.observeScanOnStartup().collect { enabled ->
                _uiState.value = _uiState.value.copy(scanOnStartup = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeScanPeriodically().collect { enabled ->
                _uiState.value = _uiState.value.copy(scanPeriodically = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeScanInterval().collect { interval ->
                _uiState.value = _uiState.value.copy(scanInterval = interval)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeScanOnlyOnWifi().collect { enabled ->
                _uiState.value = _uiState.value.copy(scanOnlyOnWifi = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeScanOnlyWhenCharging().collect { enabled ->
                _uiState.value = _uiState.value.copy(scanOnlyWhenCharging = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeScanHiddenFolders().collect { enabled ->
                _uiState.value = _uiState.value.copy(scanHiddenFolders = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeMaxScanDepth().collect { depth ->
                _uiState.value = _uiState.value.copy(maxScanDepth = depth)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeExcludedFolders().collect { folders ->
                _uiState.value = _uiState.value.copy(excludedFolders = folders)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeShowThumbnails().collect { enabled ->
                _uiState.value = _uiState.value.copy(showThumbnails = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeThumbnailQuality().collect { quality ->
                _uiState.value = _uiState.value.copy(thumbnailQuality = quality)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAutoSyncOnResume().collect { enabled ->
                _uiState.value = _uiState.value.copy(autoSyncOnResume = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAutoSyncOnNewFiles().collect { enabled ->
                _uiState.value = _uiState.value.copy(autoSyncOnNewFiles = enabled)
            }
        }
    }
    
    private fun loadStoragePaths() {
        viewModelScope.launch {
            val paths = listOf(
                StoragePathInfo(
                    path = "/storage/emulated/0/DCIM/Camera",
                    videoCount = 42,
                    totalSize = 2_500_000_000L,
                    lastScanTime = System.currentTimeMillis() - 3600000
                ),
                StoragePathInfo(
                    path = "/storage/emulated/0/Movies",
                    videoCount = 18,
                    totalSize = 15_800_000_000L,
                    lastScanTime = System.currentTimeMillis() - 86400000
                ),
                StoragePathInfo(
                    path = "/storage/emulated/0/Download",
                    videoCount = 7,
                    totalSize = 3_200_000_000L,
                    lastScanTime = System.currentTimeMillis() - 172800000
                )
            )
            
            val totalVideos = paths.sumOf { it.videoCount }
            val totalSize = paths.sumOf { it.totalSize }
            
            _uiState.value = _uiState.value.copy(
                storagePaths = paths,
                totalVideos = totalVideos,
                totalSize = totalSize
            )
        }
    }
    
    fun addStoragePath() {
        viewModelScope.launch {
        }
    }
    
    fun removeStoragePath(path: String) {
        viewModelScope.launch {
            val updatedPaths = _uiState.value.storagePaths.filter { it.path != path }
            val totalVideos = updatedPaths.sumOf { it.videoCount }
            val totalSize = updatedPaths.sumOf { it.totalSize }
            
            _uiState.value = _uiState.value.copy(
                storagePaths = updatedPaths,
                totalVideos = totalVideos,
                totalSize = totalSize
            )
        }
    }
    
    fun addExcludedFolder(pattern: String) {
        viewModelScope.launch {
            val updatedFolders = _uiState.value.excludedFolders + pattern
            _uiState.value = _uiState.value.copy(excludedFolders = updatedFolders)
            preferencesRepository.setExcludedFolders(updatedFolders)
        }
    }
    
    fun removeExcludedFolder(pattern: String) {
        viewModelScope.launch {
            val updatedFolders = _uiState.value.excludedFolders.filter { it != pattern }
            _uiState.value = _uiState.value.copy(excludedFolders = updatedFolders)
            preferencesRepository.setExcludedFolders(updatedFolders)
        }
    }
    
    fun updateScanOnStartup(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(scanOnStartup = enabled)
        viewModelScope.launch {
            preferencesRepository.setScanOnStartup(enabled)
        }
    }
    
    fun updateScanPeriodically(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(scanPeriodically = enabled)
        viewModelScope.launch {
            preferencesRepository.setScanPeriodically(enabled)
        }
    }
    
    fun updateScanInterval(interval: String) {
        _uiState.value = _uiState.value.copy(scanInterval = interval)
        viewModelScope.launch {
            preferencesRepository.setScanInterval(interval)
        }
    }
    
    fun updateScanOnlyOnWifi(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(scanOnlyOnWifi = enabled)
        viewModelScope.launch {
            preferencesRepository.setScanOnlyOnWifi(enabled)
        }
    }
    
    fun updateScanOnlyWhenCharging(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(scanOnlyWhenCharging = enabled)
        viewModelScope.launch {
            preferencesRepository.setScanOnlyWhenCharging(enabled)
        }
    }
    
    fun updateScanHiddenFolders(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(scanHiddenFolders = enabled)
        viewModelScope.launch {
            preferencesRepository.setScanHiddenFolders(enabled)
        }
    }
    
    fun updateMaxScanDepth(depth: Int) {
        _uiState.value = _uiState.value.copy(maxScanDepth = depth)
        viewModelScope.launch {
            preferencesRepository.setMaxScanDepth(depth)
        }
    }
    
    fun updateShowThumbnails(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(showThumbnails = enabled)
        viewModelScope.launch {
            preferencesRepository.setShowThumbnails(enabled)
        }
    }
    
    fun updateThumbnailQuality(quality: String) {
        _uiState.value = _uiState.value.copy(thumbnailQuality = quality)
        viewModelScope.launch {
            preferencesRepository.setThumbnailQuality(quality)
        }
    }
    
    fun updateAutoSyncOnResume(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoSyncOnResume = enabled)
        viewModelScope.launch {
            preferencesRepository.setAutoSyncOnResume(enabled)
        }
    }
    
    fun updateAutoSyncOnNewFiles(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoSyncOnNewFiles = enabled)
        viewModelScope.launch {
            preferencesRepository.setAutoSyncOnNewFiles(enabled)
        }
    }
    
    fun syncNow() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, syncProgress = 0f)
            
            mediaRepository.syncMedia()
            
            for (i in 1..10) {
                delay(300)
                _uiState.value = _uiState.value.copy(
                    syncProgress = i * 0.1f,
                    syncCurrentFile = "Scanning file $i of 10..."
                )
            }
            
            _uiState.value = _uiState.value.copy(
                isSyncing = false,
                lastSyncTime = System.currentTimeMillis()
            )
            
            loadStoragePaths()
        }
    }
    
    fun clearThumbnailCache() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(thumbnailCacheSize = "0 B")
        }
    }
    
    fun clearMetadataCache() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(metadataCacheSize = "0 B")
        }
    }
    
    fun clearAllMediaData() {
        viewModelScope.launch {
            mediaRepository.clearCache()
            
            _uiState.value = _uiState.value.copy(
                thumbnailCacheSize = "0 B",
                metadataCacheSize = "0 B",
                storagePaths = emptyList(),
                totalVideos = 0,
                totalSize = 0,
                excludedFolders = emptyList()
            )
        }
    }
}
