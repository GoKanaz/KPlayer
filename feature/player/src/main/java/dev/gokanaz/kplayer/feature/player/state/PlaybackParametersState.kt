package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class PlaybackParametersState(
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f,
    val isSkipSilenceEnabled: Boolean = false,
    val isAudioZoomEnabled: Boolean = false,
    val isSpeedChangedFromDefault: Boolean = false,
    val availableSpeedPresets: List<Float> = listOf(0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f),
    val customSpeed: Float? = null
) {
    companion object {
        val Initial = PlaybackParametersState()
        
        val Sample = PlaybackParametersState(
            speed = 1.5f,
            pitch = 1.0f,
            isSkipSilenceEnabled = false,
            isAudioZoomEnabled = false,
            isSpeedChangedFromDefault = true,
            customSpeed = null
        )
    }
    
    fun withSpeed(newSpeed: Float): PlaybackParametersState {
        val clampedSpeed = newSpeed.coerceIn(0.25f, 3.0f)
        return copy(
            speed = clampedSpeed,
            isSpeedChangedFromDefault = clampedSpeed != 1.0f,
            customSpeed = if (clampedSpeed !in availableSpeedPresets) clampedSpeed else null
        )
    }
    
    fun withIncreasedSpeed(step: Float = 0.25f): PlaybackParametersState {
        return withSpeed(speed + step)
    }
    
    fun withDecreasedSpeed(step: Float = 0.25f): PlaybackParametersState {
        return withSpeed(speed - step)
    }
    
    fun withResetToDefault(): PlaybackParametersState {
        return copy(
            speed = 1.0f,
            pitch = 1.0f,
            isSpeedChangedFromDefault = false,
            customSpeed = null
        )
    }
    
    fun withNextPreset(): PlaybackParametersState {
        val currentIndex = availableSpeedPresets.indexOf(speed)
        val nextIndex = (currentIndex + 1) % availableSpeedPresets.size
        return withSpeed(availableSpeedPresets[nextIndex])
    }
    
    fun withPreviousPreset(): PlaybackParametersState {
        val currentIndex = availableSpeedPresets.indexOf(speed)
        val previousIndex = if (currentIndex > 0) currentIndex - 1 else availableSpeedPresets.size - 1
        return withSpeed(availableSpeedPresets[previousIndex])
    }
    
    fun withPitch(newPitch: Float): PlaybackParametersState {
        return copy(
            pitch = newPitch.coerceIn(0.5f, 2.0f)
        )
    }
    
    fun withSkipSilenceEnabled(enabled: Boolean): PlaybackParametersState {
        return copy(isSkipSilenceEnabled = enabled)
    }
    
    fun withAudioZoomEnabled(enabled: Boolean): PlaybackParametersState {
        return copy(isAudioZoomEnabled = enabled)
    }
    
    fun formatSpeedForDisplay(): String {
        return if (speed % 1.0f == 0f) {
            "${speed.toInt()}x"
        } else {
            "${speed}x"
        }
    }
    
    fun getSpeedPercentage(): Int {
        return (speed * 100).toInt()
    }
}
