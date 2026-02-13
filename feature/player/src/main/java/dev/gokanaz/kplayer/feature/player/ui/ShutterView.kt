package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun ShutterView(
    isVisible: Boolean,
    isLoading: Boolean = true,
    error: String? = null,
    onAnimationComplete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isAnimating by remember { mutableStateOf(false) }
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            isAnimating = true
            animationProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800)
            )
            delay(200)
            isAnimating = false
            onAnimationComplete?.invoke()
        } else {
            animationProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 800)
            )
        }
    }
    
    if (isVisible || animationProgress.value > 0f) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = animationProgress.value * 0.9f))
                .clipToBounds(),
            contentAlignment = Alignment.Center
        ) {
            when {
                error != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = NextIconPainter(NextIcon.Error),
                            contentDescription = null,
                            tint = AppTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = error,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
                isLoading -> {
                    CircularProgressIndicator(
                        color = AppTheme.colorScheme.primary,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(48.dp)
                    )
                }
                else -> {
                    // Shutter closed - no content
                }
            }
        }
    }
}
