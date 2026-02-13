package dev.gokanaz.kplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "media_state",
    foreignKeys = [
        ForeignKey(
            entity = MediumEntity::class,
            parentColumns = ["id"],
            childColumns = ["medium_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["medium_id"], unique = true),
        Index(value = ["is_favorite"]),
        Index(value = ["last_played_at"]),
        Index(value = ["watch_count"])
    ]
)
data class MediumStateEntity(
    @PrimaryKey
    @ColumnInfo(name = "medium_id")
    val mediumId: String,
    
    @ColumnInfo(name = "last_played_position")
    var lastPlayedPosition: Long = 0L,
    
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    
    @ColumnInfo(name = "watch_count")
    var watchCount: Int = 0,
    
    @ColumnInfo(name = "last_played_at")
    var lastPlayedAt: Long? = null,
    
    @ColumnInfo(name = "playlist_ids")
    var playlistIds: String? = null,
    
    @ColumnInfo(name = "tags")
    var tags: String? = null,
    
    @ColumnInfo(name = "custom_metadata")
    var customMetadata: String? = null
)
