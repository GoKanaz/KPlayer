package dev.gokanaz.kplayer.core.database.dao

import androidx.room.*
import dev.gokanaz.kplayer.core.database.entities.MediumStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediumStateDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: MediumStateEntity)
    
    @Update
    suspend fun updateState(state: MediumStateEntity)
    
    @Delete
    suspend fun deleteState(state: MediumStateEntity)
    
    @Query("DELETE FROM media_state WHERE medium_id = :mediumId")
    suspend fun deleteStateByMediumId(mediumId: String)
    
    @Query("SELECT * FROM media_state WHERE medium_id = :mediumId")
    fun getStateByMediumId(mediumId: String): Flow<MediumStateEntity?>
    
    @Query("SELECT * FROM media_state WHERE is_favorite = 1 ORDER BY last_played_at DESC")
    fun getFavoriteMedia(): Flow<List<MediumStateEntity>>
    
    @Query("SELECT * FROM media_state ORDER BY watch_count DESC LIMIT :limit")
    fun getMostWatched(limit: Int = 20): Flow<List<MediumStateEntity>>
    
    @Query("SELECT * FROM media_state WHERE last_played_at IS NOT NULL ORDER BY last_played_at DESC LIMIT :limit")
    fun getRecentlyPlayed(limit: Int = 20): Flow<List<MediumStateEntity>>
    
    @Query("UPDATE media_state SET last_played_position = :position, last_played_at = :timestamp WHERE medium_id = :mediumId")
    suspend fun updatePlaybackPosition(mediumId: String, position: Long, timestamp: Long)
    
    @Query("UPDATE media_state SET watch_count = watch_count + 1 WHERE medium_id = :mediumId")
    suspend fun incrementWatchCount(mediumId: String)
    
    @Query("UPDATE media_state SET is_favorite = CASE WHEN is_favorite = 1 THEN 0 ELSE 1 END WHERE medium_id = :mediumId")
    suspend fun toggleFavorite(mediumId: String)
    
    @Query("UPDATE media_state SET is_favorite = 1 WHERE medium_id = :mediumId")
    suspend fun addToFavorite(mediumId: String)
    
    @Query("UPDATE media_state SET is_favorite = 0 WHERE medium_id = :mediumId")
    suspend fun removeFromFavorite(mediumId: String)
    
    @Query("UPDATE media_state SET tags = :tags WHERE medium_id = :mediumId")
    suspend fun updateTags(mediumId: String, tags: String?)
    
    @Query("UPDATE media_state SET playlist_ids = :playlistIds WHERE medium_id = :mediumId")
    suspend fun updatePlaylistIds(mediumId: String, playlistIds: String?)
    
    @Query("SELECT medium_id FROM media_state WHERE watch_count > 0")
    suspend fun getPlayedMediaIds(): List<String>
    
    @Query("SELECT COUNT(*) FROM media_state WHERE is_favorite = 1")
    suspend fun getFavoriteCount(): Int
    
    @Query("SELECT AVG(watch_count) FROM media_state")
    suspend fun getAverageWatchCount(): Double
    
    @Query("SELECT SUM(watch_count) FROM media_state")
    suspend fun getTotalWatchCount(): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStates(states: List<MediumStateEntity>)
    
    @Query("DELETE FROM media_state")
    suspend fun deleteAllStates()
    
    @Transaction
    suspend fun updatePlaybackAndIncrementWatch(mediumId: String, position: Long) {
        updatePlaybackPosition(mediumId, position, System.currentTimeMillis())
        incrementWatchCount(mediumId)
    }
}
