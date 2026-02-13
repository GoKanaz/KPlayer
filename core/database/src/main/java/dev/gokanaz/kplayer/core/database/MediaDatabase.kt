package dev.gokanaz.kplayer.core.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import dev.gokanaz.kplayer.core.database.converter.UriListConverter
import dev.gokanaz.kplayer.core.database.dao.DirectoryDao
import dev.gokanaz.kplayer.core.database.dao.MediumDao
import dev.gokanaz.kplayer.core.database.dao.MediumStateDao
import dev.gokanaz.kplayer.core.database.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        DirectoryEntity::class,
        MediumEntity::class,
        MediumStateEntity::class,
        VideoStreamInfoEntity::class,
        AudioStreamInfoEntity::class,
        SubtitleStreamInfoEntity::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(UriListConverter::class)
abstract class MediaDatabase : RoomDatabase() {
    
    abstract fun directoryDao(): DirectoryDao
    abstract fun mediumDao(): MediumDao
    abstract fun mediumStateDao(): MediumStateDao
    
    companion object {
        @Volatile
        private var INSTANCE: MediaDatabase? = null
        
        fun getInstance(context: Context, scope: CoroutineScope): MediaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MediaDatabase::class.java,
                    "kplayer_media_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database ->
                            scope.launch {
                                populateInitialData(database)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        private suspend fun populateInitialData(database: MediaDatabase) {
        }
        
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `video_stream_info` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `medium_id` TEXT NOT NULL,
                        `stream_index` INTEGER NOT NULL,
                        `codec` TEXT,
                        `profile` TEXT,
                        `level` INTEGER,
                        `bitrate` INTEGER,
                        `width` INTEGER NOT NULL,
                        `height` INTEGER NOT NULL,
                        `frame_rate` REAL,
                        `display_aspect_ratio` TEXT,
                        `pixel_aspect_ratio` TEXT,
                        `rotation` INTEGER NOT NULL DEFAULT 0,
                        `is_default` INTEGER NOT NULL DEFAULT 0,
                        `is_forced` INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(`medium_id`) REFERENCES `media`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `audio_stream_info` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `medium_id` TEXT NOT NULL,
                        `stream_index` INTEGER NOT NULL,
                        `codec` TEXT,
                        `profile` TEXT,
                        `bitrate` INTEGER,
                        `sample_rate` INTEGER,
                        `channels` INTEGER NOT NULL,
                        `channel_mask` INTEGER,
                        `language` TEXT,
                        `is_default` INTEGER NOT NULL DEFAULT 0,
                        `is_forced` INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(`medium_id`) REFERENCES `media`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `subtitle_stream_info` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `medium_id` TEXT NOT NULL,
                        `stream_index` INTEGER NOT NULL,
                        `codec` TEXT,
                        `language` TEXT,
                        `title` TEXT,
                        `is_default` INTEGER NOT NULL DEFAULT 0,
                        `is_forced` INTEGER NOT NULL DEFAULT 0,
                        `is_external` INTEGER NOT NULL DEFAULT 0,
                        `external_path` TEXT,
                        FOREIGN KEY(`medium_id`) REFERENCES `media`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE 
                    )
                """)
                
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_video_stream_info_medium_id` ON `video_stream_info` (`medium_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_audio_stream_info_medium_id` ON `audio_stream_info` (`medium_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_subtitle_stream_info_medium_id` ON `subtitle_stream_info` (`medium_id`)")
            }
        }
        
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `media` ADD COLUMN `thumbnail` BLOB")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_is_video` ON `media` (`is_video`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_is_audio` ON `media` (`is_audio`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_duration` ON `media` (`duration`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_size` ON `media` (`size`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_resolution` ON `media` (`width`, `height`)")
            }
        }
    }
}
