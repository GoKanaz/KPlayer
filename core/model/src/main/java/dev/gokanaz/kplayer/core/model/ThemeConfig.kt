package dev.gokanaz.kplayer.core.model

import dev.gokanaz.kplayer.core.model.ui.Font

enum class DarkMode {
    SYSTEM,
    LIGHT,
    DARK
}

enum class ThemeColor {
    DEFAULT,
    BLUE,
    GREEN,
    PURPLE,
    ORANGE,
    RED
}

data class ThemeConfig(
    val darkMode: DarkMode = DarkMode.SYSTEM,
    val themeColor: ThemeColor = ThemeColor.DEFAULT,
    val dynamicColorEnabled: Boolean = true,
    val fontScale: Float = 1.0f,
    val fontFamily: Font = Font.SYSTEM
)
