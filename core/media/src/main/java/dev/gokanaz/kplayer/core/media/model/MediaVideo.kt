package dev.gokanaz.kplayer.core.media.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaVideo(
    val id: Long,
    val title: String,
    val uri: Uri,
    val duration: Long,
    val size: Long,
    val dateAdded: Long,
    val dateModified: Long,
    val mimeType: String,
    val resolution: String,
    val bucketId: String,
    val bucketDisplayName: String
) : Parcelable
