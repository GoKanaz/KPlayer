package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.player.FastSeek
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.feature.settings.R

fun FastSeek.toDisplayName(context: Context): String {
    return when (this) {
        FastSeek.ALWAYS -> context.getString(R.string.fast_seek_always)
        FastSeek.WIFI_ONLY -> context.getString(R.string.fast_seek_wifi_only)
        FastSeek.NEVER -> context.getString(R.string.fast_seek_never)
    }
}

fun FastSeek.toDescription(context: Context): String {
    return when (this) {
        FastSeek.ALWAYS -> context.getString(R.string.fast_seek_always_desc)
        FastSeek.WIFI_ONLY -> context.getString(R.string.fast_seek_wifi_only_desc)
        FastSeek.NEVER -> context.getString(R.string.fast_seek_never_desc)
    }
}

fun FastSeek.toIcon(): ImageVector {
    return when (this) {
        FastSeek.ALWAYS -> NextIcon.FastForward.filled
        FastSeek.WIFI_ONLY -> NextIcon.Wifi.filled
        FastSeek.NEVER -> NextIcon.DoNotDisturb.filled
    }
}

fun FastSeek.shouldUseFastSeek(isWifiConnected: Boolean): Boolean {
    return when (this) {
        FastSeek.ALWAYS -> true
        FastSeek.WIFI_ONLY -> isWifiConnected
        FastSeek.NEVER -> false
    }
}

fun FastSeek.getKeyframeInterval(): Int {
    return when (this) {
        FastSeek.ALWAYS -> 1
        FastSeek.WIFI_ONLY -> 2
        FastSeek.NEVER -> 10
    }
}

fun FastSeek.isWifiConnected(connectivityManager: ConnectivityManager): Boolean {
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}
