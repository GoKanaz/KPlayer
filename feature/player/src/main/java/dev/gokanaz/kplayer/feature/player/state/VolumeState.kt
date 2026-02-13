package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class VolumeState(
    val currentVolume: Float = 0.5f,
    val isMuted: Boolean = false,
    val deviceVolume: Int = 15,
    val maxDeviceVolume: Int = 30,
    val minDeviceVolume: Int = 0,
    val playerVolume: Float = 1.0f,
    val volumeStepSize: Float = 0.05f,
    val isVolumeChangedByGesture: Boolean = false,
    val temporaryVolume: Float? = null,
    val audioFocusState: AudioFocusState = AudioFocusState.Gained
) {
    companion object {
        val Initial = VolumeState()
        
        val Sample = VolumeState(
            currentVolume = 0.7f,
            isMuted = false,
            deviceVolume = 20,
            maxDeviceVolume = 30,
            playerVolume = 1.0f,
            audioFocusState = AudioFocusState.Gained
        )
        
        val Muted = VolumeState(
            currentVolume = 0.5f,
            isMuted = true,
            playerVolume = 0f
        )
    }
    
    fun withVolumeIncreased(step: Float = volumeStepSize): VolumeState {
        val newVolume = (currentVolume + step).coerceIn(0f, 1f)
        return copy(
            currentVolume = newVolume,
            temporaryVolume = newVolume,
            isVolumeChangedByGesture = true,
            isMuted = false,
            playerVolume = newVolume
        )
    }
    
    fun withVolumeDecreased(step: Float = volumeStepSize): VolumeState {
        val newVolume = (currentVolume - step).coerceIn(0f, 1f)
        return copy(
            currentVolume = newVolume,
            temporaryVolume = newVolume,
            isVolumeChangedByGesture = true,
            isMuted = false,
            playerVolume = newVolume
        )
    }
    
    fun withVolume(volume: Float): VolumeState {
        val clamped = volume.coerceIn(0f, 1f)
        return copy(
            currentVolume = clamped,
            temporaryVolume = clamped,
            isMuted = clamped <= 0f,
            playerVolume = if (clamped <= 0f) 0f else clamped
        )
    }
    
    fun withDeviceVolume(volume: Int): VolumeState {
        val clamped = volume.coerceIn(minDeviceVolume, maxDeviceVolume)
        val normalized = clamped.toFloat() / maxDeviceVolume.toFloat()
        return copy(
            deviceVolume = clamped,
            currentVolume = normalized,
            isMuted = normalized <= 0f
        )
    }
    
    fun withMuteToggled(): VolumeState {
        return if (isMuted) {
            copy(
                isMuted = false,
                playerVolume = temporaryVolume ?: currentVolume
            )
        } else {
            copy(
                isMuted = true,
                temporaryVolume = currentVolume,
                playerVolume = 0f
            )
        }
    }
    
    fun withMute(enabled: Boolean): VolumeState {
        return if (enabled == isMuted) {
            this
        } else {
            withMuteToggled()
        }
    }
    
    fun withDuckingApplied(): VolumeState {
        return copy(
            playerVolume = currentVolume * 0.3f,
            audioFocusState = AudioFocusState.LostTransientCanDuck
        )
    }
    
    fun withVolumeRestored(): VolumeState {
        return copy(
            playerVolume = currentVolume,
            audioFocusState = AudioFocusState.Gained
        )
    }
    
    fun withAudioFocusState(state: AudioFocusState): VolumeState {
        return copy(audioFocusState = state)
    }
    
    fun withGestureCompleted(): VolumeState {
        return copy(
            isVolumeChangedByGesture = false,
            temporaryVolume = null
        )
    }
    
    fun getVolumePercentage(): Int {
        return (currentVolume * 100).toInt()
    }
    
    fun getPlayerVolumePercentage(): Int {
        return (playerVolume * 100).toInt()
    }
}

enum class AudioFocusState {
    Gained,
    Lost,
    LostTransient,
    LostTransientCanDuck
}
