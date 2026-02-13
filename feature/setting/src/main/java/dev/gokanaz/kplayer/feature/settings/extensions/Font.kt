package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.gokanaz.kplayer.core.model.Font

fun Font.toDisplayName(context: Context): String {
    return when (this) {
        Font.SYSTEM -> context.getString(dev.gokanaz.kplayer.core.R.string.font_system)
        Font.SANS_SERIF -> context.getString(dev.gokanaz.kplayer.core.R.string.font_sans_serif)
        Font.SERIF -> context.getString(dev.gokanaz.kplayer.core.R.string.font_serif)
        Font.MONOSPACE -> context.getString(dev.gokanaz.kplayer.core.R.string.font_monospace)
    }
}

fun Font.toFontFamily(): FontFamily {
    return when (this) {
        Font.SYSTEM -> FontFamily.Default
        Font.SANS_SERIF -> FontFamily.SansSerif
        Font.SERIF -> FontFamily.Serif
        Font.MONOSPACE -> FontFamily.Monospace
    }
}

fun Font.toAndroidTypeface(context: Context): Typeface? {
    return when (this) {
        Font.SYSTEM -> Typeface.DEFAULT
        Font.SANS_SERIF -> Typeface.SANS_SERIF
        Font.SERIF -> Typeface.SERIF
        Font.MONOSPACE -> Typeface.MONOSPACE
    }
}

fun Font.getPreviewText(): String {
    return when (this) {
        Font.SYSTEM -> "The quick brown fox jumps over the lazy dog"
        Font.SANS_SERIF -> "The quick brown fox jumps over the lazy dog"
        Font.SERIF -> "The quick brown fox jumps over the lazy dog"
        Font.MONOSPACE -> "The quick brown fox jumps over the lazy dog"
    }
}

fun Font.isSystemFont(): Boolean {
    return this == Font.SYSTEM
}

fun Font.getFontWeight(): FontWeight {
    return when (this) {
        Font.SYSTEM -> FontWeight.Normal
        Font.SANS_SERIF -> FontWeight.Normal
        Font.SERIF -> FontWeight.Normal
        Font.MONOSPACE -> FontWeight.Normal
    }
}

@Composable
fun Font.toDisplayNameComposable(): String {
    val context = LocalContext.current
    return toDisplayName(context)
}
