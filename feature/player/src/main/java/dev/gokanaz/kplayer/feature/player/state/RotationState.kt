package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class RotationState(
    val screenOrientation: ScreenOrientation = ScreenOrientation.PORTRAIT,
    val lockedOrientation: ScreenOrientation? = null,
    val isAutoRotateEnabled: Boolean = true,
    val videoRotation: VideoRotation = VideoRotation.ROTATION_0,
    val isVideoRotationLocked: Boolean = false,
    val targetOrientation: ScreenOrientation? = null
) {
    companion object {
        val Initial = RotationState()
        
        val Landscape = RotationState(
            screenOrientation = ScreenOrientation.LANDSCAPE,
            isAutoRotateEnabled = true,
            videoRotation = VideoRotation.ROTATION_0
        )
        
        val Sample = RotationState(
            screenOrientation = ScreenOrientation.LANDSCAPE,
            lockedOrientation = ScreenOrientation.LANDSCAPE,
            isAutoRotateEnabled = false,
            videoRotation = VideoRotation.ROTATION_90,
            isVideoRotationLocked = true
        )
    }
    
    fun withOrientationLocked(orientation: ScreenOrientation): RotationState {
        return copy(
            lockedOrientation = orientation,
            isAutoRotateEnabled = false
        )
    }
    
    fun withOrientationUnlocked(): RotationState {
        return copy(
            lockedOrientation = null,
            isAutoRotateEnabled = true
        )
    }
    
    fun withAutoRotateToggled(): RotationState {
        return copy(
            isAutoRotateEnabled = !isAutoRotateEnabled,
            lockedOrientation = if (isAutoRotateEnabled) null else screenOrientation
        )
    }
    
    fun withScreenOrientation(orientation: ScreenOrientation): RotationState {
        return if (lockedOrientation == null) {
            copy(screenOrientation = orientation)
        } else {
            this
        }
    }
    
    fun withVideoRotation(rotation: VideoRotation): RotationState {
        return copy(
            videoRotation = rotation,
            isVideoRotationLocked = true
        )
    }
    
    fun withVideoRotationLocked(locked: Boolean): RotationState {
        return copy(isVideoRotationLocked = locked)
    }
    
    fun withVideoRotationReset(): RotationState {
        return copy(
            videoRotation = VideoRotation.ROTATION_0,
            isVideoRotationLocked = false
        )
    }
    
    fun withTargetOrientation(orientation: ScreenOrientation): RotationState {
        return copy(targetOrientation = orientation)
    }
    
    fun getAspectRatio(): Float {
        return when (screenOrientation) {
            ScreenOrientation.PORTRAIT -> 9f / 16f
            ScreenOrientation.LANDSCAPE -> 16f / 9f
            else -> 1f
        }
    }
    
    fun getTotalRotationDegrees(): Int {
        return (videoRotation.degrees + if (screenOrientation == ScreenOrientation.PORTRAIT) 0 else 0) % 360
    }
}

enum class ScreenOrientation {
    PORTRAIT,
    LANDSCAPE,
    REVERSE_PORTRAIT,
    REVERSE_LANDSCAPE,
    SENSOR
}

enum class VideoRotation(val degrees: Int) {
    ROTATION_0(0),
    ROTATION_90(90),
    ROTATION_180(180),
    ROTATION_270(270)
}
