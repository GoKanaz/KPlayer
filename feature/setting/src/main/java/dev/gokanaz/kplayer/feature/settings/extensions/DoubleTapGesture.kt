package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.player.DoubleTapGesture
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.feature.settings.R

fun DoubleTapGesture.toDisplayName(context: Context): String {
    return when (this) {
        DoubleTapGesture.SEEK_FORWARD_BACKWARD -> context.getString(R.string.double_tap_seek_both)
        DoubleTapGesture.SEEK_FORWARD -> context.getString(R.string.double_tap_seek_forward)
        DoubleTapGesture.SEEK_BACKWARD -> context.getString(R.string.double_tap_seek_backward)
        DoubleTapGesture.PLAY_PAUSE -> context.getString(R.string.double_tap_play_pause)
        DoubleTapGesture.ZOOM -> context.getString(R.string.double_tap_zoom)
        DoubleTapGesture.NONE -> context.getString(R.string.double_tap_none)
    }
}

fun DoubleTapGesture.toDescription(context: Context): String {
    return when (this) {
        DoubleTapGesture.SEEK_FORWARD_BACKWARD -> context.getString(R.string.double_tap_seek_both_desc)
        DoubleTapGesture.SEEK_FORWARD -> context.getString(R.string.double_tap_seek_forward_desc)
        DoubleTapGesture.SEEK_BACKWARD -> context.getString(R.string.double_tap_seek_backward_desc)
        DoubleTapGesture.PLAY_PAUSE -> context.getString(R.string.double_tap_play_pause_desc)
        DoubleTapGesture.ZOOM -> context.getString(R.string.double_tap_zoom_desc)
        DoubleTapGesture.NONE -> context.getString(R.string.double_tap_none_desc)
    }
}

fun DoubleTapGesture.toIcon(): ImageVector {
    return when (this) {
        DoubleTapGesture.SEEK_FORWARD_BACKWARD -> NextIcon.SwapHoriz.filled
        DoubleTapGesture.SEEK_FORWARD -> NextIcon.FastForward.filled
        DoubleTapGesture.SEEK_BACKWARD -> NextIcon.FastRewind.filled
        DoubleTapGesture.PLAY_PAUSE -> NextIcon.PlayCircle.filled
        DoubleTapGesture.ZOOM -> NextIcon.ZoomIn.filled
        DoubleTapGesture.NONE -> NextIcon.Cancel.filled
    }
}

fun DoubleTapGesture.getDefaultSeekDuration(): Int {
    return when (this) {
        DoubleTapGesture.SEEK_FORWARD_BACKWARD -> 10000
        DoubleTapGesture.SEEK_FORWARD -> 10000
        DoubleTapGesture.SEEK_BACKWARD -> 10000
        else -> 0
    }
}

fun DoubleTapGesture.isSeekAction(): Boolean {
    return this == DoubleTapGesture.SEEK_FORWARD_BACKWARD ||
           this == DoubleTapGesture.SEEK_FORWARD ||
           this == DoubleTapGesture.SEEK_BACKWARD
}
