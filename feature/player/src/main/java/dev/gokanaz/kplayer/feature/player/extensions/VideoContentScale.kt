package dev.gokanaz.kplayer.feature.player.extensions

import android.widget.ImageView
import androidx.annotation.IntDef
import androidx.media3.common.C
import dev.gokanaz.kplayer.core.model.player.VideoContentScale

@IntDef(
    C.VIDEO_SCALING_MODE_DEFAULT,
    C.VIDEO_SCALING_MODE_SCALE_TO_FIT,
    C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
)
@Retention(AnnotationRetention.SOURCE)
annotation class AspectRatioScaling

fun VideoContentScale.toAspectRatioScaling(): @AspectRatioScaling Int {
    return when (this) {
        VideoContentScale.FIT -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        VideoContentScale.FILL -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        VideoContentScale.ZOOM -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        VideoContentScale.STRETCH -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        VideoContentScale.CROP -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        VideoContentScale.ORIGINAL -> C.VIDEO_SCALING_MODE_DEFAULT
    }
}

fun VideoContentScale.toScaleType(): ImageView.ScaleType {
    return when (this) {
        VideoContentScale.FIT -> ImageView.ScaleType.FIT_CENTER
        VideoContentScale.FILL -> ImageView.ScaleType.CENTER_CROP
        VideoContentScale.ZOOM -> ImageView.ScaleType.CENTER_CROP
        VideoContentScale.STRETCH -> ImageView.ScaleType.FIT_XY
        VideoContentScale.CROP -> ImageView.ScaleType.CENTER_CROP
        VideoContentScale.ORIGINAL -> ImageView.ScaleType.CENTER
    }
}

fun VideoContentScale.calculateDimensions(
    containerWidth: Int,
    containerHeight: Int,
    videoWidth: Int,
    videoHeight: Int
): Pair<Int, Int> {
    if (videoWidth <= 0 || videoHeight <= 0) {
        return Pair(containerWidth, containerHeight)
    }
    
    val videoAspectRatio = videoWidth.toFloat() / videoHeight.toFloat()
    val containerAspectRatio = containerWidth.toFloat() / containerHeight.toFloat()
    
    return when (this) {
        VideoContentScale.FIT -> {
            if (videoAspectRatio > containerAspectRatio) {
                Pair(containerWidth, (containerWidth / videoAspectRatio).toInt())
            } else {
                Pair((containerHeight * videoAspectRatio).toInt(), containerHeight)
            }
        }
        VideoContentScale.FILL, VideoContentScale.ZOOM, VideoContentScale.CROP -> {
            if (videoAspectRatio > containerAspectRatio) {
                Pair((containerHeight * videoAspectRatio).toInt(), containerHeight)
            } else {
                Pair(containerWidth, (containerWidth / videoAspectRatio).toInt())
            }
        }
        VideoContentScale.STRETCH -> Pair(containerWidth, containerHeight)
        VideoContentScale.ORIGINAL -> Pair(videoWidth, videoHeight)
    }
}

fun VideoContentScale.isFillMode(): Boolean {
    return this == VideoContentScale.FILL || 
           this == VideoContentScale.ZOOM || 
           this == VideoContentScale.CROP
}

fun VideoContentScale.isFitMode(): Boolean {
    return this == VideoContentScale.FIT || this == VideoContentScale.STRETCH
}
