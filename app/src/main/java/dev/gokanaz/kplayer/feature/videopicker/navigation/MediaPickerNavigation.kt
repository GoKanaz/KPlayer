package dev.gokanaz.kplayer.feature.videopicker.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.MediaPickerScreen
import dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.MediaPickerViewModel

/**
 * Sealed class representing all destinations in the media picker feature
 */
sealed class MediaPickerDestination(
    val route: String
) {
    object Main : MediaPickerDestination("media_picker_main")
    data class Folder(
        val folderId: String,
        val folderName: String
    ) : MediaPickerDestination("media_picker_folder/{folderId}/{folderName}") {
        fun createRoute() = "media_picker_folder/$folderId/$folderName"
    }
    data class Video(val videoId: String) : MediaPickerDestination("media_picker_video/{videoId}") {
        fun createRoute() = "media_picker_video/$videoId"
    }
    object Search : MediaPickerDestination("media_picker_search")
    object Settings : MediaPickerDestination("media_picker_settings")
}

/**
 * Extension functions for NavController to navigate between destinations
 */
fun NavController.navigateToMediaPicker(
    startDestination: MediaPickerDestination = MediaPickerDestination.Main
) {
    when (startDestination) {
        is MediaPickerDestination.Main -> navigate(MediaPickerDestination.Main.route)
        is MediaPickerDestination.Folder -> navigate(startDestination.createRoute())
        is MediaPickerDestination.Video -> navigate(startDestination.createRoute())
        is MediaPickerDestination.Search -> navigate(MediaPickerDestination.Search.route)
        is MediaPickerDestination.Settings -> navigate(MediaPickerDestination.Settings.route)
    }
}

fun NavController.navigateToFolder(folderId: String, folderName: String) {
    val route = MediaPickerDestination.Folder(folderId, folderName).createRoute()
    navigate(route)
}

fun NavController.navigateToVideo(videoId: String) {
    val route = MediaPickerDestination.Video(videoId).createRoute()
    navigate(route)
}

fun NavController.navigateToSearch() {
    navigate(MediaPickerDestination.Search.route)
}

fun NavController.navigateToSettings() {
    navigate(MediaPickerDestination.Settings.route)
}

/**
 * NavHost for media picker feature
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MediaPickerNavHost(
    navController: NavHostController,
    startDestination: MediaPickerDestination = MediaPickerDestination.Main,
    modifier: Modifier = Modifier,
    onVideoClick: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = when (startDestination) {
            is MediaPickerDestination.Main -> startDestination.route
            is MediaPickerDestination.Folder -> startDestination.createRoute()
            is MediaPickerDestination.Video -> startDestination.createRoute()
            is MediaPickerDestination.Search -> startDestination.route
            is MediaPickerDestination.Settings -> startDestination.route
        },
        modifier = modifier,
        enterTransition = {
            when (initialState.destination.route) {
                MediaPickerDestination.Main.route -> fadeIn(animationSpec = tween(300))
                else -> slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                MediaPickerDestination.Main.route -> fadeOut(animationSpec = tween(300))
                else -> slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            }
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        }
    ) {
        // Main screen
        composable(
            route = MediaPickerDestination.Main.route
        ) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(MediaPickerDestination.Main.route)
            }
            val viewModel: MediaPickerViewModel = hiltViewModel(parentEntry)
            
            MediaPickerScreen(
                viewModel = viewModel,
                onFolderClick = { folderId, folderName ->
                    navController.navigateToFolder(folderId, folderName)
                },
                onVideoClick = onVideoClick,
                onSearchClick = {
                    navController.navigateToSearch()
                },
                onSettingsClick = {
                    navController.navigateToSettings()
                },
                onNavigateBack = onNavigateBack
            )
        }
        
        // Folder screen
        composable(
            route = MediaPickerDestination.Folder.route,
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType },
                navArgument("folderName") { type = NavType.StringType }
            )
        ) { entry ->
            val folderId = entry.arguments?.getString("folderId") ?: return@composable
            val folderName = entry.arguments?.getString("folderName") ?: return@composable
            
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(MediaPickerDestination.Main.route)
            }
            val viewModel: MediaPickerViewModel = hiltViewModel(parentEntry)
            
            LaunchedEffect(folderId) {
                viewModel.loadVideosInFolder(folderId)
            }
            
            MediaPickerScreen(
                viewModel = viewModel,
                folderName = folderName,
                onVideoClick = { videoId ->
                    onVideoClick(videoId)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Search screen
        composable(
            route = MediaPickerDestination.Search.route
        ) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(MediaPickerDestination.Main.route)
            }
            val viewModel: MediaPickerViewModel = hiltViewModel(parentEntry)
            
            MediaPickerScreen(
                viewModel = viewModel,
                isSearchMode = true,
                onVideoClick = onVideoClick,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Settings screen
        composable(
            route = MediaPickerDestination.Settings.route
        ) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(MediaPickerDestination.Main.route)
            }
            val viewModel: MediaPickerViewModel = hiltViewModel(parentEntry)
            
            // Settings screen would be implemented here
            // For now, just navigate back
            LaunchedEffect(Unit) {
                navController.popBackStack()
            }
        }
    }
}

/**
 * Navigation actions interface for better type safety
 */
interface MediaPickerNavigationActions {
    fun navigateToFolder(folderId: String, folderName: String)
    fun navigateToVideo(videoId: String)
    fun navigateToSearch()
    fun navigateToSettings()
    fun navigateUp()
}

/**
 * Implementation of navigation actions
 */
class MediaPickerNavigationActionsImpl(
    private val navController: NavController
) : MediaPickerNavigationActions {
    override fun navigateToFolder(folderId: String, folderName: String) {
        navController.navigateToFolder(folderId, folderName)
    }
    
    override fun navigateToVideo(videoId: String) {
        navController.navigateToVideo(videoId)
    }
    
    override fun navigateToSearch() {
        navController.navigateToSearch()
    }
    
    override fun navigateToSettings() {
        navController.navigateToSettings()
    }
    
    override fun navigateUp() {
        navController.navigateUp()
    }
}

/**
 * Get navigation actions from NavController
 */
@Composable
fun rememberMediaPickerNavigationActions(
    navController: NavController
): MediaPickerNavigationActions {
    return remember(navController) {
        MediaPickerNavigationActionsImpl(navController)
    }
}
