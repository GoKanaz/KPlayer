package dev.gokanaz.kplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "audio_stream_info",
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
        Index(value = ["stream_index"]),
        Index(value = ["language"])
    ]
)
data class AudioStreamInfoEntity(
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
    
    @ColumnInfo(name = "bitrate")
    val bitrate: Long?,
    
    @ColumnInfo(name = "sample_rate")
    val sampleRate: Int?,
    
    @ColumnInfo(name = "channels")
    val channels: Int,
    
    @ColumnInfo(name = "channel_mask")
    val channelMask: Int?,
    
    @ColumnInfo(name = "language")
    val language: String?,
    
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    
    @ColumnInfo(name = "is_forced")
    val isForced: Boolean = false
)
