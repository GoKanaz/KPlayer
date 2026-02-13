package dev.gokanaz.kplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subtitle_stream_info",
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
        Index(value = ["language"]),
        Index(value = ["is_external"])
    ]
)
data class SubtitleStreamInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    
    @ColumnInfo(name = "medium_id")
    val mediumId: String,
    
    @ColumnInfo(name = "stream_index")
    val streamIndex: Int,
    
    @ColumnInfo(name = "codec")
    val codec: String?,
    
    @ColumnInfo(name = "language")
    val language: String?,
    
    @ColumnInfo(name = "title")
    val title: String?,
    
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean = false,
    
    @ColumnInfo(name = "is_forced")
    val isForced: Boolean = false,
    
    @ColumnInfo(name = "is_external")
    val isExternal: Boolean = false,
    
    @ColumnInfo(name = "external_path")
    val externalPath: String?
)
