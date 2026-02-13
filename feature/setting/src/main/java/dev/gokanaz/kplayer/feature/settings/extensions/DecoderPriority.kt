package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.player.DecoderPriority
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.feature.settings.R

fun DecoderPriority.toDisplayName(context: Context): String {
    return when (this) {
        DecoderPriority.HARDWARE -> context.getString(R.string.decoder_hardware)
        DecoderPriority.SOFTWARE -> context.getString(R.string.decoder_software)
        DecoderPriority.AUTO -> context.getString(R.string.decoder_auto)
    }
}

fun DecoderPriority.toDescription(context: Context): String {
    return when (this) {
        DecoderPriority.HARDWARE -> context.getString(R.string.decoder_hardware_desc)
        DecoderPriority.SOFTWARE -> context.getString(R.string.decoder_software_desc)
        DecoderPriority.AUTO -> context.getString(R.string.decoder_auto_desc)
    }
}

fun DecoderPriority.toIcon(): ImageVector {
    return when (this) {
        DecoderPriority.HARDWARE -> NextIcon.Memory.filled
        DecoderPriority.SOFTWARE -> NextIcon.Code.filled
        DecoderPriority.AUTO -> NextIcon.Settings.filled
    }
}

fun DecoderPriority.getRecommendedForDevice(): DecoderPriority {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> DecoderPriority.HARDWARE
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> DecoderPriority.AUTO
        else -> DecoderPriority.SOFTWARE
    }
}

fun DecoderPriority.requiresRestart(): Boolean {
    return this != DecoderPriority.AUTO
}
