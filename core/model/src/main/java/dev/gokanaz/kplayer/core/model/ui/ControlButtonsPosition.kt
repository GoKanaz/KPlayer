package dev.gokanaz.kplayer.core.model.ui

enum class ControlButtonsPosition {
    BOTTOM,
    TOP,
    LEFT,
    RIGHT,
    OVERLAY
}

data class CustomControlPosition(
    val position: ControlButtonsPosition = ControlButtonsPosition.BOTTOM,
    val offsetX: Int = 0,
    val offsetY: Int = 0
)
