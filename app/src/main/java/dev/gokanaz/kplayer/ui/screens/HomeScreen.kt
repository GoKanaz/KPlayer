package dev.gokanaz.kplayer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.gokanaz.kplayer.navigation.Graph

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: Any
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "KPlayer Home")
        Button(onClick = { navController.navigate(Graph.MEDIA.route) }) {
            Text("Open Media")
        }
        Button(onClick = { navController.navigate(Graph.SETTINGS.route) }) {
            Text("Open Settings")
        }
    }
}
