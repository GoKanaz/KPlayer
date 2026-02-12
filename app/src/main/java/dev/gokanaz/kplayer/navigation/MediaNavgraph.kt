package dev.gokanaz.kplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.gokanaz.kplayer.ui.screens.PlayerScreen
import dev.gokanaz.kplayer.ui.screens.LibraryScreen
import dev.gokanaz.kplayer.ui.screens.PlaylistScreen

@Composable
fun MediaNavGraph(
    navController: NavHostController,
    mainNavController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.MEDIA,
        startDestination = MediaScreen.Library.route
    ) {
        composable(MediaScreen.Library.route) {
            LibraryScreen(navController = navController)
        }
        composable(MediaScreen.Player.route) {
            PlayerScreen(navController = navController)
        }
        composable(MediaScreen.Playlist.route) {
            PlaylistScreen(navController = navController)
        }
    }
}

sealed class MediaScreen(val route: String) {
    object Library : MediaScreen("library")
    object Player : MediaScreen("player/{mediaId}") {
        fun passMediaId(mediaId: String): String = "player/$mediaId"
    }
    object Playlist : MediaScreen("playlist/{playlistId}") {
        fun passPlaylistId(playlistId: String): String = "playlist/$playlistId"
    }
}
