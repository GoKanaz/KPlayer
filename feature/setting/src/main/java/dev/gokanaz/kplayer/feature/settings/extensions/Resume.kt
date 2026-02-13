package dev.gokanaz.kplayer.feature.settings.extensions

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.Resume

fun Resume.toDisplayName(context: Context): String {
    return when (this) {
        Resume.ALWAYS -> context.getString(dev.gokanaz.kplayer.core.R.string.resume_always)
        Resume.ASK -> context.getString(dev.gokanaz.kplayer.core.R.string.resume_ask)
        Resume.NEVER -> context.getString(dev.gokanaz.kplayer.core.R.string.resume_never)
    }
}

fun Resume.toDescription(context: Context): String {
    return when (this) {
        Resume.ALWAYS -> context.getString(dev.gokanaz.kplayer.core.R.string.resume_always_description)
        Resume.ASK -> context.getString(dev.gokanaz.kplayer.core.R.string.resume_ask_description)
        Resume.NEVER -> context.getString(dev.gokanaz.kplayer.core.R.string.resume_never_description)
    }
}

fun Resume.toIcon(): ImageVector {
    return when (this) {
        Resume.ALWAYS -> Icons.Default.PlayCircle
        Resume.ASK -> Icons.Default.Help
        Resume.NEVER -> Icons.Default.Album
    }
}

fun Resume.shouldResume(
    lastPlayedPosition: Long,
    duration: Long,
    thresholdMs: Long = getDefaultThreshold()
): Boolean {
    if (this == Resume.NEVER) return false
    if (lastPlayedPosition <= 0) return false
    
    val remaining = duration - lastPlayedPosition
    return when (this) {
        Resume.ALWAYS -> remaining > thresholdMs
        Resume.ASK -> remaining > thresholdMs
        Resume.NEVER -> false
    }
}

fun Resume.getDefaultThreshold(): Long {
    return when (this) {
        Resume.ALWAYS -> 30000L
        Resume.ASK -> 30000L
        Resume.NEVER -> 0L
    }
}

fun Resume.next(): Resume {
    return when (this) {
        Resume.ALWAYS -> Resume.ASK
        Resume.ASK -> Resume.NEVER
        Resume.NEVER -> Resume.ALWAYS
    }
}
