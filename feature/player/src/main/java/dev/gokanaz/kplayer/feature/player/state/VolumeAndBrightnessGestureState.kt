package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

@Immutable
data class VolumeAndBrightnessGestureState(
    val isVolumeGestureActive: Boolean = false,
    val isBrightnessGestureActive: Boolean = false,
    val gestureStartY: Float = 0f,
    val currentVolume: Float = 0.5f,
    val currentBrightness: Float = 0.5f,
    val delta: Float = 0f,
    val showOverlay: Boolean = false,
    val overlayPosition: Offset = Offset.Zero,
    val gestureEdge: GestureEdge = GestureEdge.RIGHT
) {
    companion object {
        val Initial = VolumeAndBrightnessGestureState()
        
        val Sample = VolumeAndBrightnessGestureState(
            isVolumeGestureActive = true,
            isBrightnessGestureActive = false,
            gestureStartY = 500f,
            currentVolume = 0.7f,
            currentBrightness = 0.5f,
            delta = -0.2f,
            showOverlay = true,
            overlayPosition = Offset(100f, 300f),
            gestureEdge = GestureEdge.RIGHT
        )
    }
    
    fun withVolumeGestureStarted(startY: Float, edge: GestureEdge): VolumeAndBrightnessGestureState {
        return copy(
            isVolumeGestureActive = true,
            isBrightnessGestureActive = false,
            gestureStartY = startY,
            currentVolume = currentVolume,
            showOverlay = true,
            overlayPosition = Offset.Zero,
            gestureEdge = edge
        )
    }
    
    fun withBrightnessGestureStarted(startY: Float, edge: GestureEdge): VolumeAndBrightnessGestureState {
        return copy(
            isVolumeGestureActive = false,
            isBrightnessGestureActive = true,
            gestureStartY = startY,
            currentBrightness = currentBrightness,
            showOverlay = true,
            overlayPosition = Offset.Zero,
            gestureEdge = edge
        )
    }
    
    fun withVolumeGestureUpdated(deltaY: Float, sensitivity: Float = 0.01f): VolumeAndBrightnessGestureState {
        val newDelta = deltaY * sensitivity
        val newVolume = (currentVolume - newDelta).coerceIn(0f, 1f)
        return copy(
            delta = newDelta,
            currentVolume = newVolume,
            showOverlay = true
        )
    }
    
    fun withBrightnessGestureUpdated(deltaY: Float, sensitivity: Float = 0.01f): VolumeAndBrightnessGestureState {
        val newDelta = deltaY * sensitivity
        val newBrightness = (currentBrightness - newDelta).coerceIn(0f, 1f)
        return copy(
            delta = newDelta,
            currentBrightness = newBrightness,
            showOverlay = true
        )
    }
    
    fun withGestureEnded(): VolumeAndBrightnessGestureState {
        return copy(
            isVolumeGestureActive = false,
            isBrightnessGestureActive = false,
            showOverlay = false,
            delta = 0f
        )
    }
    
    fun withOverlayPosition(position: Offset): VolumeAndBrightnessGestureState {
        return copy(
            overlayPosition = position
        )
    }
    
    fun withOverlayHidden(): VolumeAndBrightnessGestureState {
        return copy(
            showOverlay = false
        )
    }
    
    fun getVolumePercentage(): Int {
        return (currentVolume * 100).toInt()
    }
    
    fun getBrightnessPercentage(): Int {
        return (currentBrightness * 100).toInt()
    }
}

enum class GestureEdge {
    LEFT,
    RIGHT
}
