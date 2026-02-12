package dev.gokanaz.kplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.gokanaz.kplayer.ui.screens.SettingsScreen
import dev.gokanaz.kplayer.ui.screens.AboutScreen
import dev.gokanaz.kplayer.ui.screens.EqualizerScreen

@Composable
fun SettingsNavGraph(
    navController: NavHostController,
    mainNavController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.SETTINGS,
        startDestination = SettingsScreen.Main.route
    ) {
        composable(SettingsScreen.Main.route) {
            SettingsScreen(navController = navController)
        }
        composable(SettingsScreen.About.route) {
            AboutScreen(navController = navController)
        }
        composable(SettingsScreen.Equalizer.route) {
            EqualizerScreen(navController = navController)
        }
    }
}

sealed class SettingsScreen(val route: String) {
    object Main : SettingsScreen("settings")
    object About : SettingsScreen("about")
    object Equalizer : SettingsScreen("equalizer")
}

sealed class Graph {
    object MAIN : Graph("main")
    object MEDIA : Graph("media")
    object SETTINGS : Graph("settings")
}

open class Graph(val route: String)
