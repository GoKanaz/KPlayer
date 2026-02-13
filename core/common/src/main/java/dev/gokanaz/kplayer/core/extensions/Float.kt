package dev.gokanaz.kplayer.core.extensions

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun Float.toTimeString(): String {
    val totalSeconds = this.toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

fun Float.toPercentage(): String {
    return if (this in 0.0f..1.0f) {
        "${(this * 100).toInt()}%"
    } else {
        "${this.toInt()}%"
    }
}

fun Float.remap(fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float {
    val fromRange = fromMax - fromMin
    val toRange = toMax - toMin
    val normalized = (this - fromMin) / fromRange
    return toMin + (normalized * toRange)
}

fun Float.formatDecimal(digits: Int): String {
    return "%.${digits}f".format(this)
}

fun Float.isValidFloat(): Boolean {
    return !this.isNaN() && !this.isInfinite()
}

fun Float.lerp(to: Float, fraction: Float): Float {
    return this + (to - this) * fraction.coerceIn(0f, 1f)
}

fun Float.normalize(min: Float, max: Float): Float {
    return if (max > min) {
        (this - min) / (max - min)
    } else {
        0f
    }.coerceIn(0f, 1f)
}
