package dev.gokanaz.kplayer.feature.videopicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import dev.gokanaz.kplayer.feature.videopicker.composables.MediaView
import javax.inject.Inject

@AndroidEntryPoint
class VideoPickerActivity : ComponentActivity() {
    
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    
    private val viewModel: VideoPickerViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                MediaView(
                    viewModel = viewModel,
                    onVideoClick = { videoId ->
                        // Navigate to video player
                    },
                    onNavigateBack = {
                        finish()
                    }
                )
            }
        }
    }
}
