package dev.gokanaz.kplayer.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class AboutDestination {
    @Serializable
    data object Main : AboutDestination()
    
    @Serializable
    data object Licenses : AboutDestination()
    
    @Serializable
    data object Privacy : AboutDestination()
    
    @Serializable
    data object Terms : AboutDestination()
    
    @Serializable
    data object Support : AboutDestination()
    
    @Serializable
    data object Changelog : AboutDestination()
}

fun NavController.navigateToAbout(destination: AboutDestination) {
    when (destination) {
        AboutDestination.Main -> navigate(AboutDestination.Main)
        AboutDestination.Licenses -> navigate(AboutDestination.Licenses)
        AboutDestination.Privacy -> navigate(AboutDestination.Privacy)
        AboutDestination.Terms -> navigate(AboutDestination.Terms)
        AboutDestination.Support -> navigate(AboutDestination.Support)
        AboutDestination.Changelog -> navigate(AboutDestination.Changelog)
    }
}

fun NavGraphBuilder.aboutNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToLicenses: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToChangelog: () -> Unit,
    onOpenLink: (String) -> Unit
) {
    composable<AboutDestination.Main> {
        dev.gokanaz.kplayer.feature.settings.AboutSettingsScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToLicenses = onNavigateToLicenses,
            onNavigateToPrivacy = onNavigateToPrivacy,
            onNavigateToTerms = onNavigateToTerms,
            onNavigateToSupport = onNavigateToSupport,
            onNavigateToChangelog = onNavigateToChangelog,
            onOpenLink = onOpenLink
        )
    }
    
    composable<AboutDestination.Licenses> {
        dev.gokanaz.kplayer.feature.settings.OpenSourceLicensesScreen(
            onNavigateBack = onNavigateBack,
            onOpenLink = onOpenLink
        )
    }
    
    composable<AboutDestination.Privacy> {
        dev.gokanaz.kplayer.feature.settings.PrivacyPolicyScreen(
            onNavigateBack = onNavigateBack,
            onOpenLink = onOpenLink
        )
    }
    
    composable<AboutDestination.Terms> {
        dev.gokanaz.kplayer.feature.settings.TermsOfServiceScreen(
            onNavigateBack = onNavigateBack,
            onOpenLink = onOpenLink
        )
    }
    
    composable<AboutDestination.Support> {
        dev.gokanaz.kplayer.feature.settings.SupportDevelopmentScreen(
            onNavigateBack = onNavigateBack,
            onOpenLink = onOpenLink
        )
    }
    
    composable<AboutDestination.Changelog> {
        dev.gokanaz.kplayer.feature.settings.ChangelogScreen(
            onNavigateBack = onNavigateBack,
            onOpenLink = onOpenLink
        )
    }
}
