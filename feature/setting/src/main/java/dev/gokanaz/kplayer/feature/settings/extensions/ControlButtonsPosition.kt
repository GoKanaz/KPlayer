package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.player.ScreenOrientation
import dev.gokanaz.kplayer.core.model.ui.ControlButtonsPosition
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.feature.settings.R

fun ControlButtonsPosition.toDisplayName(context: Context): String {
    return when (this) {
        ControlButtonsPosition.BOTTOM -> context.getString(R.string.control_position_bottom)
        ControlButtonsPosition.TOP -> context.getString(R.string.control_position_top)
        ControlButtonsPosition.LEFT -> context.getString(R.string.control_position_left)
        ControlButtonsPosition.RIGHT -> context.getString(R.string.control_position_right)
        ControlButtonsPosition.OVERLAY -> context.getString(R.string.control_position_overlay)
    }
}

fun ControlButtonsPosition.toIcon(): ImageVector {
    return when (this) {
        ControlButtonsPosition.BOTTOM -> NextIcon.VerticalAlignBottom.filled
        ControlButtonsPosition.TOP -> NextIcon.VerticalAlignTop.filled
        ControlButtonsPosition.LEFT -> NextIcon.AlignHorizontalLeft.filled
        ControlButtonsPosition.RIGHT -> NextIcon.AlignHorizontalRight.filled
        ControlButtonsPosition.OVERLAY -> NextIcon.CenterFocusWeak.filled
    }
}

fun ControlButtonsPosition.getDescription(context: Context): String {
    return when (this) {
        ControlButtonsPosition.BOTTOM -> context.getString(R.string.control_position_bottom_desc)
        ControlButtonsPosition.TOP -> context.getString(R.string.control_position_top_desc)
        ControlButtonsPosition.LEFT -> context.getString(R.string.control_position_left_desc)
        ControlButtonsPosition.RIGHT -> context.getString(R.string.control_position_right_desc)
        ControlButtonsPosition.OVERLAY -> context.getString(R.string.control_position_overlay_desc)
    }
}

fun ControlButtonsPosition.next(): ControlButtonsPosition {
    val values = ControlButtonsPosition.values()
    val nextIndex = (this.ordinal + 1) % values.size
    return values[nextIndex]
}

fun ControlButtonsPosition.isValidForOrientation(orientation: ScreenOrientation): Boolean {
    return when (this) {
        ControlButtonsPosition.BOTTOM, ControlButtonsPosition.TOP -> true
        ControlButtonsPosition.LEFT, ControlButtonsPosition.RIGHT -> orientation.isLandscape()
        ControlButtonsPosition.OVERLAY -> true
    }
}
