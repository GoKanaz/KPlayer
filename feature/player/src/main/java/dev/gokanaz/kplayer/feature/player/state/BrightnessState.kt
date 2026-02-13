package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Immutable
data class BrightnessState(
    val currentBrightness: Float = 0.5f,
    val defaultBrightness: Float = 0.5f,
    val isAutoBrightnessEnabled: Boolean = false,
    val isOverlayVisible: Boolean = false,
    val temporaryBrightness: Float? = null,
    val mode: BrightnessMode = BrightnessMode.MANUAL
) {
    companion object {
        val Initial = BrightnessState()
        val Sample = BrightnessState(
            currentBrightness = 0.7f,
            defaultBrightness = 0.5f,
            isAutoBrightnessEnabled = false,
            mode = BrightnessMode.MANUAL
        )
    }
    
    fun withIncreasedBrightness(step: Float = 0.05f): BrightnessState {
        val newBrightness = (currentBrightness + step).coerceIn(0f, 1f)
        return copy(
            currentBrightness = newBrightness,
            temporaryBrightness = newBrightness,
            isOverlayVisible = true
        )
    }
    
    fun withDecreasedBrightness(step: Float = 0.05f): BrightnessState {
        val newBrightness = (currentBrightness - step).coerceIn(0f, 1f)
        return copy(
            currentBrightness = newBrightness,
            temporaryBrightness = newBrightness,
            isOverlayVisible = true
        )
    }
    
    fun withBrightness(brightness: Float): BrightnessState {
        val clamped = brightness.coerceIn(0f, 1f)
        return copy(
            currentBrightness = clamped,
            temporaryBrightness = clamped,
            isOverlayVisible = true
        )
    }
    
    fun withResetToDefault(): BrightnessState {
        return copy(
            currentBrightness = defaultBrightness,
            temporaryBrightness = null,
            isOverlayVisible = false
        )
    }
    
    fun withOverlayHidden(): BrightnessState {
        return copy(
            isOverlayVisible = false,
            temporaryBrightness = null
        )
    }
    
    fun withAutoBrightness(enabled: Boolean): BrightnessState {
        return copy(
            isAutoBrightnessEnabled = enabled,
            mode = if (enabled) BrightnessMode.AUTO else BrightnessMode.MANUAL
        )
    }
    
    fun withMode(mode: BrightnessMode): BrightnessState {
        return copy(
            mode = mode,
            isAutoBrightnessEnabled = mode == BrightnessMode.AUTO
        )
    }
}

enum class BrightnessMode {
    MANUAL,
    AUTO
}

class BrightnessStateHolder {
    private val _state = MutableStateFlow(BrightnessState.Initial)
    val state: StateFlow<BrightnessState> = _state.asStateFlow()
    
    fun update(block: (BrightnessState) -> BrightnessState) {
        _state.value = block(_state.value)
    }
}
