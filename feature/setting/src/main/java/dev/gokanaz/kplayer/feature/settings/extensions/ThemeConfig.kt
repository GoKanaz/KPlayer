package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.DarkMode
import dev.gokanaz.kplayer.core.model.ThemeColor
import dev.gokanaz.kplayer.core.model.ThemeConfig

fun DarkMode.toDisplayName(context: Context): String {
    return when (this) {
        DarkMode.SYSTEM -> context.getString(dev.gokanaz.kplayer.core.R.string.dark_mode_system)
        DarkMode.LIGHT -> context.getString(dev.gokanaz.kplayer.core.R.string.dark_mode_light)
        DarkMode.DARK -> context.getString(dev.gokanaz.kplayer.core.R.string.dark_mode_dark)
    }
}

fun DarkMode.toIcon(): ImageVector {
    return when (this) {
        DarkMode.SYSTEM -> Icons.Default.BrightnessAuto
        DarkMode.LIGHT -> Icons.Default.Brightness5
        DarkMode.DARK -> Icons.Default.Brightness2
    }
}

fun DarkMode.isDark(uiMode: Int): Boolean {
    return when (this) {
        DarkMode.SYSTEM -> {
            when (uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        }
        DarkMode.LIGHT -> false
        DarkMode.DARK -> true
    }
}

fun ThemeColor.toDisplayName(context: Context): String {
    return when (this) {
        ThemeColor.DEFAULT -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_default)
        ThemeColor.BLUE -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_blue)
        ThemeColor.GREEN -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_green)
        ThemeColor.RED -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_red)
        ThemeColor.PURPLE -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_purple)
        ThemeColor.ORANGE -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_orange)
        ThemeColor.TEAL -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_teal)
        ThemeColor.PINK -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_pink)
        ThemeColor.BROWN -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_brown)
        ThemeColor.GRAY -> context.getString(dev.gokanaz.kplayer.core.R.string.theme_gray)
    }
}

fun ThemeColor.toColor(): Color {
    return when (this) {
        ThemeColor.DEFAULT -> Color(0xFF6200EE)
        ThemeColor.BLUE -> Color(0xFF2196F3)
        ThemeColor.GREEN -> Color(0xFF4CAF50)
        ThemeColor.RED -> Color(0xFFF44336)
        ThemeColor.PURPLE -> Color(0xFF9C27B0)
        ThemeColor.ORANGE -> Color(0xFFFF9800)
        ThemeColor.TEAL -> Color(0xFF009688)
        ThemeColor.PINK -> Color(0xFFE91E63)
        ThemeColor.BROWN -> Color(0xFF795548)
        ThemeColor.GRAY -> Color(0xFF9E9E9E)
    }
}

fun ThemeColor.toSeedColor(): Color {
    return toColor()
}

fun ThemeColor.getPreviewColors(): List<Color> {
    val primary = toColor()
    return listOf(
        primary,
        primary.copy(alpha = 0.7f),
        primary.copy(alpha = 0.5f),
        primary.copy(alpha = 0.3f),
        primary.copy(alpha = 0.1f)
    )
}

fun ThemeColor.toIcon(): ImageVector {
    return Icons.Default.Palette
}

fun ThemeConfig.toDisplaySummary(context: Context): String {
    val darkModeName = darkMode.toDisplayName(context)
    val themeColorName = themeColor.toDisplayName(context)
    return context.getString(
        dev.gokanaz.kplayer.core.R.string.theme_summary,
        darkModeName,
        themeColorName
    )
}

fun ThemeConfig.isUsingDynamicColor(): Boolean {
    return useDynamicColor
}

fun ThemeConfig.getEffectiveDarkMode(uiMode: Int): DarkMode {
    return if (useDynamicColor) {
        DarkMode.SYSTEM
    } else {
        darkMode
    }
}
