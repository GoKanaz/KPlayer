package dev.gokanaz.kplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentMediaId = MutableStateFlow<String?>(null)
    val currentMediaId: StateFlow<String?> = _currentMediaId.asStateFlow()
    
    private val _volume = MutableStateFlow(1.0f)
    val volume: StateFlow<Float> = _volume.asStateFlow()
    
    fun playMedia(mediaId: String) {
        viewModelScope.launch {
            _currentMediaId.value = mediaId
            _isPlaying.value = true
        }
    }
    
    fun pauseMedia() {
        viewModelScope.launch {
            _isPlaying.value = false
        }
    }
    
    fun resumeMedia() {
        viewModelScope.launch {
            _isPlaying.value = true
        }
    }
    
    fun stopMedia() {
        viewModelScope.launch {
            _isPlaying.value = false
            _currentMediaId.value = null
        }
    }
    
    fun setVolume(volume: Float) {
        viewModelScope.launch {
            _volume.value = volume
        }
    }
}
