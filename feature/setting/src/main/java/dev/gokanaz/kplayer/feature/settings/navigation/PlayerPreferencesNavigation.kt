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
sealed class PlayerDestination {
    @Serializable
    data object Main : PlayerDestination()
    
    @Serializable
    data class Playback(val tabIndex: Int = 0) : PlayerDestination()
    
    @Serializable
    data class Audio(val tabIndex: Int = 0) : PlayerDestination()
    
    @Serializable
    data class Subtitle(val tabIndex: Int = 0) : PlayerDestination()
    
    @Serializable
    data class Gesture(val tabIndex: Int = 0) : PlayerDestination()
}

fun NavController.navigateToPlayer(destination: PlayerDestination) {
    when (destination) {
        PlayerDestination.Main -> navigate(PlayerDestination.Main)
        is PlayerDestination.Playback -> navigate(destination)
        is PlayerDestination.Audio -> navigate(destination)
        is PlayerDestination.Subtitle -> navigate(destination)
        is PlayerDestination.Gesture -> navigate(destination)
    }
}

fun NavGraphBuilder.playerNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToPlayback: (Int) -> Unit,
    onNavigateToAudio: (Int) -> Unit,
    onNavigateToSubtitle: (Int) -> Unit,
    onNavigateToGesture: (Int) -> Unit
) {
    composable<PlayerDestination.Main> {
        dev.gokanaz.kplayer.feature.settings.PlayerSettingsScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToPlayback = onNavigateToPlayback,
            onNavigateToAudio = onNavigateToAudio,
            onNavigateToSubtitle = onNavigateToSubtitle,
            onNavigateToGesture = onNavigateToGesture
        )
    }
    
    composable<PlayerDestination.Playback> { backStackEntry ->
        val destination = backStackEntry.toRoute<PlayerDestination.Playback>()
        dev.gokanaz.kplayer.feature.settings.PlaybackSettingsScreen(
            initialTabIndex = destination.tabIndex,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<PlayerDestination.Audio> { backStackEntry ->
        val destination = backStackEntry.toRoute<PlayerDestination.Audio>()
        dev.gokanaz.kplayer.feature.settings.AudioSettingsScreen(
            initialTabIndex = destination.tabIndex,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<PlayerDestination.Subtitle> { backStackEntry ->
        val destination = backStackEntry.toRoute<PlayerDestination.Subtitle>()
        dev.gokanaz.kplayer.feature.settings.SubtitleSettingsScreen(
            initialTabIndex = destination.tabIndex,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<PlayerDestination.Gesture> { backStackEntry ->
        val destination = backStackEntry.toRoute<PlayerDestination.Gesture>()
        dev.gokanaz.kplayer.feature.settings.GestureSettingsScreen(
            initialTabIndex = destination.tabIndex,
            onNavigateBack = onNavigateBack
        )
    }
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val _currentTabIndex = MutableStateFlow(0)
    val currentTabIndex: StateFlow<Int> = _currentTabIndex.asStateFlow()
    
    init {
        val destination = savedStateHandle.get<PlayerDestination>("destination")
        when (destination) {
            is PlayerDestination.Playback -> _currentTabIndex.value = destination.tabIndex
            is PlayerDestination.Audio -> _currentTabIndex.value = destination.tabIndex
            is PlayerDestination.Subtitle -> _currentTabIndex.value = destination.tabIndex
            is PlayerDestination.Gesture -> _currentTabIndex.value = destination.tabIndex
            else -> {}
        }
    }
    
    fun onTabSelected(index: Int) {
        viewModelScope.launch {
            _currentTabIndex.emit(index)
        }
    }
}
