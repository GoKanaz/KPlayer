package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import android.util.Rational

@Immutable
data class PictureInPictureState(
    val isInPipMode: Boolean = false,
    val isPipAvailable: Boolean = false,
    val isPipEnabledInSettings: Boolean = true,
    val isAutoEnterPipEnabled: Boolean = true,
    val aspectRatio: Rational = Rational(16, 9),
    val customActions: List<PipAction> = emptyList(),
    val lastPlaybackPosition: Long = 0,
    val isPipRequested: Boolean = false
) {
    companion object {
        val Initial = PictureInPictureState()
        
        val Sample = PictureInPictureState(
            isInPipMode = true,
            isPipAvailable = true,
            isPipEnabledInSettings = true,
            isAutoEnterPipEnabled = true,
            aspectRatio = Rational(16, 9),
            customActions = listOf(
                PipAction.PlayPause,
                PipAction.Next,
                PipAction.Previous
            ),
            lastPlaybackPosition = 125000
        )
    }
    
    fun withPipEntered(positionMs: Long): PictureInPictureState {
        return copy(
            isInPipMode = true,
            lastPlaybackPosition = positionMs,
            isPipRequested = false
        )
    }
    
    fun withPipExited(): PictureInPictureState {
        return copy(
            isInPipMode = false,
            isPipRequested = false
        )
    }
    
    fun withPipRequested(): PictureInPictureState {
        return copy(
            isPipRequested = true
        )
    }
    
    fun withPipAvailability(available: Boolean): PictureInPictureState {
        return copy(
            isPipAvailable = available
        )
    }
    
    fun withPipSettings(enabled: Boolean): PictureInPictureState {
        return copy(
            isPipEnabledInSettings = enabled
        )
    }
    
    fun withAutoEnterPip(enabled: Boolean): PictureInPictureState {
        return copy(
            isAutoEnterPipEnabled = enabled
        )
    }
    
    fun withAspectRatio(width: Int, height: Int): PictureInPictureState {
        return copy(
            aspectRatio = Rational(width, height)
        )
    }
    
    fun withCustomActions(actions: List<PipAction>): PictureInPictureState {
        return copy(
            customActions = actions
        )
    }
    
    fun shouldAutoEnterPip(): Boolean {
        return isPipAvailable && isPipEnabledInSettings && isAutoEnterPipEnabled
    }
    
    fun getPipActionsForMediaSession(): List<PipAction> {
        return customActions.ifEmpty {
            listOf(PipAction.PlayPause, PipAction.Next, PipAction.Previous)
        }
    }
}

enum class PipAction {
    PlayPause,
    Next,
    Previous,
    Rewind,
    FastForward,
    Close
}
