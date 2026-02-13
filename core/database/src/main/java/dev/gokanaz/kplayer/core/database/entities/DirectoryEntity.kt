package dev.gokanaz.kplayer.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "directories",
    indices = [
        Index(value = ["bucket_id"], unique = true),
        Index(value = ["name"]),
        Index(value = ["path"])
    ]
)
data class DirectoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "bucket_id")
    val bucketId: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "path")
    val path: String,
    
    @ColumnInfo(name = "date_added")
    val dateAdded: Long,
    
    @ColumnInfo(name = "date_modified")
    val dateModified: Long,
    
    @ColumnInfo(name = "media_count")
    var mediaCount: Int = 0,
    
    @ColumnInfo(name = "total_size")
    var totalSize: Long = 0L,
    
    @ColumnInfo(name = "total_duration")
    var totalDuration: Long = 0L
)
