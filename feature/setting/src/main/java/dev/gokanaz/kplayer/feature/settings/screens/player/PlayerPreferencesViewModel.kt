package dev.gokanaz.kplayer.feature.settings.screens.player

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import dev.gokanaz.kplayer.core.model.RepeatMode
import dev.gokanaz.kplayer.core.model.Resume
import dev.gokanaz.kplayer.core.model.VideoQuality
import dev.gokanaz.kplayer.core.model.ScreenOrientation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val defaultQuality: VideoQuality = VideoQuality.AUTO,
    val autoPlayNext: Boolean = true,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val shuffleMode: Boolean = false,
    val resumeMode: Resume = Resume.ASK,
    val resumeThreshold: Int = 30,
    val controlsTimeout: Int = 3,
    val doubleTapToSeek: Boolean = true,
    val seekDuration: Int = 10,
    val volumeGesture: Boolean = true,
    val brightnessGesture: Boolean = true,
    val seekGesture: Boolean = true,
    val pinchToZoom: Boolean = true,
    val lockControlsFullscreen: Boolean = true,
    val pipSupported: Boolean = false,
    val pipEnabled: Boolean = false,
    val autoEnterPip: Boolean = true,
    val pipButtonPosition: String = "Center",
    val pipCustomActions: Boolean = true,
    val backgroundPlayEnabled: Boolean = true,
    val keepNotification: Boolean = true,
    val audioFocusHandling: String = "Auto duck",
    val defaultOrientation: ScreenOrientation = ScreenOrientation.SYSTEM,
    val lockOrientationInPlayer: Boolean = false,
    val fullscreenMode: String = "Immersive",
    val keepScreenOn: Boolean = true
)

