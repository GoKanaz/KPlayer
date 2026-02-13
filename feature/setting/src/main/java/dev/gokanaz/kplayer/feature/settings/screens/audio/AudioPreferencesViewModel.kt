package dev.gokanaz.kplayer.feature.settings.screens.audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AudioUiState(
    val volume: Float = 0.8f,
    val volumeGestureSensitivity: Int = 1,
    val muteOnHeadsetUnplug: Boolean = true,
    val audioDucking: Boolean = true,
    val equalizerEnabled: Boolean = false,
    val equalizerPreset: EqualizerPreset = EqualizerPreset.NORMAL,
    val equalizerBands: List<Int> = List(10) { 0 },
    val preferredAudioLanguage: String = "English",
    val preferStereo: Boolean = true,
    val defaultAudioTrack: Int = 0,
    val audioOutput: String = "Speaker",
    val audioBufferSize: Int = 200,
    val audioOffload: Boolean = false
)

@HiltViewModel
class AudioPreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AudioUiState())
    val uiState: StateFlow<AudioUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesRepository.observeVolume().collect { volume ->
                _uiState.value = _uiState.value.copy(volume = volume)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeVolumeGestureSensitivity().collect { sensitivity ->
                _uiState.value = _uiState.value.copy(volumeGestureSensitivity = sensitivity)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeMuteOnHeadsetUnplug().collect { enabled ->
                _uiState.value = _uiState.value.copy(muteOnHeadsetUnplug = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAudioDucking().collect { enabled ->
                _uiState.value = _uiState.value.copy(audioDucking = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeEqualizerEnabled().collect { enabled ->
                _uiState.value = _uiState.value.copy(equalizerEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeEqualizerPreset().collect { preset ->
                _uiState.value = _uiState.value.copy(equalizerPreset = preset)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeEqualizerBands().collect { bands ->
                _uiState.value = _uiState.value.copy(equalizerBands = bands)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observePreferredAudioLanguage().collect { language ->
                _uiState.value = _uiState.value.copy(preferredAudioLanguage = language)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observePreferStereo().collect { preferStereo ->
                _uiState.value = _uiState.value.copy(preferStereo = preferStereo)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeDefaultAudioTrack().collect { track ->
                _uiState.value = _uiState.value.copy(defaultAudioTrack = track)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAudioBufferSize().collect { size ->
                _uiState.value = _uiState.value.copy(audioBufferSize = size)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAudioOffload().collect { offload ->
                _uiState.value = _uiState.value.copy(audioOffload = offload)
            }
        }
    }
    
    fun updateVolume(volume: Float) {
        _uiState.value = _uiState.value.copy(volume = volume)
        viewModelScope.launch {
            preferencesRepository.setVolume(volume)
        }
    }
    
    fun updateVolumeGestureSensitivity(sensitivity: Int) {
        _uiState.value = _uiState.value.copy(volumeGestureSensitivity = sensitivity)
        viewModelScope.launch {
            preferencesRepository.setVolumeGestureSensitivity(sensitivity)
        }
    }
    
    fun updateMuteOnHeadsetUnplug(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(muteOnHeadsetUnplug = enabled)
        viewModelScope.launch {
            preferencesRepository.setMuteOnHeadsetUnplug(enabled)
        }
    }
    
    fun updateAudioDucking(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(audioDucking = enabled)
        viewModelScope.launch {
            preferencesRepository.setAudioDucking(enabled)
        }
    }
    
    fun updateEqualizerEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(equalizerEnabled = enabled)
        viewModelScope.launch {
            preferencesRepository.setEqualizerEnabled(enabled)
        }
    }
    
    fun updateEqualizerPreset(preset: EqualizerPreset) {
        _uiState.value = _uiState.value.copy(equalizerPreset = preset)
        viewModelScope.launch {
            preferencesRepository.setEqualizerPreset(preset)
            
            if (preset != EqualizerPreset.CUSTOM) {
                val presetBands = getPresetBands(preset)
                _uiState.value = _uiState.value.copy(equalizerBands = presetBands)
                preferencesRepository.setEqualizerBands(presetBands)
            }
        }
    }
    
    fun updateEqualizerBand(index: Int, value: Int) {
        val newBands = _uiState.value.equalizerBands.toMutableList()
        newBands[index] = value
        _uiState.value = _uiState.value.copy(equalizerBands = newBands)
        viewModelScope.launch {
            preferencesRepository.setEqualizerBands(newBands)
        }
    }
    
    fun updatePreferredAudioLanguage(language: String) {
        _uiState.value = _uiState.value.copy(preferredAudioLanguage = language)
        viewModelScope.launch {
            preferencesRepository.setPreferredAudioLanguage(language)
        }
    }
    
    fun updatePreferStereo(preferStereo: Boolean) {
        _uiState.value = _uiState.value.copy(preferStereo = preferStereo)
        viewModelScope.launch {
            preferencesRepository.setPreferStereo(preferStereo)
        }
    }
    
    fun updateDefaultAudioTrack(track: Int) {
        _uiState.value = _uiState.value.copy(defaultAudioTrack = track)
        viewModelScope.launch {
            preferencesRepository.setDefaultAudioTrack(track)
        }
    }
    
    fun updateAudioBufferSize(size: Int) {
        _uiState.value = _uiState.value.copy(audioBufferSize = size)
        viewModelScope.launch {
            preferencesRepository.setAudioBufferSize(size)
        }
    }
    
    fun updateAudioOffload(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(audioOffload = enabled)
        viewModelScope.launch {
            preferencesRepository.setAudioOffload(enabled)
        }
    }
    
    fun testAudioOutput() {
        viewModelScope.launch {
            preferencesRepository.testAudioOutput()
        }
    }
    
    private fun getPresetBands(preset: EqualizerPreset): List<Int> {
        return when (preset) {
            EqualizerPreset.NORMAL -> List(10) { 0 }
            EqualizerPreset.CLASSICAL -> listOf(0, 2, 3, 4, 3, 2, 1, 0, -1, -2)
            EqualizerPreset.DANCE -> listOf(5, 4, 3, 2, 1, 0, -1, -2, -3, -4)
            EqualizerPreset.FLAT -> List(10) { 0 }
            EqualizerPreset.JAZZ -> listOf(2, 3, 4, 3, 2, 1, 0, -1, -2, -3)
            EqualizerPreset.POP -> listOf(-1, 0, 2, 3, 4, 3, 2, 1, 0, -1)
            EqualizerPreset.ROCK -> listOf(4, 3, 2, 1, 0, -1, -2, -3, -4, -5)
            EqualizerPreset.CUSTOM -> List(10) { 0 }
        }
    }
}
