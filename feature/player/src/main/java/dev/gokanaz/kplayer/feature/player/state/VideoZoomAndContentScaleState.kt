package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import dev.gokanaz.kplayer.core.model.player.VideoContentScale

@Immutable
data class VideoZoomAndContentScaleState(
    val scaleType: VideoContentScale = VideoContentScale.FIT,
    val zoomScale: Float = 1.0f,
    val pivotX: Float = 0.5f,
    val pivotY: Float = 0.5f,
    val panOffsetX: Float = 0f,
    val panOffsetY: Float = 0f,
    val isZoomEnabled: Boolean = true,
    val isGestureZoomInProgress: Boolean = false,
    val minZoomScale: Float = 1.0f,
    val maxZoomScale: Float = 3.0f
) {
    companion object {
        val Initial = VideoZoomAndContentScaleState()
        
        val Sample = VideoZoomAndContentScaleState(
            scaleType = VideoContentScale.ZOOM,
            zoomScale = 1.5f,
            pivotX = 0.3f,
            pivotY = 0.7f,
            panOffsetX = -50f,
            panOffsetY = 30f,
            isZoomEnabled = true,
            isGestureZoomInProgress = false
        )
    }
    
    fun withScaleType(scaleType: VideoContentScale): VideoZoomAndContentScaleState {
        return copy(
            scaleType = scaleType,
            zoomScale = 1.0f,
            panOffsetX = 0f,
            panOffsetY = 0f
        )
    }
    
    fun withZoomIn(step: Float = 0.1f): VideoZoomAndContentScaleState {
        return copy(
            zoomScale = (zoomScale + step).coerceIn(minZoomScale, maxZoomScale),
            isGestureZoomInProgress = true
        )
    }
    
    fun withZoomOut(step: Float = 0.1f): VideoZoomAndContentScaleState {
        return copy(
            zoomScale = (zoomScale - step).coerceIn(minZoomScale, maxZoomScale),
            isGestureZoomInProgress = true
        )
    }
    
    fun withZoomAtPoint(scale: Float, x: Float, y: Float): VideoZoomAndContentScaleState {
        return copy(
            zoomScale = scale.coerceIn(minZoomScale, maxZoomScale),
            pivotX = x,
            pivotY = y,
            isGestureZoomInProgress = true
        )
    }
    
    fun withPan(deltaX: Float, deltaY: Float): VideoZoomAndContentScaleState {
        return copy(
            panOffsetX = panOffsetX + deltaX,
            panOffsetY = panOffsetY + deltaY
        )
    }
    
    fun withResetZoom(): VideoZoomAndContentScaleState {
        return copy(
            zoomScale = 1.0f,
            pivotX = 0.5f,
            pivotY = 0.5f,
            panOffsetX = 0f,
            panOffsetY = 0f,
            isGestureZoomInProgress = false
        )
    }
    
    fun withGestureCompleted(): VideoZoomAndContentScaleState {
        return copy(
            isGestureZoomInProgress = false
        )
    }
    
    fun withZoomEnabled(enabled: Boolean): VideoZoomAndContentScaleState {
        return copy(isZoomEnabled = enabled)
    }
    
    fun withZoomLimits(min: Float, max: Float): VideoZoomAndContentScaleState {
        return copy(
            minZoomScale = min,
            maxZoomScale = max,
            zoomScale = zoomScale.coerceIn(min, max)
        )
    }
    
    fun canZoomIn(): Boolean {
        return zoomScale < maxZoomScale
    }
    
    fun canZoomOut(): Boolean {
        return zoomScale > minZoomScale
    }
    
    fun getZoomPercentage(): Int {
        return (zoomScale * 100).toInt()
    }
}
