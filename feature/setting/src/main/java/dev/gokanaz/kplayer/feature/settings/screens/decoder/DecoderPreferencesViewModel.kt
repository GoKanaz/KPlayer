package dev.gokanaz.kplayer.feature.settings.screens.decoder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DecoderUiState(
    val decoderPriority: DecoderPriority = DecoderPriority.AUTO,
    val codecs: List<CodecInfo> = emptyList(),
    val preferredCodec: String = "Auto",
    val maxResolution: String = "1080p",
    val maxFrameRate: Int = 60,
    val frameSkipping: Boolean = false,
    val tunnelingEnabled: Boolean = false,
    val audioDecoderPriority: String = "Auto",
    val showDecoderInfo: Boolean = false,
    val decoderStats: Map<String, String> = emptyMap()
)

@HiltViewModel
class DecoderPreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DecoderUiState(codecs = getDefaultCodecs()))
    val uiState: StateFlow<DecoderUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesRepository.observeDecoderPriority().collect { priority ->
                _uiState.value = _uiState.value.copy(decoderPriority = priority)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeMaxResolution().collect { resolution ->
                _uiState.value = _uiState.value.copy(maxResolution = resolution)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeMaxFrameRate().collect { frameRate ->
                _uiState.value = _uiState.value.copy(maxFrameRate = frameRate)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeFrameSkipping().collect { enabled ->
                _uiState.value = _uiState.value.copy(frameSkipping = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeTunnelingEnabled().collect { enabled ->
                _uiState.value = _uiState.value.copy(tunnelingEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeShowDecoderInfo().collect { show ->
                _uiState.value = _uiState.value.copy(showDecoderInfo = show)
                if (show) {
                    loadDecoderStats()
                }
            }
        }
    }
    
    fun updateDecoderPriority(priority: DecoderPriority) {
        _uiState.value = _uiState.value.copy(decoderPriority = priority)
        viewModelScope.launch {
            preferencesRepository.setDecoderPriority(priority)
        }
    }
    
    fun toggleCodec(codecName: String, enabled: Boolean) {
        val updatedCodecs = _uiState.value.codecs.map { codec ->
            if (codec.name == codecName) {
                codec.copy(isEnabled = enabled)
            } else {
                codec
            }
        }
        _uiState.value = _uiState.value.copy(codecs = updatedCodecs)
        viewModelScope.launch {
            preferencesRepository.setCodecEnabled(codecName, enabled)
        }
    }
    
    fun updateMaxFrameRate(frameRate: Int) {
        _uiState.value = _uiState.value.copy(maxFrameRate = frameRate)
        viewModelScope.launch {
            preferencesRepository.setMaxFrameRate(frameRate)
        }
    }
    
    fun updateFrameSkipping(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(frameSkipping = enabled)
        viewModelScope.launch {
            preferencesRepository.setFrameSkipping(enabled)
        }
    }
    
    fun updateTunnelingEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(tunnelingEnabled = enabled)
        viewModelScope.launch {
            preferencesRepository.setTunnelingEnabled(enabled)
        }
    }
    
    fun updateShowDecoderInfo(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(showDecoderInfo = enabled)
        viewModelScope.launch {
            preferencesRepository.setShowDecoderInfo(enabled)
            if (enabled) {
                loadDecoderStats()
            }
        }
    }
    
    fun clearDecoderCache() {
        viewModelScope.launch {
            preferencesRepository.clearDecoderCache()
            loadDecoderStats()
        }
    }
    
    private fun loadDecoderStats() {
        viewModelScope.launch {
            val stats = mapOf(
                "Hardware Decoders" to "4 active",
                "Software Decoders" to "6 available",
                "Frames Decoded" to "12,345",
                "Average Decode Time" to "3.2 ms",
                "Dropped Frames" to "12",
                "Last Error" to "None"
            )
            _uiState.value = _uiState.value.copy(decoderStats = stats)
        }
    }
    
    private fun getDefaultCodecs(): List<CodecInfo> {
        return listOf(
            CodecInfo(
                name = "H.264 / AVC",
                description = "Most common video codec",
                hardwareSupported = true,
                softwareSupported = true,
                isEnabled = true
            ),
            CodecInfo(
                name = "H.265 / HEVC",
                description = "Better compression, 4K/8K support",
                hardwareSupported = true,
                softwareSupported = true,
                isEnabled = true
            ),
            CodecInfo(
                name = "VP9",
                description = "YouTube, WebM format",
                hardwareSupported = true,
                softwareSupported = true,
                isEnabled = true
            ),
            CodecInfo(
                name = "AV1",
                description = "Modern, royalty-free codec",
                hardwareSupported = false,
                softwareSupported = true,
                isEnabled = false
            ),
            CodecInfo(
                name = "MPEG-4",
                description = "Legacy codec",
                hardwareSupported = true,
                softwareSupported = true,
                isEnabled = true
            ),
            CodecInfo(
                name = "MPEG-2",
                description = "DVD, broadcast",
                hardwareSupported = true,
                softwareSupported = true,
                isEnabled = true
            ),
            CodecInfo(
                name = "VC-1",
                description = "Windows Media Video",
                hardwareSupported = true,
                softwareSupported = true,
                isEnabled = true
            )
        )
    }
}
