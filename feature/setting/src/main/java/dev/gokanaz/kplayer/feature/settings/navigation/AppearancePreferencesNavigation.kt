package dev.gokanaz.kplayer.feature.settings.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
sealed class AppearanceDestination {
    @Serializable
    data object Main : AppearanceDestination()
    
    @Serializable
    data class Theme(val preferenceId: String) : AppearanceDestination()
    
    @Serializable
    data class Language(val preferenceId: String) : AppearanceDestination()
    
    @Serializable
    data class Sort(val preferenceId: String) : AppearanceDestination()
    
    @Serializable
    data class View(val preferenceId: String) : AppearanceDestination()
}

fun NavController.navigateToAppearance(destination: AppearanceDestination) {
    when (destination) {
        AppearanceDestination.Main -> navigate(AppearanceDestination.Main)
        is AppearanceDestination.Theme -> navigate(destination)
        is AppearanceDestination.Language -> navigate(destination)
        is AppearanceDestination.Sort -> navigate(destination)
        is AppearanceDestination.View -> navigate(destination)
    }
}

fun NavGraphBuilder.appearanceNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToTheme: (String) -> Unit,
    onNavigateToLanguage: (String) -> Unit,
    onNavigateToSort: (String) -> Unit,
    onNavigateToView: (String) -> Unit
) {
    composable<AppearanceDestination.Main> {
        dev.gokanaz.kplayer.feature.settings.AppearanceSettingsScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToTheme = onNavigateToTheme,
            onNavigateToLanguage = onNavigateToLanguage,
            onNavigateToSort = onNavigateToSort,
            onNavigateToView = onNavigateToView
        )
    }
    
    composable<AppearanceDestination.Theme> { backStackEntry ->
        val destination = backStackEntry.toRoute<AppearanceDestination.Theme>()
        dev.gokanaz.kplayer.feature.settings.ThemeSettingsScreen(
            preferenceId = destination.preferenceId,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<AppearanceDestination.Language> { backStackEntry ->
        val destination = backStackEntry.toRoute<AppearanceDestination.Language>()
        dev.gokanaz.kplayer.feature.settings.LanguageSettingsScreen(
            preferenceId = destination.preferenceId,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<AppearanceDestination.Sort> { backStackEntry ->
        val destination = backStackEntry.toRoute<AppearanceDestination.Sort>()
        dev.gokanaz.kplayer.feature.settings.SortSettingsScreen(
            preferenceId = destination.preferenceId,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<AppearanceDestination.View> { backStackEntry ->
        val destination = backStackEntry.toRoute<AppearanceDestination.View>()
        dev.gokanaz.kplayer.feature.settings.ViewSettingsScreen(
            preferenceId = destination.preferenceId,
            onNavigateBack = onNavigateBack
        )
    }
}

@HiltViewModel
class AppearanceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _selectedPreferenceId = MutableStateFlow<String?>(null)
    val selectedPreferenceId: StateFlow<String?> = _selectedPreferenceId.asStateFlow()
    
    init {
        val destination = savedStateHandle.get<AppearanceDestination>("destination")
        when (destination) {
            is AppearanceDestination.Theme -> _selectedPreferenceId.value = destination.preferenceId
            is AppearanceDestination.Language -> _selectedPreferenceId.value = destination.preferenceId
            is AppearanceDestination.Sort -> _selectedPreferenceId.value = destination.preferenceId
            is AppearanceDestination.View -> _selectedPreferenceId.value = destination.preferenceId
            else -> {}
        }
    }
    
    fun onPreferenceSelected(preferenceId: String) {
        viewModelScope.launch {
            _selectedPreferenceId.emit(preferenceId)
        }
    }
}
