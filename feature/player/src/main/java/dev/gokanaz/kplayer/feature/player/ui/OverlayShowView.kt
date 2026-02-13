package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun OverlayShowView(
    isVisible: Boolean,
    autoHideDelay: Long = 2000,
    onHide: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var internalVisible by remember(isVisible) { mutableStateOf(isVisible) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            internalVisible = true
            delay(autoHideDelay)
            internalVisible = false
            onHide?.invoke()
        }
    }
    
    AnimatedVisibility(
        visible = internalVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun OverlayStack(
    overlays: List<OverlayItem>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        overlays.forEach { overlay ->
            OverlayShowView(
                isVisible = overlay.isVisible,
                autoHideDelay = overlay.autoHideDelay,
                onHide = overlay.onHide
            ) {
                overlay.content()
            }
        }
    }
}

data class OverlayItem(
    val id: String,
    val isVisible: Boolean,
    val priority: Int = 0,
    val autoHideDelay: Long = 2000,
    val onHide: (() -> Unit)? = null,
    val content: @Composable () -> Unit
)
