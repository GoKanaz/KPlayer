package dev.gokanaz.kplayer.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class DecoderDestination {
    @Serializable
    data object Main : DecoderDestination()
    
    @Serializable
    data object Hardware : DecoderDestination()
    
    @Serializable
    data object Software : DecoderDestination()
    
    @Serializable
    data object CodecPriority : DecoderDestination()
    
    @Serializable
    data object Advanced : DecoderDestination()
    
    @Serializable
    data object TestPlayback : DecoderDestination()
}

fun NavController.navigateToDecoder(destination: DecoderDestination) {
    when (destination) {
        DecoderDestination.Main -> navigate(DecoderDestination.Main)
        DecoderDestination.Hardware -> navigate(DecoderDestination.Hardware)
        DecoderDestination.Software -> navigate(DecoderDestination.Software)
        DecoderDestination.CodecPriority -> navigate(DecoderDestination.CodecPriority)
        DecoderDestination.Advanced -> navigate(DecoderDestination.Advanced)
        DecoderDestination.TestPlayback -> navigate(DecoderDestination.TestPlayback)
    }
}

fun NavGraphBuilder.decoderNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToHardware: () -> Unit,
    onNavigateToSoftware: () -> Unit,
    onNavigateToCodecPriority: () -> Unit,
    onNavigateToAdvanced: () -> Unit,
    onNavigateToTestPlayback: () -> Unit
) {
    composable<DecoderDestination.Main> {
        dev.gokanaz.kplayer.feature.settings.DecoderSettingsScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToHardware = onNavigateToHardware,
            onNavigateToSoftware = onNavigateToSoftware,
            onNavigateToCodecPriority = onNavigateToCodecPriority,
            onNavigateToAdvanced = onNavigateToAdvanced,
            onNavigateToTestPlayback = onNavigateToTestPlayback
        )
    }
    
    composable<DecoderDestination.Hardware> {
        dev.gokanaz.kplayer.feature.settings.HardwareDecoderSettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<DecoderDestination.Software> {
        dev.gokanaz.kplayer.feature.settings.SoftwareDecoderSettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<DecoderDestination.CodecPriority> {
        dev.gokanaz.kplayer.feature.settings.CodecPrioritySettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<DecoderDestination.Advanced> {
        dev.gokanaz.kplayer.feature.settings.AdvancedDecoderSettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<DecoderDestination.TestPlayback> {
        dev.gokanaz.kplayer.feature.settings.TestPlaybackScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
