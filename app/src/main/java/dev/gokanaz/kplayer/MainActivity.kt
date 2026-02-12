package dev.gokanaz.kplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.gokanaz.kplayer.crash.GlobalExceptionHandler
import dev.gokanaz.kplayer.navigation.Graph
import dev.gokanaz.kplayer.navigation.MediaNavGraph
import dev.gokanaz.kplayer.navigation.SettingsNavGraph
import dev.gokanaz.kplayer.ui.screens.HomeScreen
import dev.gokanaz.kplayer.ui.theme.KPlayerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var exceptionHandler: GlobalExceptionHandler
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        exceptionHandler.init()
        exceptionHandler.setCurrentActivity(this)
        
        setContent {
            KPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KPlayerApp()
                }
            }
        }
    }
}

@Composable
fun KPlayerApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Graph.MAIN.route
    ) {
        composable(Graph.MAIN.route) {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        
        mediaNavGraph(navController)
        settingsNavGraph(navController)
    }
}

fun NavHost.mediaNavGraph(navController: NavHostController) {
    navigation(
        startDestination = MediaScreen.Library.route,
        route = Graph.MEDIA.route
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

fun NavHost.settingsNavGraph(navController: NavHostController) {
    navigation(
        startDestination = SettingsScreen.Main.route,
        route = Graph.SETTINGS.route
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
