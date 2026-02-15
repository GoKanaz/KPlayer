package dev.gokanaz.kplayer.core.model

data class VideoFilter(
    val bucketId: String? = null,
    val minDuration: Long? = null,
    val maxDuration: Long? = null,
    val minSize: Long? = null,
    val maxSize: Long? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val isFavorite: Boolean? = null,
    val mimeType: String? = null
)
