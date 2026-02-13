package dev.gokanaz.kplayer.feature.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class SettingsDestination {
    @Serializable
    data object Main : SettingsDestination()
    
    @Serializable
    data object Appearance : SettingsDestination()
    
    @Serializable
    data object Player : SettingsDestination()
    
    @Serializable
    data object Decoder : SettingsDestination()
    
    @Serializable
    data object MediaLibrary : SettingsDestination()
    
    @Serializable
    data object About : SettingsDestination()
}

fun NavController.navigateToSettings(destination: SettingsDestination) {
    when (destination) {
        SettingsDestination.Main -> navigate(SettingsDestination.Main)
        SettingsDestination.Appearance -> navigate(SettingsDestination.Appearance)
        SettingsDestination.Player -> navigate(SettingsDestination.Player)
        SettingsDestination.Decoder -> navigate(SettingsDestination.Decoder)
        SettingsDestination.MediaLibrary -> navigate(SettingsDestination.MediaLibrary)
        SettingsDestination.About -> navigate(SettingsDestination.About)
    }
}

@Composable
fun SettingsNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onNavigateToAppearance: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onNavigateToDecoder: () -> Unit,
    onNavigateToMediaLibrary: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = SettingsDestination.Main,
        modifier = modifier
    ) {
        composable<SettingsDestination.Main> {
            dev.gokanaz.kplayer.feature.settings.MainSettingsScreen(
                onNavigateBack = onNavigateBack,
                onNavigateToAppearance = onNavigateToAppearance,
                onNavigateToPlayer = onNavigateToPlayer,
                onNavigateToDecoder = onNavigateToDecoder,
                onNavigateToMediaLibrary = onNavigateToMediaLibrary,
                onNavigateToAbout = onNavigateToAbout
            )
        }
        
        composable<SettingsDestination.Appearance> {
            dev.gokanaz.kplayer.feature.settings.AppearanceSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTheme = { /* Navigate to theme settings */ },
                onNavigateToLanguage = { /* Navigate to language settings */ },
                onNavigateToSort = { /* Navigate to sort settings */ },
                onNavigateToView = { /* Navigate to view settings */ }
            )
        }
        
        composable<SettingsDestination.Player> {
            dev.gokanaz.kplayer.feature.settings.PlayerSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlayback = { /* Navigate to playback settings */ },
                onNavigateToAudio = { /* Navigate to audio settings */ },
                onNavigateToSubtitle = { /* Navigate to subtitle settings */ },
                onNavigateToGesture = { /* Navigate to gesture settings */ }
            )
        }
        
        composable<SettingsDestination.Decoder> {
            dev.gokanaz.kplayer.feature.settings.DecoderSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHardware = { /* Navigate to hardware decoder settings */ },
                onNavigateToSoftware = { /* Navigate to software decoder settings */ },
                onNavigateToCodecPriority = { /* Navigate to codec priority settings */ },
                onNavigateToAdvanced = { /* Navigate to advanced decoder settings */ },
                onNavigateToTestPlayback = { /* Navigate to test playback screen */ }
            )
        }
        
        composable<SettingsDestination.MediaLibrary> {
            dev.gokanaz.kplayer.feature.settings.MediaLibrarySettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStorage = { /* Navigate to storage management */ },
                onNavigateToScan = { /* Navigate to scan settings */ },
                onNavigateToExcludedFolders = { /* Navigate to excluded folders */ },
                onNavigateToCache = { /* Navigate to cache management */ },
                onNavigateToFileFilters = { /* Navigate to file type filters */ }
            )
        }
        
        composable<SettingsDestination.About> {
            dev.gokanaz.kplayer.feature.settings.AboutSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLicenses = { /* Navigate to open source licenses */ },
                onNavigateToPrivacy = { /* Navigate to privacy policy */ },
                onNavigateToTerms = { /* Navigate to terms of service */ },
                onNavigateToSupport = { /* Navigate to support development */ },
                onNavigateToChangelog = { /* Navigate to changelog */ },
                onOpenLink = { url -> /* Open external link */ }
            )
        }
    }
}
