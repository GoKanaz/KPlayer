package dev.gokanaz.kplayer.core.model.ui

enum class Font {
    SYSTEM,
    SANS_SERIF,
    SERIF,
    MONOSPACE
}

data class CustomFont(
    val fontFamily: Font = Font.SYSTEM,
    val fontPath: String = "",
    val weight: Int = 400,
    val isItalic: Boolean = false
)
