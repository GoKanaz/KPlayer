package dev.gokanaz.kplayer.feature.player.extensions

import java.util.concurrent.TimeUnit

fun Long.formatDuration(): String {
    if (this <= 0) return "00:00"
    
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun Long.formatDurationWithHours(): String {
    if (this <= 0) return "00:00:00"
    
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return String.format("%d:%02d:%02d", hours, minutes, seconds)
}

fun Long.formatDurationFromSeconds(): String {
    return (this * 1000).formatDuration()
}

fun Long.toHumanReadableDuration(): String {
    if (this <= 0) return "0s"
    
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    val parts = mutableListOf<String>()
    if (hours > 0) parts.add("${hours}h")
    if (minutes > 0) parts.add("${minutes}m")
    if (seconds > 0) parts.add("${seconds}s")
    
    return parts.joinToString(" ")
}

fun String.parseDuration(): Long {
    if (!this.isValidDurationFormat()) return 0
    
    val parts = this.split(":").map { it.toIntOrNull() ?: 0 }
    return when (parts.size) {
        3 -> TimeUnit.HOURS.toMillis(parts[0].toLong()) +
             TimeUnit.MINUTES.toMillis(parts[1].toLong()) +
             TimeUnit.SECONDS.toMillis(parts[2].toLong())
        2 -> TimeUnit.MINUTES.toMillis(parts[0].toLong()) +
             TimeUnit.SECONDS.toMillis(parts[1].toLong())
        else -> TimeUnit.SECONDS.toMillis(parts[0].toLong())
    }
}

fun String.isValidDurationFormat(): Boolean {
    val regex = Regex("^\\d{1,2}:\\d{2}(:\\d{2})?$")
    return regex.matches(this)
}

fun Long.toPositionPercentage(totalDuration: Long): Float {
    if (totalDuration <= 0 || this <= 0) return 0f
    return (this.toFloat() / totalDuration.toFloat()).coerceIn(0f, 1f)
}

fun Float.fromPercentageToPosition(totalDuration: Long): Long {
    return (this * totalDuration).toLong().coerceAtLeast(0)
}

fun Long.remainingDuration(totalDuration: Long): Long {
    return (totalDuration - this).coerceAtLeast(0)
}
