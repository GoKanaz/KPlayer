package dev.gokanaz.kplayer.core.database.dao

import androidx.room.*
import dev.gokanaz.kplayer.core.database.entities.MediumEntity
import dev.gokanaz.kplayer.core.database.relations.MediumWithInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MediumDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedium(medium: MediumEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: List<MediumEntity>)
    
    @Update
    suspend fun updateMedium(medium: MediumEntity)
    
    @Update
    suspend fun updateMedia(media: List<MediumEntity>)
    
    @Delete
    suspend fun deleteMedium(medium: MediumEntity)
    
    @Delete
    suspend fun deleteMedia(media: List<MediumEntity>)
    
    @Query("DELETE FROM media WHERE id = :id")
    suspend fun deleteMediumById(id: String)
    
    @Query("DELETE FROM media WHERE bucket_id = :bucketId")
    suspend fun deleteMediaByBucketId(bucketId: String)
    
    @Query("SELECT * FROM media WHERE id = :id")
    fun getMediumById(id: String): Flow<MediumEntity?>
    
    @Query("SELECT * FROM media WHERE uri = :uri")
    suspend fun getMediumByUri(uri: String): MediumEntity?
    
    @Query("SELECT * FROM media WHERE bucket_id = :bucketId")
    fun getMediaByBucketId(bucketId: String): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE is_video = 1")
    fun getAllVideos(): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE is_audio = 1")
    fun getAllAudios(): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE mime_type LIKE '%' || :mimeType || '%'")
    fun getMediaByMimeType(mimeType: String): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE title LIKE '%' || :query || '%' OR file_name LIKE '%' || :query || '%'")
    fun searchMedia(query: String): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media ORDER BY date_added DESC LIMIT :limit")
    fun getRecentMedia(limit: Int = 20): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE date_added BETWEEN :startDate AND :endDate")
    fun getMediaByDateRange(startDate: Long, endDate: Long): Flow<List<MediumEntity>>
    
    @Transaction
    @Query("SELECT * FROM media WHERE id = :id")
    fun getMediumWithInfo(id: String): Flow<MediumWithInfo?>
    
    @Transaction
    @Query("SELECT * FROM media")
    fun getAllMediaWithInfo(): Flow<List<MediumWithInfo>>
    
    @Query("SELECT COUNT(*) FROM media")
    suspend fun getTotalMediaCount(): Int
    
    @Query("SELECT COUNT(*) FROM media WHERE is_video = 1")
    suspend fun getTotalVideoCount(): Int
    
    @Query("SELECT COUNT(*) FROM media WHERE is_audio = 1")
    suspend fun getTotalAudioCount(): Int
    
    @Query("SELECT SUM(size) FROM media")
    suspend fun getTotalMediaSize(): Long
    
    @Query("SELECT SUM(duration) FROM media")
    suspend fun getTotalMediaDuration(): Long
    
    @Query("SELECT SUM(size) FROM media WHERE bucket_id = :bucketId")
    suspend fun getDirectoryMediaSize(bucketId: String): Long
    
    @Query("SELECT SUM(duration) FROM media WHERE bucket_id = :bucketId")
    suspend fun getDirectoryMediaDuration(bucketId: String): Long
    
    @Query("SELECT COUNT(*) FROM media WHERE bucket_id = :bucketId")
    suspend fun getDirectoryMediaCount(bucketId: String): Int
    
    @Query("SELECT * FROM media WHERE duration > 0 ORDER BY duration ASC")
    fun getMediaByDuration(): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE size > 0 ORDER BY size DESC")
    fun getMediaBySize(): Flow<List<MediumEntity>>
    
    @Query("SELECT * FROM media WHERE width >= :minWidth AND height >= :minHeight")
    fun getMediaByResolution(minWidth: Int, minHeight: Int): Flow<List<MediumEntity>>
    
    @Transaction
    suspend fun deleteMediaAndUpdateDirectory(mediumId: String, bucketId: String) {
        deleteMediumById(mediumId)
        val mediaCount = getDirectoryMediaCount(bucketId)
        val totalSize = getDirectoryMediaSize(bucketId)
        val totalDuration = getDirectoryMediaDuration(bucketId)
    }
}
