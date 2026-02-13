package dev.gokanaz.kplayer.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
sealed class MediaLibraryDestination {
    @Serializable
    data object Main : MediaLibraryDestination()
    
    @Serializable
    data object Storage : MediaLibraryDestination()
    
    @Serializable
    data object Scan : MediaLibraryDestination()
    
    @Serializable
    data object ExcludedFolders : MediaLibraryDestination()
    
    @Serializable
    data object Cache : MediaLibraryDestination()
    
    @Serializable
    data object FileFilters : MediaLibraryDestination()
}

fun NavController.navigateToMediaLibrary(destination: MediaLibraryDestination) {
    when (destination) {
        MediaLibraryDestination.Main -> navigate(MediaLibraryDestination.Main)
        MediaLibraryDestination.Storage -> navigate(MediaLibraryDestination.Storage)
        MediaLibraryDestination.Scan -> navigate(MediaLibraryDestination.Scan)
        MediaLibraryDestination.ExcludedFolders -> navigate(MediaLibraryDestination.ExcludedFolders)
        MediaLibraryDestination.Cache -> navigate(MediaLibraryDestination.Cache)
        MediaLibraryDestination.FileFilters -> navigate(MediaLibraryDestination.FileFilters)
    }
}

fun NavGraphBuilder.mediaLibraryNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToStorage: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToExcludedFolders: () -> Unit,
    onNavigateToCache: () -> Unit,
    onNavigateToFileFilters: () -> Unit
) {
    composable<MediaLibraryDestination.Main> {
        dev.gokanaz.kplayer.feature.settings.MediaLibrarySettingsScreen(
            onNavigateBack = onNavigateBack,
            onNavigateToStorage = onNavigateToStorage,
            onNavigateToScan = onNavigateToScan,
            onNavigateToExcludedFolders = onNavigateToExcludedFolders,
            onNavigateToCache = onNavigateToCache,
            onNavigateToFileFilters = onNavigateToFileFilters
        )
    }
    
    composable<MediaLibraryDestination.Storage> {
        dev.gokanaz.kplayer.feature.settings.StorageManagementScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<MediaLibraryDestination.Scan> {
        dev.gokanaz.kplayer.feature.settings.ScanSettingsScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<MediaLibraryDestination.ExcludedFolders> {
        dev.gokanaz.kplayer.feature.settings.ExcludedFoldersScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<MediaLibraryDestination.Cache> {
        dev.gokanaz.kplayer.feature.settings.CacheManagementScreen(
            onNavigateBack = onNavigateBack
        )
    }
    
    composable<MediaLibraryDestination.FileFilters> {
        dev.gokanaz.kplayer.feature.settings.FileTypeFiltersScreen(
            onNavigateBack = onNavigateBack
        )
    }
}
