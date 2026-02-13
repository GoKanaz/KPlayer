package dev.gokanaz.kplayer.core.database.dao

import androidx.room.*
import dev.gokanaz.kplayer.core.database.entities.DirectoryEntity
import dev.gokanaz.kplayer.core.database.relations.DirectoryWithMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface DirectoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirectory(directory: DirectoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirectories(directories: List<DirectoryEntity>)
    
    @Update
    suspend fun updateDirectory(directory: DirectoryEntity)
    
    @Update
    suspend fun updateDirectories(directories: List<DirectoryEntity>)
    
    @Delete
    suspend fun deleteDirectory(directory: DirectoryEntity)
    
    @Delete
    suspend fun deleteDirectories(directories: List<DirectoryEntity>)
    
    @Query("DELETE FROM directories WHERE bucket_id = :bucketId")
    suspend fun deleteDirectoryByBucketId(bucketId: String)
    
    @Query("SELECT * FROM directories ORDER BY name ASC")
    fun getAllDirectories(): Flow<List<DirectoryEntity>>
    
    @Query("SELECT * FROM directories ORDER BY date_added DESC")
    fun getAllDirectoriesByDate(): Flow<List<DirectoryEntity>>
    
    @Query("SELECT * FROM directories WHERE bucket_id = :bucketId")
    suspend fun getDirectoryByBucketId(bucketId: String): DirectoryEntity?
    
    @Query("SELECT * FROM directories WHERE path = :path")
    suspend fun getDirectoryByPath(path: String): DirectoryEntity?
    
    @Query("SELECT * FROM directories WHERE name LIKE '%' || :query || '%'")
    fun searchDirectories(query: String): Flow<List<DirectoryEntity>>
    
    @Transaction
    @Query("SELECT * FROM directories WHERE bucket_id = :bucketId")
    suspend fun getDirectoryWithMedia(bucketId: String): DirectoryWithMedia?
    
    @Transaction
    @Query("SELECT * FROM directories")
    fun getAllDirectoriesWithMedia(): Flow<List<DirectoryWithMedia>>
    
    @Query("UPDATE directories SET media_count = :mediaCount WHERE bucket_id = :bucketId")
    suspend fun updateMediaCount(bucketId: String, mediaCount: Int)
    
    @Query("UPDATE directories SET total_size = :totalSize WHERE bucket_id = :bucketId")
    suspend fun updateTotalSize(bucketId: String, totalSize: Long)
    
    @Query("UPDATE directories SET total_duration = :totalDuration WHERE bucket_id = :bucketId")
    suspend fun updateTotalDuration(bucketId: String, totalDuration: Long)
    
    @Transaction
    suspend fun updateDirectoryStatistics(bucketId: String, mediaCount: Int, totalSize: Long, totalDuration: Long) {
        updateMediaCount(bucketId, mediaCount)
        updateTotalSize(bucketId, totalSize)
        updateTotalDuration(bucketId, totalDuration)
    }
    
    @Query("SELECT COUNT(*) FROM directories")
    suspend fun getDirectoriesCount(): Int
    
    @Query("SELECT SUM(media_count) FROM directories")
    suspend fun getTotalMediaCount(): Int
    
    @Query("SELECT SUM(total_size) FROM directories")
    suspend fun getTotalSize(): Long
    
    @Query("SELECT SUM(total_duration) FROM directories")
    suspend fun getTotalDuration(): Long
    
    @Query("SELECT * FROM directories WHERE bucket_id IN (:bucketIds)")
    suspend fun getDirectoriesByBucketIds(bucketIds: List<String>): List<DirectoryEntity>
    
    @Query("SELECT DISTINCT bucket_id FROM directories")
    suspend fun getAllBucketIds(): List<String>
}
