package dev.gokanaz.kplayer.core.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import dev.gokanaz.kplayer.core.database.entities.DirectoryEntity
import dev.gokanaz.kplayer.core.database.entities.MediumEntity

data class DirectoryWithMedia(
    @Embedded
    val directory: DirectoryEntity,
    
    @Relation(
        parentColumn = "bucket_id",
        entityColumn = "bucket_id"
    )
    val media: List<MediumEntity>
)
