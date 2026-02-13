package dev.gokanaz.kplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "media",
    foreignKeys = [
        ForeignKey(
            entity = DirectoryEntity::class,
            parentColumns = ["bucket_id"],
            childColumns = ["bucket_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["uri"], unique = true),
        Index(value = ["bucket_id"]),
        Index(value = ["date_added"]),
        Index(value = ["mime_type"]),
        Index(value = ["is_video"]),
        Index(value = ["is_audio"])
    ]
)
data class MediumEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "uri")
    val uri: String,
    
    @ColumnInfo(name = "title")
    val title: String?,
    
    @ColumnInfo(name = "file_name")
    val fileName: String,
    
    @ColumnInfo(name = "file_path")
    val filePath: String,
    
    @ColumnInfo(name = "mime_type")
    val mimeType: String?,
    
    @ColumnInfo(name = "size")
    val size: Long,
    
    @ColumnInfo(name = "duration")
    val duration: Long,
    
    @ColumnInfo(name = "date_added")
    val dateAdded: Long,
    
    @ColumnInfo(name = "date_modified")
    val dateModified: Long,
    
    @ColumnInfo(name = "bucket_id")
    val bucketId: String,
    
    @ColumnInfo(name = "bucket_display_name")
    val bucketDisplayName: String?,
    
    @ColumnInfo(name = "width")
    val width: Int = 0,
    
    @ColumnInfo(name = "height")
    val height: Int = 0,
    
    @ColumnInfo(name = "resolution")
    val resolution: String?,
    
    @ColumnInfo(name = "is_video")
    val isVideo: Boolean = false,
    
    @ColumnInfo(name = "is_audio")
    val isAudio: Boolean = false,
    
    @ColumnInfo(name = "thumbnail", typeAffinity = ColumnInfo.BLOB)
    val thumbnail: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediumEntity

        if (id != other.id) return false
        if (uri != other.uri) return false
        if (title != other.title) return false
        if (fileName != other.fileName) return false
        if (filePath != other.filePath) return false
        if (mimeType != other.mimeType) return false
        if (size != other.size) return false
        if (duration != other.duration) return false
        if (dateAdded != other.dateAdded) return false
        if (dateModified != other.dateModified) return false
        if (bucketId != other.bucketId) return false
        if (bucketDisplayName != other.bucketDisplayName) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (resolution != other.resolution) return false
        if (isVideo != other.isVideo) return false
        if (isAudio != other.isAudio) return false
        if (thumbnail != null) {
            if (other.thumbnail == null) return false
            if (!thumbnail.contentEquals(other.thumbnail)) return false
        } else if (other.thumbnail != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + fileName.hashCode()
        result = 31 * result + filePath.hashCode()
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + size.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + dateAdded.hashCode()
        result = 31 * result + dateModified.hashCode()
        result = 31 * result + bucketId.hashCode()
        result = 31 * result + (bucketDisplayName?.hashCode() ?: 0)
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + (resolution?.hashCode() ?: 0)
        result = 31 * result + isVideo.hashCode()
        result = 31 * result + isAudio.hashCode()
        result = 31 * result + (thumbnail?.contentHashCode() ?: 0)
        return result
    }
}
