package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset

@Immutable
data class TapGestureState(
    val tapPosition: Offset = Offset.Unspecified,
    val tapRegion: TapRegion = TapRegion.CenterThird,
    val tapCount: Int = 0,
    val isDoubleTapDetected: Boolean = false,
    val doubleTapAction: DoubleTapAction = DoubleTapAction.SEEK_FORWARD_BACKWARD,
    val lastTapTimestamp: Long = 0L,
    val gestureVelocity: Float = 0f
) {
    companion object {
        val Initial = TapGestureState()
        
        val Sample = TapGestureState(
            tapPosition = Offset(500f, 300f),
            tapRegion = TapRegion.RightThird,
            tapCount = 2,
            isDoubleTapDetected = true,
            doubleTapAction = DoubleTapAction.SEEK_FORWARD_BACKWARD,
            lastTapTimestamp = System.currentTimeMillis(),
            gestureVelocity = 0.5f
        )
    }
    
    fun withTapRegistered(position: Offset, screenSize: androidx.compose.ui.geometry.Size): TapGestureState {
        return copy(
            tapPosition = position,
            tapRegion = calculateTapRegion(position, screenSize),
            tapCount = 1,
            lastTapTimestamp = System.currentTimeMillis()
        )
    }
    
    fun withDoubleTapRegistered(position: Offset, screenSize: androidx.compose.ui.geometry.Size): TapGestureState {
        return copy(
            tapPosition = position,
            tapRegion = calculateTapRegion(position, screenSize),
            tapCount = 2,
            isDoubleTapDetected = true,
            lastTapTimestamp = System.currentTimeMillis()
        )
    }
    
    fun withTripleTapRegistered(): TapGestureState {
        return copy(
            tapCount = 3,
            lastTapTimestamp = System.currentTimeMillis()
        )
    }
    
    fun withDoubleTapAction(action: DoubleTapAction): TapGestureState {
        return copy(doubleTapAction = action)
    }
    
    fun withGestureVelocity(velocity: Float): TapGestureState {
        return copy(gestureVelocity = velocity)
    }
    
    fun resetTapCounter(): TapGestureState {
        return copy(
            tapCount = 0,
            isDoubleTapDetected = false
        )
    }
    
    fun shouldResetTapCounter(timeoutMs: Long = 300): Boolean {
        return System.currentTimeMillis() - lastTapTimestamp > timeoutMs
    }
    
    fun getActionForRegion(): DoubleTapAction {
        return when (tapRegion) {
            TapRegion.LeftThird -> DoubleTapAction.SEEK_BACKWARD
            TapRegion.RightThird -> DoubleTapAction.SEEK_FORWARD
            TapRegion.CenterThird -> doubleTapAction
            else -> DoubleTapAction.PLAY_PAUSE
        }
    }
    
    private fun calculateTapRegion(
        position: Offset,
        screenSize: androidx.compose.ui.geometry.Size
    ): TapRegion {
        val x = position.x
        val y = position.y
        val thirdWidth = screenSize.width / 3
        
        return when {
            x < thirdWidth -> TapRegion.LeftThird
            x > thirdWidth * 2 -> TapRegion.RightThird
            else -> TapRegion.CenterThird
        }.also {
            if (y < screenSize.height / 2) {
                return when (it) {
                    TapRegion.LeftThird -> TapRegion.TopLeftThird
                    TapRegion.RightThird -> TapRegion.TopRightThird
                    TapRegion.CenterThird -> TapRegion.TopCenterThird
                    else -> it
                }
            }
        }
    }
}

enum class TapRegion {
    LeftThird,
    CenterThird,
    RightThird,
    TopLeftThird,
    TopCenterThird,
    TopRightThird
}

enum class DoubleTapAction {
    SEEK_FORWARD_BACKWARD,
    SEEK_FORWARD,
    SEEK_BACKWARD,
    PLAY_PAUSE,
    ZOOM,
    NONE
}
