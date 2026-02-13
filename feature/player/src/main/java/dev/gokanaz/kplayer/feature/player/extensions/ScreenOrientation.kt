package dev.gokanaz.kplayer.feature.player.extensions

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import dev.gokanaz.kplayer.core.model.player.ScreenOrientation

fun ScreenOrientation.toActivityOrientation(): Int {
    return when (this) {
        ScreenOrientation.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ScreenOrientation.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        ScreenOrientation.REVERSE_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        ScreenOrientation.SENSOR -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
        ScreenOrientation.SENSOR_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        ScreenOrientation.SYSTEM -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}

fun Int.toScreenOrientation(): ScreenOrientation {
    return when (this) {
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> ScreenOrientation.PORTRAIT
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> ScreenOrientation.LANDSCAPE
        ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE -> ScreenOrientation.REVERSE_LANDSCAPE
        ActivityInfo.SCREEN_ORIENTATION_SENSOR -> ScreenOrientation.SENSOR
        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> ScreenOrientation.SENSOR_LANDSCAPE
        else -> ScreenOrientation.SYSTEM
    }
}

fun Configuration.toScreenOrientation(): ScreenOrientation {
    return when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> ScreenOrientation.PORTRAIT
        Configuration.ORIENTATION_LANDSCAPE -> ScreenOrientation.LANDSCAPE
        else -> ScreenOrientation.SENSOR
    }
}

fun ScreenOrientation.isLandscape(): Boolean {
    return this == ScreenOrientation.LANDSCAPE || 
           this == ScreenOrientation.REVERSE_LANDSCAPE || 
           this == ScreenOrientation.SENSOR_LANDSCAPE
}

fun ScreenOrientation.isPortrait(): Boolean {
    return this == ScreenOrientation.PORTRAIT
}

fun ScreenOrientation.rotate(): ScreenOrientation {
    return when (this) {
        ScreenOrientation.PORTRAIT -> ScreenOrientation.LANDSCAPE
        ScreenOrientation.LANDSCAPE -> ScreenOrientation.REVERSE_LANDSCAPE
        ScreenOrientation.REVERSE_LANDSCAPE -> ScreenOrientation.PORTRAIT
        ScreenOrientation.SENSOR -> ScreenOrientation.SENSOR_LANDSCAPE
        ScreenOrientation.SENSOR_LANDSCAPE -> ScreenOrientation.SENSOR
        ScreenOrientation.SYSTEM -> ScreenOrientation.SYSTEM
    }
}

fun ScreenOrientation.getAspectRatio(): Float {
    return when (this) {
        ScreenOrientation.PORTRAIT -> 9f / 16f
        ScreenOrientation.LANDSCAPE -> 16f / 9f
        ScreenOrientation.REVERSE_LANDSCAPE -> 16f / 9f
        ScreenOrientation.SENSOR_LANDSCAPE -> 16f / 9f
        else -> 1f
    }
}
