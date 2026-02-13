package dev.gokanaz.kplayer.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class SubtitleDestination {
    @Serializable
    data object Main : SubtitleDestination()
    
    @Serializable
    data class Track(val trackId: String = "") : SubtitleDestination()
    
    @Serializable
    data object Fonts : SubtitleDestination()
    
    @Serializable
    data object Styles : SubtitleDestination()
    
    @Serializable
    data object Sync : SubtitleDestination()
}

fun NavController.navigateToSubtitle(destination: SubtitleDestination) {
    when (destination) {
        SubtitleDestination.Main -> navigate(SubtitleDestination.Main)
        is SubtitleDestination.Track -> navigate(destination)
        SubtitleDestination.Fonts -> navigate(SubtitleDestination.Fonts)
        SubtitleDestination.Styles -> navigate(SubtitleDestination.Styles)
        SubtitleDestination.Sync -> navigate(SubtitleDestination.Sync)
    }
}

fun NavGraphBuilder.subtitleNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToFonts: () -> Unit,
    onNavigateToStyles: () -> Unit,
    onNavigateToSync: () -> Unit,
    onNavigateToTrackDetails: (String) -> Unit
) {
    composable<SubtitleDestination.Main> {
        dev.gokanaz.kplayer.feature.settings.screens.subtitle.SubtitlePreferencesScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToFonts = onNavigateToFonts,
            onNavigateToStyles = onNavigateToStyles,
            onNavigateToSync = onNavigateToSync,
            onNavigateToTrackDetails = onNavigateToTrackDetails
        )
    }
    
    composable<SubtitleDestination.Track> { backStackEntry ->
        val destination = backStackEntry.toRoute<SubtitleDestination.Track>()
        dev.gokanaz.kplayer.feature.settings.screens.subtitle.SubtitleTrackDetailsScreen(
            trackId = destination.trackId,
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<SubtitleDestination.Fonts> {
        dev.gokanaz.kplayer.feature.settings.screens.subtitle.SubtitleFontsScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<SubtitleDestination.Styles> {
        dev.gokanaz.kplayer.feature.settings.screens.subtitle.SubtitleStylesScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<SubtitleDestination.Sync> {
        dev.gokanaz.kplayer.feature.settings.screens.subtitle.SubtitleSyncScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
