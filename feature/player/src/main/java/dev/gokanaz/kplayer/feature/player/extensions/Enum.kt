package dev.gokanaz.kplayer.feature.player.extensions

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.media3.common.C
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.model.player.VideoContentScale
import dev.gokanaz.kplayer.core.ui.R
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon

inline fun <reified T : Enum<T>> T.toDisplayName(context: Context): String {
    val resourceName = "${T::class.simpleName?.lowercase()}_${this.name.lowercase()}"
    val resId = context.resources.getIdentifier(resourceName, "string", context.packageName)
    return if (resId != 0) context.getString(resId) else this.name
}

fun <T : Enum<T>> T.toValue(): String {
    return this.name
}

inline fun <reified T : Enum<T>> String?.toEnumOrDefault(default: T): T {
    return try {
        this?.let { enumValueOf<T>(it) } ?: default
    } catch (e: IllegalArgumentException) {
        default
    }
}

fun LoopMode.toIcon(): ImageVector {
    return when (this) {
        LoopMode.NONE -> NextIcon.Repeat.outlined
        LoopMode.ONE -> NextIcon.RepeatOne.filled
        LoopMode.ALL -> NextIcon.Repeat.filled
    }
}

fun LoopMode.next(): LoopMode {
    return when (this) {
        LoopMode.NONE -> LoopMode.ONE
        LoopMode.ONE -> LoopMode.ALL
        LoopMode.ALL -> LoopMode.NONE
    }
}

fun SortType.toDisplayName(): String {
    return when (this) {
        SortType.NAME -> "Name"
        SortType.DATE -> "Date"
        SortType.SIZE -> "Size"
        SortType.DURATION -> "Duration"
        SortType.TYPE -> "Type"
        SortType.ARTIST -> "Artist"
        SortType.ALBUM -> "Album"
    }
}

enum class VideoQuality {
    AUTO,
    Q_144P,
    Q_240P,
    Q_360P,
    Q_480P,
    Q_720P,
    Q_1080P,
    Q_1440P,
    Q_2160P;
    
    companion object {
        fun fromHeight(height: Int): VideoQuality {
            return when {
                height <= 144 -> Q_144P
                height <= 240 -> Q_240P
                height <= 360 -> Q_360P
                height <= 480 -> Q_480P
                height <= 720 -> Q_720P
                height <= 1080 -> Q_1080P
                height <= 1440 -> Q_1440P
                height <= 2160 -> Q_2160P
                else -> AUTO
            }
        }
    }
}

fun VideoQuality.toResolution(): Int {
    return when (this) {
        VideoQuality.AUTO -> 0
        VideoQuality.Q_144P -> 144
        VideoQuality.Q_240P -> 240
        VideoQuality.Q_360P -> 360
        VideoQuality.Q_480P -> 480
        VideoQuality.Q_720P -> 720
        VideoQuality.Q_1080P -> 1080
        VideoQuality.Q_1440P -> 1440
        VideoQuality.Q_2160P -> 2160
    }
}

fun VideoQuality.next(): VideoQuality {
    val values = VideoQuality.values()
    val nextIndex = (this.ordinal + 1) % values.size
    return values[nextIndex]
}

fun VideoContentScale.toScaleType(): @C.VideoScalingMode Int {
    return when (this) {
        VideoContentScale.FIT -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        VideoContentScale.FILL -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        VideoContentScale.ZOOM -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        VideoContentScale.STRETCH -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        VideoContentScale.CROP -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        VideoContentScale.ORIGINAL -> C.VIDEO_SCALING_MODE_DEFAULT
    }
}
