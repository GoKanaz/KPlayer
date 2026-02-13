package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class SeekGestureState(
    val isSeekingInProgress: Boolean = false,
    val startPositionMs: Long = 0,
    val currentSeekPositionMs: Long = 0,
    val seekDeltaMs: Long = 0,
    val seekPercentage: Float = 0f,
    val isSeekingForward: Boolean = false,
    val isSeekingBackward: Boolean = false,
    val showSeekIndicator: Boolean = false,
    val seekSource: SeekSource = SeekSource.BUTTON,
    val seekDurationPreset: SeekDuration = SeekDuration.SECONDS_10
) {
    companion object {
        val Initial = SeekGestureState()
        
        val Sample = SeekGestureState(
            isSeekingInProgress = true,
            startPositionMs = 120000,
            currentSeekPositionMs = 130000,
            seekDeltaMs = 10000,
            seekPercentage = 0.5f,
            isSeekingForward = true,
            isSeekingBackward = false,
            showSeekIndicator = true,
            seekSource = SeekSource.DOUBLE_TAP,
            seekDurationPreset = SeekDuration.SECONDS_10
        )
    }
    
    fun withSeekStarted(positionMs: Long, source: SeekSource): SeekGestureState {
        return copy(
            isSeekingInProgress = true,
            startPositionMs = positionMs,
            currentSeekPositionMs = positionMs,
            seekDeltaMs = 0,
            seekPercentage = 0f,
            showSeekIndicator = true,
            seekSource = source
        )
    }
    
    fun withSeekForward(deltaMs: Long = seekDurationPreset.milliseconds): SeekGestureState {
        return copy(
            currentSeekPositionMs = (currentSeekPositionMs + deltaMs).coerceAtLeast(0),
            seekDeltaMs = deltaMs,
            isSeekingForward = true,
            isSeekingBackward = false,
            showSeekIndicator = true
        )
    }
    
    fun withSeekBackward(deltaMs: Long = seekDurationPreset.milliseconds): SeekGestureState {
        return copy(
            currentSeekPositionMs = (currentSeekPositionMs - deltaMs).coerceAtLeast(0),
            seekDeltaMs = -deltaMs,
            isSeekingForward = false,
            isSeekingBackward = true,
            showSeekIndicator = true
        )
    }
    
    fun withSeekCompleted(): SeekGestureState {
        return copy(
            isSeekingInProgress = false,
            showSeekIndicator = false,
            seekDeltaMs = 0,
            seekPercentage = 0f
        )
    }
    
    fun withSeekCancelled(): SeekGestureState {
        return copy(
            isSeekingInProgress = false,
            currentSeekPositionMs = startPositionMs,
            showSeekIndicator = false,
            seekDeltaMs = 0,
            seekPercentage = 0f
        )
    }
    
    fun withSeekPercentage(percentage: Float): SeekGestureState {
        return copy(
            seekPercentage = percentage.coerceIn(0f, 1f),
            currentSeekPositionMs = (percentage * Long.MAX_VALUE).toLong()
        )
    }
    
    fun withSeekDurationPreset(duration: SeekDuration): SeekGestureState {
        return copy(seekDurationPreset = duration)
    }
    
    fun formatSeekTime(): String {
        val delta = if (isSeekingForward) seekDeltaMs else -seekDeltaMs
        val sign = if (delta > 0) "+" else "-"
        val absDelta = kotlin.math.abs(delta)
        return "$sign${absDelta.formatDuration()}"
    }
    
    fun getSeekColor(): SeekColor {
        return when {
            isSeekingForward -> SeekColor.FORWARD
            isSeekingBackward -> SeekColor.BACKWARD
            else -> SeekColor.NEUTRAL
        }
    }
}

enum class SeekSource {
    BUTTON,
    DOUBLE_TAP,
    SWIPE,
    GESTURE,
    KEYBOARD,
    REMOTE
}

enum class SeekDuration(val milliseconds: Long) {
    SECONDS_5(5000),
    SECONDS_10(10000),
    SECONDS_15(15000),
    SECONDS_30(30000);
    
    companion object {
        fun fromMilliseconds(ms: Long): SeekDuration {
            return values().find { it.milliseconds == ms } ?: SECONDS_10
        }
    }
}

enum class SeekColor {
    FORWARD,
    BACKWARD,
    NEUTRAL
}
