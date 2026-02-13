package dev.gokanaz.kplayer.core.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import dev.gokanaz.kplayer.core.database.entities.MediumEntity
import dev.gokanaz.kplayer.core.database.entities.MediumStateEntity
import dev.gokanaz.kplayer.core.database.entities.VideoStreamInfoEntity
import dev.gokanaz.kplayer.core.database.entities.AudioStreamInfoEntity
import dev.gokanaz.kplayer.core.database.entities.SubtitleStreamInfoEntity

data class MediumWithInfo(
    @Embedded
    val medium: MediumEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "medium_id"
    )
    val state: MediumStateEntity?,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "medium_id"
    )
    val videoStreams: List<VideoStreamInfoEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "medium_id"
    )
    val audioStreams: List<AudioStreamInfoEntity>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "medium_id"
    )
    val subtitleStreams: List<SubtitleStreamInfoEntity>
)
