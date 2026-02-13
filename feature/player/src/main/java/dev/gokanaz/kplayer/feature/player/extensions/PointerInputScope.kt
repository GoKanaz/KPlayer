package dev.gokanaz.kplayer.feature.player.extensions

import androidx.compose.foundation.gestures.AwaitPointerEventScope
import androidx.compose.foundation.gestures.DragEvent
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

suspend fun PointerInputScope.detectVerticalDragGesturesWithThreshold(
    threshold: Float = 50f,
    onDragStart: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onSwipeUp: () -> Unit = {},
    onSwipeDown: () -> Unit = {}
) = coroutineScope {
    awaitEachGesture {
        val down = awaitFirstDown()
        onDragStart(down.position)
        var dragTotal = 0f
        var isSwipeTriggered = false
        
        drag(down.id) { change ->
            val dragAmount = change.positionChange().y
            dragTotal += dragAmount
            change.consumeAllChanges()
            
            if (!isSwipeTriggered) {
                when {
                    dragTotal < -threshold -> {
                        onSwipeUp()
                        isSwipeTriggered = true
                    }
                    dragTotal > threshold -> {
                        onSwipeDown()
                        isSwipeTriggered = true
                    }
                }
            }
        }
        onDragEnd()
    }
}

suspend fun PointerInputScope.detectHorizontalDragGesturesWithThreshold(
    threshold: Float = 50f,
    onDragStart: (Offset) -> Unit = {},
    onDragEnd: () -> Unit = {},
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {}
) = coroutineScope {
    awaitEachGesture {
        val down = awaitFirstDown()
        onDragStart(down.position)
        var dragTotal = 0f
        var isSwipeTriggered = false
        
        drag(down.id) { change ->
            val dragAmount = change.positionChange().x
            dragTotal += dragAmount
            change.consumeAllChanges()
            
            if (!isSwipeTriggered) {
                when {
                    dragTotal < -threshold -> {
                        onSwipeLeft()
                        isSwipeTriggered = true
                    }
                    dragTotal > threshold -> {
                        onSwipeRight()
                        isSwipeTriggered = true
                    }
                }
            }
        }
        onDragEnd()
    }
}

suspend fun PointerInputScope.detectDoubleTapRegions(
    leftRegion: () -> Unit = {},
    centerRegion: () -> Unit = {},
    rightRegion: () -> Unit = {}
) = coroutineScope {
    awaitEachGesture {
        val firstTap = awaitFirstDown()
        val firstTapPosition = firstTap.position
        
        val secondTap = awaitFirstDown(waitFor = 250)
        if (secondTap.changedToUp()) {
            val regionWidth = size.width / 3
            
            when (firstTapPosition.x) {
                in 0f..regionWidth -> leftRegion()
                in regionWidth..(regionWidth * 2) -> centerRegion()
                else -> rightRegion()
            }
        }
    }
}

suspend fun PointerInputScope.detectPinchToZoom(
    onZoomChange: (Float) -> Unit
) = coroutineScope {
    awaitEachGesture {
        var initialDistance = 0f
        var lastDistance = 0f
        
        val down = awaitFirstDown(requireUnconsumed = false)
        val secondDown = awaitFirstDown(requireUnconsumed = false)
        
        if (down.id != secondDown.id) {
            initialDistance = calculateDistance(down.position, secondDown.position)
            lastDistance = initialDistance
            
            do {
                val event = awaitPointerEvent(PointerEventPass.Main)
                val pointers = event.changes.filter { it.pressed }
                
                if (pointers.size >= 2) {
                    val currentDistance = calculateDistance(
                        pointers[0].position,
                        pointers[1].position
                    )
                    
                    if (currentDistance > 0) {
                        val scale = currentDistance / lastDistance
                        onZoomChange(scale)
                        lastDistance = currentDistance
                    }
                    
                    pointers.forEach { it.consumeAllChanges() }
                }
            } while (pointers.size >= 2)
        }
    }
}

fun PointerInputScope.isInLeftRegion(size: Size): Boolean {
    val touchX = awaitFirstDown().position.x
    return touchX < size.width / 3
}

fun PointerInputScope.isInRightRegion(size: Size): Boolean {
    val touchX = awaitFirstDown().position.x
    return touchX > (size.width / 3) * 2
}

fun PointerInputScope.isInTopRegion(size: Size): Boolean {
    val touchY = awaitFirstDown().position.y
    return touchY < size.height / 3
}

fun PointerInputScope.isInBottomRegion(size: Size): Boolean {
    val touchY = awaitFirstDown().position.y
    return touchY > (size.height / 3) * 2
}

fun PointerInputScope.getTouchPercentage(size: Size): Offset {
    val touch = awaitFirstDown().position
    return Offset(
        x = (touch.x / size.width).coerceIn(0f, 1f),
        y = (touch.y / size.height).coerceIn(0f, 1f)
    )
}

private fun calculateDistance(p1: Offset, p2: Offset): Float {
    val dx = p1.x - p2.x
    val dy = p1.y - p2.y
    return kotlin.math.sqrt(dx * dx + dy * dy)
}
