package dev.gokanaz.kplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "video_stream_info",
    foreignKeys = [
        ForeignKey(
            entity = MediumEntity::class,
            parentColumns = ["id"],
            childColumns = ["medium_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["medium_id"]),
        Index(value = ["stream_index"])
    ]
)
data class VideoStreamInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    
    @ColumnInfo(name = "medium_id")
    val mediumId: String,
    
    @ColumnInfo(name = "stream_index")
    val streamIndex: Int,
    
    @ColumnInfo(name = "codec")
    val codec: String?,
    
    @ColumnInfo(name = "profile")
    val profile: String?,
    
    @ColumnInfo(name = "level")
    val level: Int?,
    
    @ColumnInfo(name = "bitrate")
    val bitrate: Long?,
    
    @ColumnInfo(name = "width")
    val width: Int,
    
    @ColumnInfo(name = "height")
    val height: Int,
    
    @ColumnInfo(name = "frame_rate")
    val frameRate: Double?,
    
    @ColumnInfo(name = "display_aspect_ratio")
    val displayAspectRatio: String?,
    
    @ColumnInfo(name = "pixel_aspect_ratio")
    val pixelAspectRatio: String?,
    
    @ColumnInfo(name = "rotation")
    val rotation: Int = 0,
    
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    
    @ColumnInfo(name = "is_forced")
    val isForced: Boolean = false
)
