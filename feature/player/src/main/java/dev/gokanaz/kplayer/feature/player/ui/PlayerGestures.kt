package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import dev.gokanaz.kplayer.feature.player.extensions.detectDoubleTapRegions
import dev.gokanaz.kplayer.feature.player.extensions.detectVerticalDragGesturesWithThreshold
import dev.gokanaz.kplayer.feature.player.extensions.detectPinchToZoom

data class GestureState(
    val isDoubleTapDetected: Boolean = false,
    val doubleTapPosition: Offset = Offset.Zero,
    val doubleTapRegion: TapRegion = TapRegion.Center,
    val isVerticalDragActive: Boolean = false,
    val dragDelta: Float = 0f,
    val dragEdge: GestureEdge = GestureEdge.RIGHT,
    val zoomScale: Float = 1f,
    val isZoomActive: Boolean = false
)

enum class TapRegion {
    Left, Center, Right, TopLeft, TopCenter, TopRight
}

enum class GestureEdge {
    Left, Right
}

fun Modifier.playerGestures(
    onDoubleTapLeft: () -> Unit = {},
    onDoubleTapRight: () -> Unit = {},
    onDoubleTapCenter: () -> Unit = {},
    onVerticalDragLeft: (Float) -> Unit = {},
    onVerticalDragRight: (Float) -> Unit = {},
    onHorizontalSwipe: (Float) -> Unit = {},
    onPinchZoom: (Float) -> Unit = {},
    onSingleTap: () -> Unit = {}
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    val configuration = LocalConfiguration.current
    var gestureState by remember { mutableStateOf(GestureState()) }
    
    this
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = { offset ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    val screenWidth = size.width
                    when {
                        offset.x < screenWidth / 3 -> onDoubleTapLeft()
                        offset.x > (screenWidth / 3) * 2 -> onDoubleTapRight()
                        else -> onDoubleTapCenter()
                    }
                },
                onTap = {
                    onSingleTap()
                }
            )
        }
        .pointerInput(Unit) {
            detectVerticalDragGesturesWithThreshold(
                threshold = 20f,
                onDragStart = { startPosition ->
                    val screenWidth = size.width
                    val edge = if (startPosition.x < screenWidth / 2) {
                        GestureEdge.LEFT
                    } else {
                        GestureEdge.RIGHT
                    }
                },
                onDragEnd = {
                    // Drag ended
                },
                onSwipeUp = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    // Handle swipe up
                },
                onSwipeDown = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    // Handle swipe down
                }
            )
        }
        .pointerInput(Unit) {
            detectPinchToZoom { scale ->
                onPinchZoom(scale)
            }
        }
}
