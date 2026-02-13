package dev.gokanaz.kplayer.feature.player.extensions

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.util.Rational
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import dev.gokanaz.kplayer.core.model.player.ScreenOrientation

fun Activity.hideSystemBars() {
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_FULLSCREEN
        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    )
}

fun Activity.showSystemBars() {
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    )
}

fun Activity.toggleSystemBars() {
    if (isInImmersiveMode()) {
        showSystemBars()
    } else {
        hideSystemBars()
    }
}

fun Activity.isInImmersiveMode(): Boolean {
    val visibility = window.decorView.systemUiVisibility
    return visibility and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY != 0
}

fun Activity.setKeepScreenOn(enabled: Boolean) {
    if (enabled) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

fun Activity.lockOrientation(orientation: ScreenOrientation) {
    requestedOrientation = orientation.toActivityOrientation()
}

fun Activity.unlockOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

fun Activity.getCurrentOrientation(): ScreenOrientation {
    return when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ScreenOrientation.LANDSCAPE
        Configuration.ORIENTATION_PORTRAIT -> ScreenOrientation.PORTRAIT
        else -> ScreenOrientation.SENSOR
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Activity.enterPipMode(aspectRatio: Rational, sourceRectHint: Rect? = null) {
    if (!supportsPip()) return
    
    val params = PictureInPictureParams.Builder()
        .setAspectRatio(aspectRatio)
        .apply {
            sourceRectHint?.let { setSourceRectHint(it) }
        }
        .build()
    
    enterPictureInPictureMode(params)
}

fun Activity.isInPipMode(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        isInPictureInPictureMode
    } else {
        false
    }
}

fun Activity.supportsPip(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        packageManager.hasSystemFeature(android.content.pm.PackageManager.FEATURE_PICTURE_IN_PICTURE)
    } else {
        false
    }
}

fun Activity.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Activity.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

fun Activity.getDisplayRotation(): Int {
    return windowManager.defaultDisplay.rotation
}
