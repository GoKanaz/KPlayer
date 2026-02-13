package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import android.view.WindowManager

@Immutable
data class MediaPresentationState(
    val mode: PresentationMode = PresentationMode.Normal,
    val isFullscreen: Boolean = false,
    val isImmersiveModeEnabled: Boolean = false,
    val areSystemBarsVisible: Boolean = true,
    val displayCutoutHandling: DisplayCutoutHandling = DisplayCutoutHandling.Never,
    val aspectRatioMode: AspectRatioMode = AspectRatioMode.Fit,
    val surfaceWidth: Int = 0,
    val surfaceHeight: Int = 0,
    val surfaceX: Int = 0,
    val surfaceY: Int = 0
) {
    companion object {
        val Initial = MediaPresentationState()
        
        val Fullscreen = MediaPresentationState(
            mode = PresentationMode.Fullscreen,
            isFullscreen = true,
            isImmersiveModeEnabled = true,
            areSystemBarsVisible = false,
            displayCutoutHandling = DisplayCutoutHandling.Always
        )
        
        val Sample = MediaPresentationState(
            mode = PresentationMode.Normal,
            isFullscreen = false,
            isImmersiveModeEnabled = false,
            areSystemBarsVisible = true,
            surfaceWidth = 1920,
            surfaceHeight = 1080
        )
    }
    
    fun withFullscreenEntered(): MediaPresentationState {
        return copy(
            mode = PresentationMode.Fullscreen,
            isFullscreen = true,
            isImmersiveModeEnabled = true,
            areSystemBarsVisible = false,
            displayCutoutHandling = DisplayCutoutHandling.Always
        )
    }
    
    fun withFullscreenExited(): MediaPresentationState {
        return copy(
            mode = PresentationMode.Normal,
            isFullscreen = false,
            isImmersiveModeEnabled = false,
            areSystemBarsVisible = true,
            displayCutoutHandling = DisplayCutoutHandling.Never
        )
    }
    
    fun withImmersiveModeToggled(): MediaPresentationState {
        return copy(
            isImmersiveModeEnabled = !isImmersiveModeEnabled,
            areSystemBarsVisible = isImmersiveModeEnabled
        )
    }
    
    fun withDisplayCutoutHandling(handling: DisplayCutoutHandling): MediaPresentationState {
        return copy(displayCutoutHandling = handling)
    }
    
    fun withAspectRatioMode(mode: AspectRatioMode): MediaPresentationState {
        return copy(aspectRatioMode = mode)
    }
    
    fun withSurfaceSize(width: Int, height: Int): MediaPresentationState {
        return copy(
            surfaceWidth = width,
            surfaceHeight = height
        )
    }
    
    fun withSurfacePosition(x: Int, y: Int): MediaPresentationState {
        return copy(
            surfaceX = x,
            surfaceY = y
        )
    }
    
    fun withPipMode(): MediaPresentationState {
        return copy(
            mode = PresentationMode.PictureInPicture,
            isFullscreen = false,
            isImmersiveModeEnabled = false,
            areSystemBarsVisible = false
        )
    }
    
    fun withMiniPlayerMode(): MediaPresentationState {
        return copy(
            mode = PresentationMode.MiniPlayer,
            isFullscreen = false,
            isImmersiveModeEnabled = false,
            areSystemBarsVisible = true
        )
    }
    
    fun getSurfaceAspectRatio(): Float {
        return if (surfaceHeight > 0) {
            surfaceWidth.toFloat() / surfaceHeight.toFloat()
        } else {
            16f / 9f
        }
    }
}

enum class PresentationMode {
    Normal,
    Fullscreen,
    PictureInPicture,
    MiniPlayer
}

enum class DisplayCutoutHandling {
    Never,
    Always,
    ShortEdges,
    NeverWithCutout
}

enum class AspectRatioMode {
    Fit,
    Fill,
    Stretch,
    Original
}
