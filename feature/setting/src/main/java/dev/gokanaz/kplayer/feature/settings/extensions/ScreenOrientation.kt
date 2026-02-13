package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ScreenLockLandscape
import androidx.compose.material.icons.filled.ScreenLockPortrait
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.ScreenOrientation

fun ScreenOrientation.toDisplayName(context: Context): String {
    return when (this) {
        ScreenOrientation.SYSTEM -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_system)
        ScreenOrientation.PORTRAIT -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_portrait)
        ScreenOrientation.LANDSCAPE -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_landscape)
        ScreenOrientation.REVERSE_PORTRAIT -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_reverse_portrait)
        ScreenOrientation.REVERSE_LANDSCAPE -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_reverse_landscape)
        ScreenOrientation.SENSOR -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_sensor)
        ScreenOrientation.FULL_SENSOR -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_full_sensor)
        ScreenOrientation.USER -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_user)
        ScreenOrientation.UNSPECIFIED -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_unspecified)
        ScreenOrientation.LOCKED -> context.getString(dev.gokanaz.kplayer.core.R.string.orientation_locked)
    }
}

fun ScreenOrientation.toIcon(): ImageVector {
    return when (this) {
        ScreenOrientation.SYSTEM -> Icons.Default.ScreenRotation
        ScreenOrientation.PORTRAIT -> Icons.Default.ScreenLockPortrait
        ScreenOrientation.LANDSCAPE -> Icons.Default.ScreenLockLandscape
        ScreenOrientation.REVERSE_PORTRAIT -> Icons.Default.ScreenLockPortrait
        ScreenOrientation.REVERSE_LANDSCAPE -> Icons.Default.ScreenLockLandscape
        ScreenOrientation.SENSOR -> Icons.Default.ScreenRotation
        ScreenOrientation.FULL_SENSOR -> Icons.Default.ScreenRotation
        ScreenOrientation.USER -> Icons.Default.ScreenRotation
        ScreenOrientation.UNSPECIFIED -> Icons.Default.ScreenRotation
        ScreenOrientation.LOCKED -> Icons.Default.ScreenRotation
    }
}

fun ScreenOrientation.toActivityOrientation(): Int {
    return when (this) {
        ScreenOrientation.SYSTEM -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        ScreenOrientation.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ScreenOrientation.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        ScreenOrientation.REVERSE_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
        ScreenOrientation.REVERSE_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        ScreenOrientation.SENSOR -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
        ScreenOrientation.FULL_SENSOR -> ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        ScreenOrientation.USER -> ActivityInfo.SCREEN_ORIENTATION_USER
        ScreenOrientation.UNSPECIFIED -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        ScreenOrientation.LOCKED -> ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }
}

fun ScreenOrientation.isLandscape(): Boolean {
    return this == ScreenOrientation.LANDSCAPE || 
           this == ScreenOrientation.REVERSE_LANDSCAPE
}

fun ScreenOrientation.isPortrait(): Boolean {
    return this == ScreenOrientation.PORTRAIT || 
           this == ScreenOrientation.REVERSE_PORTRAIT
}

fun ScreenOrientation.getAspectRatio(): Float {
    return when (this) {
        ScreenOrientation.PORTRAIT, ScreenOrientation.REVERSE_PORTRAIT -> 9f / 16f
        ScreenOrientation.LANDSCAPE, ScreenOrientation.REVERSE_LANDSCAPE -> 16f / 9f
        else -> 1f
    }
}

fun ScreenOrientation.next(): ScreenOrientation {
    val values = ScreenOrientation.entries
    val currentIndex = values.indexOf(this)
    return values[(currentIndex + 1) % values.size]
}
