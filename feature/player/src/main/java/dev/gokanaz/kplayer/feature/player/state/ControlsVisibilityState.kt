package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class ControlsVisibilityState(
    val areControlsVisible: Boolean = false,
    val isAutoHideTimerActive: Boolean = false,
    val remainingTimeMs: Long = 3000,
    val isPlayerLocked: Boolean = false,
    val isTopBarVisible: Boolean = true,
    val isBottomBarVisible: Boolean = true,
    val isCenterControlsVisible: Boolean = true,
    val lastInteractionTimestamp: Long = 0L
) {
    companion object {
        val Initial = ControlsVisibilityState(
            areControlsVisible = false,
            isAutoHideTimerActive = false
        )
        
        val Visible = ControlsVisibilityState(
            areControlsVisible = true,
            isAutoHideTimerActive = true,
            remainingTimeMs = 3000,
            lastInteractionTimestamp = System.currentTimeMillis()
        )
        
        val Sample = ControlsVisibilityState(
            areControlsVisible = true,
            isAutoHideTimerActive = true,
            remainingTimeMs = 1500,
            isPlayerLocked = false,
            lastInteractionTimestamp = System.currentTimeMillis()
        )
    }
    
    fun withControlsShown(durationMs: Long = 3000): ControlsVisibilityState {
        return copy(
            areControlsVisible = true,
            isAutoHideTimerActive = true,
            remainingTimeMs = durationMs,
            lastInteractionTimestamp = System.currentTimeMillis()
        )
    }
    
    fun withControlsHidden(): ControlsVisibilityState {
        return copy(
            areControlsVisible = false,
            isAutoHideTimerActive = false,
            remainingTimeMs = 0
        )
    }
    
    fun withControlsToggled(): ControlsVisibilityState {
        return if (areControlsVisible) {
            withControlsHidden()
        } else {
            withControlsShown()
        }
    }
    
    fun withTimerReset(durationMs: Long = 3000): ControlsVisibilityState {
        return copy(
            isAutoHideTimerActive = true,
            remainingTimeMs = durationMs,
            lastInteractionTimestamp = System.currentTimeMillis()
        )
    }
    
    fun withTimerElapsed(elapsedMs: Long): ControlsVisibilityState {
        val newRemaining = (remainingTimeMs - elapsedMs).coerceAtLeast(0)
        return if (newRemaining <= 0) {
            copy(
                areControlsVisible = false,
                isAutoHideTimerActive = false,
                remainingTimeMs = 0
            )
        } else {
            copy(
                remainingTimeMs = newRemaining
            )
        }
    }
    
    fun withPlayerLocked(): ControlsVisibilityState {
        return copy(
            isPlayerLocked = true,
            areControlsVisible = false,
            isAutoHideTimerActive = false
        )
    }
    
    fun withPlayerUnlocked(): ControlsVisibilityState {
        return copy(
            isPlayerLocked = false
        )
    }
    
    fun withTopBarVisible(visible: Boolean): ControlsVisibilityState {
        return copy(isTopBarVisible = visible)
    }
    
    fun withBottomBarVisible(visible: Boolean): ControlsVisibilityState {
        return copy(isBottomBarVisible = visible)
    }
    
    fun withCenterControlsVisible(visible: Boolean): ControlsVisibilityState {
        return copy(isCenterControlsVisible = visible)
    }
    
    fun shouldAutoHide(): Boolean {
        return areControlsVisible && isAutoHideTimerActive && !isPlayerLocked
    }
}
