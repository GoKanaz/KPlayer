package dev.gokanaz.kplayer.feature.settings.screens.subtitle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.ui.graphics.Color
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import dev.gokanaz.kplayer.core.model.Font
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubtitleUiState(
    val subtitlesEnabled: Boolean = true,
    val subtitleLanguage: String = "English",
    val autoDownloadSubtitles: Boolean = false,
    val subtitleDelay: Int = 0,
    val fontFamily: Font = Font.SANS_SERIF,
    val fontSize: Int = 1,
    val fontColor: Color = Color.White,
    val boldText: Boolean = false,
    val italicText: Boolean = false,
    val textShadow: Boolean = true,
    val shadowOpacity: Float = 0.5f,
    val backgroundColor: Color = Color.Black,
    val backgroundOpacity: Float = 0.7f,
    val backgroundBlur: Boolean = false,
    val subtitlePosition: String = "Bottom",
    val verticalOffset: Int = 50,
    val maxLines: Int = 2,
    val textAlignment: String = "Center",
    val subtitleEncoding: String = "Auto",
    val subtitleFormat: String = "Auto",
    val overrideAssStyling: Boolean = false,
    val syncMethod: String = "Based on PTS",
    val cacheSize: String = "24.5 MB"
)

@HiltViewModel
class SubtitlePreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SubtitleUiState())
    val uiState: StateFlow<SubtitleUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            preferencesRepository.observeSubtitlesEnabled().collect { enabled ->
                _uiState.value = _uiState.value.copy(subtitlesEnabled = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSubtitleLanguage().collect { language ->
                _uiState.value = _uiState.value.copy(subtitleLanguage = language)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeAutoDownloadSubtitles().collect { enabled ->
                _uiState.value = _uiState.value.copy(autoDownloadSubtitles = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSubtitleDelay().collect { delay ->
                _uiState.value = _uiState.value.copy(subtitleDelay = delay)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeFontFamily().collect { font ->
                _uiState.value = _uiState.value.copy(fontFamily = font)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeFontSize().collect { size ->
                _uiState.value = _uiState.value.copy(fontSize = size)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeFontColor().collect { color ->
                _uiState.value = _uiState.value.copy(fontColor = color)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeBoldText().collect { enabled ->
                _uiState.value = _uiState.value.copy(boldText = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeItalicText().collect { enabled ->
                _uiState.value = _uiState.value.copy(italicText = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeTextShadow().collect { enabled ->
                _uiState.value = _uiState.value.copy(textShadow = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeShadowOpacity().collect { opacity ->
                _uiState.value = _uiState.value.copy(shadowOpacity = opacity)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeBackgroundColor().collect { color ->
                _uiState.value = _uiState.value.copy(backgroundColor = color)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeBackgroundOpacity().collect { opacity ->
                _uiState.value = _uiState.value.copy(backgroundOpacity = opacity)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeBackgroundBlur().collect { enabled ->
                _uiState.value = _uiState.value.copy(backgroundBlur = enabled)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSubtitlePosition().collect { position ->
                _uiState.value = _uiState.value.copy(subtitlePosition = position)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeVerticalOffset().collect { offset ->
                _uiState.value = _uiState.value.copy(verticalOffset = offset)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeMaxLines().collect { lines ->
                _uiState.value = _uiState.value.copy(maxLines = lines)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeTextAlignment().collect { alignment ->
                _uiState.value = _uiState.value.copy(textAlignment = alignment)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSubtitleEncoding().collect { encoding ->
                _uiState.value = _uiState.value.copy(subtitleEncoding = encoding)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeSubtitleFormat().collect { format ->
                _uiState.value = _uiState.value.copy(subtitleFormat = format)
            }
        }
        
        viewModelScope.launch {
            preferencesRepository.observeOverrideAssStyling().collect { enabled ->
                _uiState.value = _uiState.value.copy(overrideAssStyling = enabled)
            }
        }
    }
    
    fun updateSubtitlesEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(subtitlesEnabled = enabled)
        viewModelScope.launch {
            preferencesRepository.setSubtitlesEnabled(enabled)
        }
    }
    
    fun updateSubtitleLanguage(language: String) {
        _uiState.value = _uiState.value.copy(subtitleLanguage = language)
        viewModelScope.launch {
            preferencesRepository.setSubtitleLanguage(language)
        }
    }
    
    fun updateAutoDownloadSubtitles(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(autoDownloadSubtitles = enabled)
        viewModelScope.launch {
            preferencesRepository.setAutoDownloadSubtitles(enabled)
        }
    }
    
    fun updateSubtitleDelay(delay: Int) {
        _uiState.value = _uiState.value.copy(subtitleDelay = delay)
        viewModelScope.launch {
            preferencesRepository.setSubtitleDelay(delay)
        }
    }
    
    fun updateFontFamily(font: Font) {
        _uiState.value = _uiState.value.copy(fontFamily = font)
        viewModelScope.launch {
            preferencesRepository.setFontFamily(font)
        }
    }
    
    fun updateFontSize(size: Int) {
        _uiState.value = _uiState.value.copy(fontSize = size)
        viewModelScope.launch {
            preferencesRepository.setFontSize(size)
        }
    }
    
    fun updateFontColor(color: Color) {
        _uiState.value = _uiState.value.copy(fontColor = color)
        viewModelScope.launch {
            preferencesRepository.setFontColor(color)
        }
    }
    
    fun updateBoldText(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(boldText = enabled)
        viewModelScope.launch {
            preferencesRepository.setBoldText(enabled)
        }
    }
    
    fun updateItalicText(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(italicText = enabled)
        viewModelScope.launch {
            preferencesRepository.setItalicText(enabled)
        }
    }
    
    fun updateTextShadow(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(textShadow = enabled)
        viewModelScope.launch {
            preferencesRepository.setTextShadow(enabled)
        }
    }
    
    fun updateShadowOpacity(opacity: Float) {
        _uiState.value = _uiState.value.copy(shadowOpacity = opacity)
        viewModelScope.launch {
            preferencesRepository.setShadowOpacity(opacity)
        }
    }
    
    fun updateBackgroundColor(color: Color) {
        _uiState.value = _uiState.value.copy(backgroundColor = color)
        viewModelScope.launch {
            preferencesRepository.setBackgroundColor(color)
        }
    }
    
    fun updateBackgroundOpacity(opacity: Float) {
        _uiState.value = _uiState.value.copy(backgroundOpacity = opacity)
        viewModelScope.launch {
            preferencesRepository.setBackgroundOpacity(opacity)
        }
    }
    
    fun updateBackgroundBlur(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(backgroundBlur = enabled)
        viewModelScope.launch {
            preferencesRepository.setBackgroundBlur(enabled)
        }
    }
    
    fun updateSubtitlePosition(position: String) {
        _uiState.value = _uiState.value.copy(subtitlePosition = position)
        viewModelScope.launch {
            preferencesRepository.setSubtitlePosition(position)
        }
    }
    
    fun updateVerticalOffset(offset: Int) {
        _uiState.value = _uiState.value.copy(verticalOffset = offset)
        viewModelScope.launch {
            preferencesRepository.setVerticalOffset(offset)
        }
    }
    
    fun updateMaxLines(lines: Int) {
        _uiState.value = _uiState.value.copy(maxLines = lines)
        viewModelScope.launch {
            preferencesRepository.setMaxLines(lines)
        }
    }
    
    fun updateTextAlignment(alignment: String) {
        _uiState.value = _uiState.value.copy(textAlignment = alignment)
        viewModelScope.launch {
            preferencesRepository.setTextAlignment(alignment)
        }
    }
    
    fun updateSubtitleEncoding(encoding: String) {
        _uiState.value = _uiState.value.copy(subtitleEncoding = encoding)
        viewModelScope.launch {
            preferencesRepository.setSubtitleEncoding(encoding)
        }
    }
    
    fun updateSubtitleFormat(format: String) {
        _uiState.value = _uiState.value.copy(subtitleFormat = format)
        viewModelScope.launch {
            preferencesRepository.setSubtitleFormat(format)
        }
    }
    
    fun updateOverrideAssStyling(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(overrideAssStyling = enabled)
        viewModelScope.launch {
            preferencesRepository.setOverrideAssStyling(enabled)
        }
    }
    
    fun clearCache() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cacheSize = "0 B")
        }
    }
    
    fun resetToDefaults() {
        viewModelScope.launch {
            preferencesRepository.resetSubtitlePreferences()
            loadPreferences()
        }
    }
    
    fun testWithSampleSubtitle() {
        viewModelScope.launch {
        }
    }
}
