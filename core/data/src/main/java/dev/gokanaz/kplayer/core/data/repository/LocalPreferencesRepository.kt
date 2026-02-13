package dev.gokanaz.kplayer.core.data.repository

import dev.gokanaz.kplayer.core.datastore.datasource.AppPreferencesDataSource
import dev.gokanaz.kplayer.core.datastore.datasource.DarkMode
import dev.gokanaz.kplayer.core.datastore.datasource.AppLanguage
import dev.gokanaz.kplayer.core.datastore.datasource.ViewType
import dev.gokanaz.kplayer.core.datastore.datasource.VideoQuality
import dev.gokanaz.kplayer.core.datastore.datasource.RepeatMode
import dev.gokanaz.kplayer.core.datastore.datasource.PlaybackHistoryItem
import dev.gokanaz.kplayer.core.datastore.datasource.PlayerPreferencesDataSource
import dev.gokanaz.kplayer.core.domain.SortType
import dev.gokanaz.kplayer.core.domain.SortOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPreferencesRepository @Inject constructor(
    private val appPreferences: AppPreferencesDataSource,
    private val playerPreferences: PlayerPreferencesDataSource
) : PreferencesRepository {
    
    override fun observeDarkMode(): Flow<DarkMode> = appPreferences.observeDarkMode()
    override suspend fun setDarkMode(mode: DarkMode) = appPreferences.setDarkMode(mode)
    
    override fun observeDynamicColorEnabled(): Flow<Boolean> = appPreferences.observeDynamicColorEnabled()
    override suspend fun setDynamicColorEnabled(enabled: Boolean) = appPreferences.setDynamicColorEnabled(enabled)
    
    override fun observeFontScale(): Flow<Int> = appPreferences.observeFontScale()
    override suspend fun setFontScale(scale: Int) = appPreferences.setFontScale(scale)
    
    override fun observeAppLanguage(): Flow<AppLanguage> = appPreferences.observeAppLanguage()
    override suspend fun setAppLanguage(language: AppLanguage) = appPreferences.setAppLanguage(language)
    
    override fun isFirstRun(): Flow<Boolean> = appPreferences.isFirstRun()
    override suspend fun setFirstRun(completed: Boolean) = appPreferences.setFirstRun(completed)
    
    override fun isOnboardingCompleted(): Flow<Boolean> = appPreferences.isOnboardingCompleted()
    override suspend fun setOnboardingCompleted(completed: Boolean) = appPreferences.setOnboardingCompleted(completed)
    
    override fun observeDefaultVideoSortType(): Flow<SortType> = appPreferences.observeDefaultVideoSortType()
    override suspend fun setDefaultVideoSortType(sortType: SortType) = appPreferences.setDefaultVideoSortType(sortType)
    
    override fun observeDefaultSortOrder(): Flow<SortOrder> = appPreferences.observeDefaultSortOrder()
    override suspend fun setDefaultSortOrder(order: SortOrder) = appPreferences.setDefaultSortOrder(order)
    
    override fun observeDefaultViewType(): Flow<ViewType> = appPreferences.observeDefaultViewType()
    override suspend fun setDefaultViewType(viewType: ViewType) = appPreferences.setDefaultViewType(viewType)
    
    override fun isStoragePermissionGranted(): Flow<Boolean> = appPreferences.isStoragePermissionGranted()
    override suspend fun setStoragePermissionGranted(granted: Boolean) = appPreferences.setStoragePermissionGranted(granted)
    
    override fun observeStoragePaths(): Flow<List<String>> = appPreferences.observeStoragePaths()
    override suspend fun addStoragePath(path: String) = appPreferences.addStoragePath(path)
    override suspend fun removeStoragePath(path: String) = appPreferences.removeStoragePath(path)
    
    override fun observePlaybackSpeed(): Flow<Float> = playerPreferences.observePlaybackSpeed()
    override suspend fun setPlaybackSpeed(speed: Float) = playerPreferences.setPlaybackSpeed(speed)
    
    override fun observeDefaultQuality(): Flow<VideoQuality> = playerPreferences.observeDefaultQuality()
    override suspend fun setDefaultQuality(quality: VideoQuality) = playerPreferences.setDefaultQuality(quality)
    
    override fun observeAutoPlay(): Flow<Boolean> = playerPreferences.observeAutoPlay()
    override suspend fun setAutoPlay(enabled: Boolean) = playerPreferences.setAutoPlay(enabled)
    
    override fun observeRepeatMode(): Flow<RepeatMode> = playerPreferences.observeRepeatMode()
    override suspend fun setRepeatMode(mode: RepeatMode) = playerPreferences.setRepeatMode(mode)
    
    override fun observeShuffleMode(): Flow<Boolean> = playerPreferences.observeShuffleMode()
    override suspend fun setShuffleMode(enabled: Boolean) = playerPreferences.setShuffleMode(enabled)
    
    override fun observeVolume(): Flow<Float> = playerPreferences.observeVolume()
    override suspend fun setVolume(volume: Float) = playerPreferences.setVolume(volume)
    
    override fun observeMute(): Flow<Boolean> = playerPreferences.observeMute()
    override suspend fun setMute(muted: Boolean) = playerPreferences.setMute(muted)
    
    override fun observeEqualizerEnabled(): Flow<Boolean> = playerPreferences.observeEqualizerEnabled()
    override suspend fun setEqualizerEnabled(enabled: Boolean) = playerPreferences.setEqualizerEnabled(enabled)
    
    override fun observeEqualizerBands(): Flow<List<Int>> = playerPreferences.observeEqualizerBands()
    override suspend fun setEqualizerBands(bands: List<Int>) = playerPreferences.setEqualizerBands(bands)
    
    override fun observeSubtitlesEnabled(): Flow<Boolean> = playerPreferences.observeSubtitlesEnabled()
    override suspend fun setSubtitlesEnabled(enabled: Boolean) = playerPreferences.setSubtitlesEnabled(enabled)
    
    override fun observeSubtitleLanguage(): Flow<String> = playerPreferences.observeSubtitleLanguage()
    override suspend fun setSubtitleLanguage(language: String) = playerPreferences.setSubtitleLanguage(language)
    
    override fun observeSubtitleSize(): Flow<Int> = playerPreferences.observeSubtitleSize()
    override suspend fun setSubtitleSize(size: Int) = playerPreferences.setSubtitleSize(size)
    
    override fun observeSubtitleFont(): Flow<String> = playerPreferences.observeSubtitleFont()
    override suspend fun setSubtitleFont(font: String) = playerPreferences.setSubtitleFont(font)
    
    override fun observeSeekGestureEnabled(): Flow<Boolean> = playerPreferences.observeSeekGestureEnabled()
    override suspend fun setSeekGestureEnabled(enabled: Boolean) = playerPreferences.setSeekGestureEnabled(enabled)
    
    override fun observeVolumeGestureEnabled(): Flow<Boolean> = playerPreferences.observeVolumeGestureEnabled()
    override suspend fun setVolumeGestureEnabled(enabled: Boolean) = playerPreferences.setVolumeGestureEnabled(enabled)
    
    override fun observeBrightnessGestureEnabled(): Flow<Boolean> = playerPreferences.observeBrightnessGestureEnabled()
    override suspend fun setBrightnessGestureEnabled(enabled: Boolean) = playerPreferences.setBrightnessGestureEnabled(enabled)
    
    override fun observePipEnabled(): Flow<Boolean> = playerPreferences.observePipEnabled()
    override suspend fun setPipEnabled(enabled: Boolean) = playerPreferences.setPipEnabled(enabled)
    
    override fun observeBackgroundPlayEnabled(): Flow<Boolean> = playerPreferences.observeBackgroundPlayEnabled()
    override suspend fun setBackgroundPlayEnabled(enabled: Boolean) = playerPreferences.setBackgroundPlayEnabled(enabled)
    
    override suspend fun saveResumePosition(videoId: String, positionMs: Long) = playerPreferences.saveResumePosition(videoId, positionMs)
    override suspend fun getResumePosition(videoId: String): Long? = playerPreferences.getResumePosition(videoId)
    override suspend fun clearResumePosition(videoId: String) = playerPreferences.clearResumePosition(videoId)
    override fun observeResumePositions(): Flow<Map<String, Long>> = playerPreferences.observeResumePositions()
    
    override suspend fun addToHistory(item: PlaybackHistoryItem) = playerPreferences.addToHistory(item)
    override suspend fun clearHistory() = playerPreferences.clearHistory()
    override fun observeHistory(): Flow<List<PlaybackHistoryItem>> = playerPreferences.observeHistory()
    
    override suspend fun clearAllPreferences() {
        appPreferences.clear()
        playerPreferences.clear()
    }
}
