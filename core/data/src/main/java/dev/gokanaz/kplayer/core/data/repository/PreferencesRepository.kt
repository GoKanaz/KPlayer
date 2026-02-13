package dev.gokanaz.kplayer.core.data.repository

import dev.gokanaz.kplayer.core.datastore.datasource.DarkMode
import dev.gokanaz.kplayer.core.datastore.datasource.AppLanguage
import dev.gokanaz.kplayer.core.datastore.datasource.ViewType
import dev.gokanaz.kplayer.core.datastore.datasource.VideoQuality
import dev.gokanaz.kplayer.core.datastore.datasource.RepeatMode
import dev.gokanaz.kplayer.core.datastore.datasource.PlaybackHistoryItem
import dev.gokanaz.kplayer.core.domain.SortType
import dev.gokanaz.kplayer.core.domain.SortOrder
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun observeDarkMode(): Flow<DarkMode>
    suspend fun setDarkMode(mode: DarkMode)
    
    fun observeDynamicColorEnabled(): Flow<Boolean>
    suspend fun setDynamicColorEnabled(enabled: Boolean)
    
    fun observeFontScale(): Flow<Int>
    suspend fun setFontScale(scale: Int)
    
    fun observeAppLanguage(): Flow<AppLanguage>
    suspend fun setAppLanguage(language: AppLanguage)
    
    fun isFirstRun(): Flow<Boolean>
    suspend fun setFirstRun(completed: Boolean)
    
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
    
    fun observeDefaultVideoSortType(): Flow<SortType>
    suspend fun setDefaultVideoSortType(sortType: SortType)
    
    fun observeDefaultSortOrder(): Flow<SortOrder>
    suspend fun setDefaultSortOrder(order: SortOrder)
    
    fun observeDefaultViewType(): Flow<ViewType>
    suspend fun setDefaultViewType(viewType: ViewType)
    
    fun isStoragePermissionGranted(): Flow<Boolean>
    suspend fun setStoragePermissionGranted(granted: Boolean)
    
    fun observeStoragePaths(): Flow<List<String>>
    suspend fun addStoragePath(path: String)
    suspend fun removeStoragePath(path: String)
    
    fun observePlaybackSpeed(): Flow<Float>
    suspend fun setPlaybackSpeed(speed: Float)
    
    fun observeDefaultQuality(): Flow<VideoQuality>
    suspend fun setDefaultQuality(quality: VideoQuality)
    
    fun observeAutoPlay(): Flow<Boolean>
    suspend fun setAutoPlay(enabled: Boolean)
    
    fun observeRepeatMode(): Flow<RepeatMode>
    suspend fun setRepeatMode(mode: RepeatMode)
    
    fun observeShuffleMode(): Flow<Boolean>
    suspend fun setShuffleMode(enabled: Boolean)
    
    fun observeVolume(): Flow<Float>
    suspend fun setVolume(volume: Float)
    
    fun observeMute(): Flow<Boolean>
    suspend fun setMute(muted: Boolean)
    
    fun observeEqualizerEnabled(): Flow<Boolean>
    suspend fun setEqualizerEnabled(enabled: Boolean)
    
    fun observeEqualizerBands(): Flow<List<Int>>
    suspend fun setEqualizerBands(bands: List<Int>)
    
    fun observeSubtitlesEnabled(): Flow<Boolean>
    suspend fun setSubtitlesEnabled(enabled: Boolean)
    
    fun observeSubtitleLanguage(): Flow<String>
    suspend fun setSubtitleLanguage(language: String)
    
    fun observeSubtitleSize(): Flow<Int>
    suspend fun setSubtitleSize(size: Int)
    
    fun observeSubtitleFont(): Flow<String>
    suspend fun setSubtitleFont(font: String)
    
    fun observeSeekGestureEnabled(): Flow<Boolean>
    suspend fun setSeekGestureEnabled(enabled: Boolean)
    
    fun observeVolumeGestureEnabled(): Flow<Boolean>
    suspend fun setVolumeGestureEnabled(enabled: Boolean)
    
    fun observeBrightnessGestureEnabled(): Flow<Boolean>
    suspend fun setBrightnessGestureEnabled(enabled: Boolean)
    
    fun observePipEnabled(): Flow<Boolean>
    suspend fun setPipEnabled(enabled: Boolean)
    
    fun observeBackgroundPlayEnabled(): Flow<Boolean>
    suspend fun setBackgroundPlayEnabled(enabled: Boolean)
    
    suspend fun saveResumePosition(videoId: String, positionMs: Long)
    suspend fun getResumePosition(videoId: String): Long?
    suspend fun clearResumePosition(videoId: String)
    fun observeResumePositions(): Flow<Map<String, Long>>
    
    suspend fun addToHistory(item: PlaybackHistoryItem)
    suspend fun clearHistory()
    fun observeHistory(): Flow<List<PlaybackHistoryItem>>
    
    suspend fun clearAllPreferences()
}