@HiltViewModel
class PlayerPreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(
        PlayerUiState(
            pipSupported = isPipSupported()
        )
    )
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesRepository.observeDefaultQuality().collect { quality ->
                _uiState.value = _uiState.value.copy(defaultQuality = quality)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAutoPlayNext().collect { enabled ->
                _uiState.value = _uiState.value.copy(autoPlayNext = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeRepeatMode().collect { mode ->
                _uiState.value = _uiState.value.copy(repeatMode = mode)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeShuffleMode().collect { enabled ->
                _uiState.value = _uiState.value.copy(shuffleMode = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeResumeMode().collect { mode ->
                _uiState.value = _uiState.value.copy(resumeMode = mode)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeResumeThreshold().collect { threshold ->
                _uiState.value = _uiState.value.copy(resumeThreshold = threshold)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeControlsTimeout().collect { timeout ->
                _uiState.value = _uiState.value.copy(controlsTimeout = timeout)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeDoubleTapToSeek().collect { enabled ->
                _uiState.value = _uiState.value.copy(doubleTapToSeek = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSeekDuration().collect { duration ->
                _uiState.value = _uiState.value.copy(seekDuration = duration)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeVolumeGesture().collect { enabled ->
                _uiState.value = _uiState.value.copy(volumeGesture = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeBrightnessGesture().collect { enabled ->
                _uiState.value = _uiState.value.copy(brightnessGesture = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSeekGesture().collect { enabled ->
                _uiState.value = _uiState.value.copy(seekGesture = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observePinchToZoom().collect { enabled ->
                _uiState.value = _uiState.value.copy(pinchToZoom = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeLockControlsFullscreen().collect { enabled ->
                _uiState.value = _uiState.value.copy(lockControlsFullscreen = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observePipEnabled().collect { enabled ->
                _uiState.value = _uiState.value.copy(pipEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAutoEnterPip().collect { enabled ->
                _uiState.value = _uiState.value.copy(autoEnterPip = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observePipButtonPosition().collect { position ->
                _uiState.value = _uiState.value.copy(pipButtonPosition = position)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observePipCustomActions().collect { enabled ->
                _uiState.value = _uiState.value.copy(pipCustomActions = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeBackgroundPlayEnabled().collect { enabled ->
                _uiState.value = _uiState.value.copy(backgroundPlayEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeKeepNotification().collect { enabled ->
                _uiState.value = _uiState.value.copy(keepNotification = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeDefaultOrientation().collect { orientation ->
                _uiState.value = _uiState.value.copy(defaultOrientation = orientation)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeLockOrientationInPlayer().collect { enabled ->
                _uiState.value = _uiState.value.copy(lockOrientationInPlayer = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeFullscreenMode().collect { mode ->
                _uiState.value = _uiState.value.copy(fullscreenMode = mode)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeKeepScreenOn().collect { enabled ->
                _uiState.value = _uiState.value.copy(keepScreenOn = enabled)
            }
        }
    }
    
    fun updateDefaultQuality(quality: VideoQuality) {
        _uiState.value = _uiState.value.copy(defaultQuality = quality)
        viewModelScope.launch {
            preferencesRepository.setDefaultQuality(quality)
        }
    }
    
    fun updateAutoPlayNext(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoPlayNext = enabled)
        viewModelScope.launch {
            preferencesRepository.setAutoPlayNext(enabled)
        }
    }
    
    fun updateRepeatMode(mode: RepeatMode) {
        _uiState.value = _uiState.value.copy(repeatMode = mode)
        viewModelScope.launch {
            preferencesRepository.setRepeatMode(mode)
        }
    }
    
    fun updateShuffleMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(shuffleMode = enabled)
        viewModelScope.launch {
            preferencesRepository.setShuffleMode(enabled)
        }
    }
    
    fun updateResumeMode(mode: Resume) {
        _uiState.value = _uiState.value.copy(resumeMode = mode)
        viewModelScope.launch {
            preferencesRepository.setResumeMode(mode)
        }
    }
    
    fun updateResumeThreshold(threshold: Int) {
        _uiState.value = _uiState.value.copy(resumeThreshold = threshold)
        viewModelScope.launch {
            preferencesRepository.setResumeThreshold(threshold)
        }
    }
    
    fun updateControlsTimeout(timeout: Int) {
        _uiState.value = _uiState.value.copy(controlsTimeout = timeout)
        viewModelScope.launch {
            preferencesRepository.setControlsTimeout(timeout)
        }
    }
    
    fun updateDoubleTapToSeek(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(doubleTapToSeek = enabled)
        viewModelScope.launch {
            preferencesRepository.setDoubleTapToSeek(enabled)
        }
    }
    
    fun updateSeekDuration(duration: Int) {
        _uiState.value = _uiState.value.copy(seekDuration = duration)
        viewModelScope.launch {
            preferencesRepository.setSeekDuration(duration)
        }
    }
    
    fun updateVolumeGesture(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(volumeGesture = enabled)
        viewModelScope.launch {
            preferencesRepository.setVolumeGesture(enabled)
        }
    }
    
    fun updateBrightnessGesture(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(brightnessGesture = enabled)
        viewModelScope.launch {
            preferencesRepository.setBrightnessGesture(enabled)
        }
    }
    
    fun updateSeekGesture(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(seekGesture = enabled)
        viewModelScope.launch {
            preferencesRepository.setSeekGesture(enabled)
        }
    }
    
    fun updatePinchToZoom(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(pinchToZoom = enabled)
        viewModelScope.launch {
            preferencesRepository.setPinchToZoom(enabled)
        }
    }
    
    fun updateLockControlsFullscreen(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(lockControlsFullscreen = enabled)
        viewModelScope.launch {
            preferencesRepository.setLockControlsFullscreen(enabled)
        }
    }
    
    fun updatePipEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(pipEnabled = enabled)
        viewModelScope.launch {
            preferencesRepository.setPipEnabled(enabled)
        }
    }
    
    fun updateAutoEnterPip(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoEnterPip = enabled)
        viewModelScope.launch {
            preferencesRepository.setAutoEnterPip(enabled)
        }
    }
    
    fun updatePipButtonPosition(position: String) {
        _uiState.value = _uiState.value.copy(pipButtonPosition = position)
        viewModelScope.launch {
            preferencesRepository.setPipButtonPosition(position)
        }
    }
    
    fun updatePipCustomActions(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(pipCustomActions = enabled)
        viewModelScope.launch {
            preferencesRepository.setPipCustomActions(enabled)
        }
    }
    
    fun updateBackgroundPlayEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(backgroundPlayEnabled = enabled)
        viewModelScope.launch {
            preferencesRepository.setBackgroundPlayEnabled(enabled)
        }
    }
    
    fun updateKeepNotification(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(keepNotification = enabled)
        viewModelScope.launch {
            preferencesRepository.setKeepNotification(enabled)
        }
    }
    
    fun updateDefaultOrientation(orientation: ScreenOrientation) {
        _uiState.value = _uiState.value.copy(defaultOrientation = orientation)
        viewModelScope.launch {
            preferencesRepository.setDefaultOrientation(orientation)
        }
    }
    
    fun updateLockOrientationInPlayer(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(lockOrientationInPlayer = enabled)
        viewModelScope.launch {
            preferencesRepository.setLockOrientationInPlayer(enabled)
        }
    }
    
    fun updateFullscreenMode(mode: String) {
        _uiState.value = _uiState.value.copy(fullscreenMode = mode)
        viewModelScope.launch {
            preferencesRepository.setFullscreenMode(mode)
        }
    }
    
    fun updateKeepScreenOn(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(keepScreenOn = enabled)
        viewModelScope.launch {
            preferencesRepository.setKeepScreenOn(enabled)
        }
    }
    
    fun resetToDefaults() {
        viewModelScope.launch {
            preferencesRepository.resetPlayerPreferences()
            loadPreferences()
        }
    }
    
    fun testPlayback() {
        viewModelScope.launch {
        }
    }
    
    private fun isPipSupported(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        } else {
            false
        }
    }
}
