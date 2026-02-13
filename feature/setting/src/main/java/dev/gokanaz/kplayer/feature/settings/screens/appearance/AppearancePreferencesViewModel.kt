package dev.gokanaz.kplayer.feature.settings.screens.appearance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import dev.gokanaz.kplayer.core.model.DarkMode
import dev.gokanaz.kplayer.core.model.ThemeColor
import dev.gokanaz.kplayer.core.model.ViewType
import dev.gokanaz.kplayer.core.model.Font
import dev.gokanaz.kplayer.core.model.AppLanguage
import dev.gokanaz.kplayer.core.domain.SortType
import dev.gokanaz.kplayer.core.domain.SortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppearanceUiState(
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val themeColor: ThemeColor = ThemeColor.DEFAULT,
    val dynamicColor: Boolean = true,
    val fontScale: Float = 1.0f,
    val appLanguage: AppLanguage = AppLanguage.AUTO,
    val viewType: ViewType = ViewType.GRID,
    val sortType: SortType = SortType.DATE,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val gridColumns: Int = 2,
    val isLoading: Boolean = false,
    val hasUnsavedChanges: Boolean = false
)

@HiltViewModel
class AppearancePreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AppearanceUiState(isLoading = true))
    val uiState: StateFlow<AppearanceUiState> = _uiState.asStateFlow()
    
    private val originalState = MutableStateFlow<AppearanceUiState?>(null)
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            combine(
                preferencesRepository.observeDarkMode(),
                preferencesRepository.observeThemeColor(),
                preferencesRepository.observeDynamicColorEnabled(),
                preferencesRepository.observeFontScale(),
                preferencesRepository.observeAppLanguage(),
                preferencesRepository.observeDefaultViewType(),
                preferencesRepository.observeDefaultVideoSortType(),
                preferencesRepository.observeDefaultSortOrder(),
                preferencesRepository.observeGridColumns()
            ) { darkMode, themeColor, dynamicColor, fontScale, language,
                viewType, sortType, sortOrder, gridColumns ->
                
                AppearanceUiState(
                    darkMode = darkMode,
                    themeColor = themeColor,
                    dynamicColor = dynamicColor,
                    fontScale = fontScale / 100f,
                    appLanguage = language,
                    viewType = viewType,
                    sortType = sortType,
                    sortOrder = sortOrder,
                    gridColumns = gridColumns,
                    isLoading = false,
                    hasUnsavedChanges = false
                )
            }.collect { newState ->
                _uiState.value = newState
                if (originalState.value == null) {
                    originalState.value = newState
                }
            }
        }
    }
    
    fun updateDarkMode(mode: DarkMode) {
        _uiState.value = _uiState.value.copy(
            darkMode = mode,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setDarkMode(mode)
            checkUnsavedChanges()
        }
    }
    
    fun updateThemeColor(color: ThemeColor) {
        _uiState.value = _uiState.value.copy(
            themeColor = color,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setThemeColor(color)
            checkUnsavedChanges()
        }
    }
    
    fun updateDynamicColor(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            dynamicColor = enabled,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setDynamicColorEnabled(enabled)
            checkUnsavedChanges()
        }
    }
    
    fun updateFontScale(scale: Float) {
        _uiState.value = _uiState.value.copy(
            fontScale = scale,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setFontScale((scale * 100).toInt())
            checkUnsavedChanges()
        }
    }
    
    fun updateAppLanguage(language: AppLanguage) {
        _uiState.value = _uiState.value.copy(
            appLanguage = language,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setAppLanguage(language)
            checkUnsavedChanges()
        }
    }
    
    fun updateViewType(viewType: ViewType) {
        _uiState.value = _uiState.value.copy(
            viewType = viewType,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setDefaultViewType(viewType)
            checkUnsavedChanges()
        }
    }
    
    fun updateSortType(sortType: SortType) {
        _uiState.value = _uiState.value.copy(
            sortType = sortType,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setDefaultVideoSortType(sortType)
            checkUnsavedChanges()
        }
    }
    
    fun updateSortOrder(order: SortOrder) {
        _uiState.value = _uiState.value.copy(
            sortOrder = order,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setDefaultSortOrder(order)
            checkUnsavedChanges()
        }
    }
    
    fun updateGridColumns(columns: Int) {
        _uiState.value = _uiState.value.copy(
            gridColumns = columns,
            hasUnsavedChanges = true
        )
        viewModelScope.launch {
            preferencesRepository.setGridColumns(columns)
            checkUnsavedChanges()
        }
    }
    
    fun saveChanges() {
        viewModelScope.launch {
            val currentState = _uiState.value
            preferencesRepository.setDarkMode(currentState.darkMode)
            preferencesRepository.setThemeColor(currentState.themeColor)
            preferencesRepository.setDynamicColorEnabled(currentState.dynamicColor)
            preferencesRepository.setFontScale((currentState.fontScale * 100).toInt())
            preferencesRepository.setAppLanguage(currentState.appLanguage)
            preferencesRepository.setDefaultViewType(currentState.viewType)
            preferencesRepository.setDefaultVideoSortType(currentState.sortType)
            preferencesRepository.setDefaultSortOrder(currentState.sortOrder)
            preferencesRepository.setGridColumns(currentState.gridColumns)
            
            originalState.value = currentState.copy(hasUnsavedChanges = false)
            _uiState.value = currentState.copy(hasUnsavedChanges = false)
        }
    }
    
    fun resetToDefaults() {
        viewModelScope.launch {
            preferencesRepository.clearAllPreferences()
            loadPreferences()
        }
    }
    
    private suspend fun checkUnsavedChanges() {
        val current = _uiState.value
        val original = originalState.value
        val hasChanges = original != null && (
            current.darkMode != original.darkMode ||
            current.themeColor != original.themeColor ||
            current.dynamicColor != original.dynamicColor ||
            current.fontScale != original.fontScale ||
            current.appLanguage != original.appLanguage ||
            current.viewType != original.viewType ||
            current.sortType != original.sortType ||
            current.sortOrder != original.sortOrder ||
            current.gridColumns != original.gridColumns
        )
        _uiState.value = current.copy(hasUnsavedChanges = hasChanges)
    }
}
