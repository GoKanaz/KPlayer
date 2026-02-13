package dev.gokanaz.kplayer.feature.player.extensions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

fun Modifier.playerControls(): Modifier = composed {
    this
        .fillMaxSize()
        .alpha(animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300)
        ).value)
}

fun Modifier.playerControlButton(): Modifier = composed {
    this
        .size(48.dp)
        .clickable { }
}

fun Modifier.playerSeekbar(): Modifier = composed {
    this
        .fillMaxSize()
}

fun Modifier.playerOverlay(): Modifier = composed {
    this
        .fillMaxSize()
}

fun Modifier.playerGestureLayer(): Modifier = composed {
    this
        .fillMaxSize()
}

fun Modifier.visibleIf(condition: Boolean): Modifier = composed {
    this.alpha(if (condition) 1f else 0f)
}

fun Modifier.enabledIf(condition: Boolean): Modifier = composed {
    val alpha = if (condition) 1f else 0.38f
    this.alpha(alpha)
}

fun Modifier.alphaIf(condition: Boolean): Modifier = composed {
    val targetAlpha = if (condition) 1f else 0.5f
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 300)
    )
    this.alpha(alpha)
}

fun Modifier.animateVisibility(isVisible: Boolean): Modifier = composed {
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    this.alpha(alpha)
}

fun Modifier.consumeVerticalSwipe(
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {}
): Modifier = composed {
    this.pointerInput(Unit) {
        detectVerticalDragGestures(
            onVerticalDrag = { _, dragAmount ->
                if (dragAmount < -50) onSwipeUp()
                if (dragAmount > 50) onSwipeDown()
            }
        )
    }
}

fun Modifier.consumeHorizontalSwipe(
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {}
): Modifier = composed {
    this.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onHorizontalDrag = { _, dragAmount ->
                if (dragAmount < -50) onSwipeLeft()
                if (dragAmount > 50) onSwipeRight()
            }
        )
    }
}

fun Modifier.consumeDoubleTap(
    onDoubleTap: () -> Unit = {}
): Modifier = composed {
    this.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = { onDoubleTap() }
        )
    }
}
