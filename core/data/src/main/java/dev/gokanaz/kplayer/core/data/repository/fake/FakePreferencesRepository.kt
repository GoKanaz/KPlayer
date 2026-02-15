package dev.gokanaz.kplayer.core.data.repository.fake

import dev.gokanaz.kplayer.core.data.repository.PreferencesRepository
import dev.gokanaz.kplayer.core.datastore.datasource.DarkMode
import dev.gokanaz.kplayer.core.datastore.datasource.AppLanguage
import dev.gokanaz.kplayer.core.datastore.datasource.ViewType
import dev.gokanaz.kplayer.core.model.media.VideoQuality
import dev.gokanaz.kplayer.core.datastore.datasource.RepeatMode
import dev.gokanaz.kplayer.core.datastore.datasource.PlaybackHistoryItem
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakePreferencesRepository @Inject constructor() : PreferencesRepository {
    
    private val darkModeFlow = MutableStateFlow(DarkMode.SYSTEM)
    private val dynamicColorFlow = MutableStateFlow(true)
    private val fontScaleFlow = MutableStateFlow(100)
    private val appLanguageFlow = MutableStateFlow(AppLanguage.AUTO)
    private val firstRunFlow = MutableStateFlow(true)
    private val onboardingCompletedFlow = MutableStateFlow(false)
    private val defaultSortTypeFlow = MutableStateFlow<SortType>(SortType.DATE)
    private val defaultSortOrderFlow = MutableStateFlow<SortOrder>(SortOrder.DESCENDING)
    private val defaultViewTypeFlow = MutableStateFlow(ViewType.GRID)
    private val storagePermissionFlow = MutableStateFlow(false)
    private val storagePathsFlow = MutableStateFlow<List<String>>(emptyList())
    
    private val playbackSpeedFlow = MutableStateFlow(1.0f)
    private val defaultQualityFlow = MutableStateFlow(VideoQuality.AUTO)
    private val autoPlayFlow = MutableStateFlow(true)
    private val repeatModeFlow = MutableStateFlow(RepeatMode.NONE)
    private val shuffleModeFlow = MutableStateFlow(false)
    private val volumeFlow = MutableStateFlow(0.8f)
    private val muteFlow = MutableStateFlow(false)
    private val equalizerEnabledFlow = MutableStateFlow(false)
    private val equalizerBandsFlow = MutableStateFlow(listOf(0, 0, 0, 0, 0))
    private val subtitlesEnabledFlow = MutableStateFlow(false)
    private val subtitleLanguageFlow = MutableStateFlow("en")
    private val subtitleSizeFlow = MutableStateFlow(16)
    private val subtitleFontFlow = MutableStateFlow("sans-serif")
    private val seekGestureEnabledFlow = MutableStateFlow(true)
    private val volumeGestureEnabledFlow = MutableStateFlow(true)
    private val brightnessGestureEnabledFlow = MutableStateFlow(true)
    private val pipEnabledFlow = MutableStateFlow(false)
    private val backgroundPlayEnabledFlow = MutableStateFlow(false)
    
    private val resumePositionsFlow = MutableStateFlow<Map<String, Long>>(emptyMap())
    private val historyFlow = MutableStateFlow<List<PlaybackHistoryItem>>(emptyList())
    
    override fun observeDarkMode(): Flow<DarkMode> = darkModeFlow
    override suspend fun setDarkMode(mode: DarkMode) { darkModeFlow.value = mode }
    
    override fun observeDynamicColorEnabled(): Flow<Boolean> = dynamicColorFlow
    override suspend fun setDynamicColorEnabled(enabled: Boolean) { dynamicColorFlow.value = enabled }
    
    override fun observeFontScale(): Flow<Int> = fontScaleFlow
    override suspend fun setFontScale(scale: Int) { fontScaleFlow.value = scale }
    
    override fun observeAppLanguage(): Flow<AppLanguage> = appLanguageFlow
    override suspend fun setAppLanguage(language: AppLanguage) { appLanguageFlow.value = language }
    
    override fun isFirstRun(): Flow<Boolean> = firstRunFlow
    override suspend fun setFirstRun(completed: Boolean) { firstRunFlow.value = completed }
    
    override fun isOnboardingCompleted(): Flow<Boolean> = onboardingCompletedFlow
    override suspend fun setOnboardingCompleted(completed: Boolean) { onboardingCompletedFlow.value = completed }
    
    override fun observeDefaultVideoSortType(): Flow<SortType> = defaultSortTypeFlow
    override suspend fun setDefaultVideoSortType(sortType: SortType) { defaultSortTypeFlow.value = sortType }
    
    override fun observeDefaultSortOrder(): Flow<SortOrder> = defaultSortOrderFlow
    override suspend fun setDefaultSortOrder(order: SortOrder) { defaultSortOrderFlow.value = order }
    
    override fun observeDefaultViewType(): Flow<ViewType> = defaultViewTypeFlow
    override suspend fun setDefaultViewType(viewType: ViewType) { defaultViewTypeFlow.value = viewType }
    
    override fun isStoragePermissionGranted(): Flow<Boolean> = storagePermissionFlow
    override suspend fun setStoragePermissionGranted(granted: Boolean) { storagePermissionFlow.value = granted }
    
    override fun observeStoragePaths(): Flow<List<String>> = storagePathsFlow
    override suspend fun addStoragePath(path: String) { 
        storagePathsFlow.update { it + path }
    }
    override suspend fun removeStoragePath(path: String) { 
        storagePathsFlow.update { it - path }
    }
    
    override fun observePlaybackSpeed(): Flow<Float> = playbackSpeedFlow
    override suspend fun setPlaybackSpeed(speed: Float) { playbackSpeedFlow.value = speed }
    
    override fun observeDefaultQuality(): Flow<VideoQuality> = defaultQualityFlow
    override suspend fun setDefaultQuality(quality: VideoQuality) { defaultQualityFlow.value = quality }
    
    override fun observeAutoPlay(): Flow<Boolean> = autoPlayFlow
    override suspend fun setAutoPlay(enabled: Boolean) { autoPlayFlow.value = enabled }
    
    override fun observeRepeatMode(): Flow<RepeatMode> = repeatModeFlow
    override suspend fun setRepeatMode(mode: RepeatMode) { repeatModeFlow.value = mode }
    
    override fun observeShuffleMode(): Flow<Boolean> = shuffleModeFlow
    override suspend fun setShuffleMode(enabled: Boolean) { shuffleModeFlow.value = enabled }
    
    override fun observeVolume(): Flow<Float> = volumeFlow
    override suspend fun setVolume(volume: Float) { volumeFlow.value = volume }
    
    override fun observeMute(): Flow<Boolean> = muteFlow
    override suspend fun setMute(muted: Boolean) { muteFlow.value = muted }
    
    override fun observeEqualizerEnabled(): Flow<Boolean> = equalizerEnabledFlow
    override suspend fun setEqualizerEnabled(enabled: Boolean) { equalizerEnabledFlow.value = enabled }
    
    override fun observeEqualizerBands(): Flow<List<Int>> = equalizerBandsFlow
    override suspend fun setEqualizerBands(bands: List<Int>) { equalizerBandsFlow.value = bands }
    
    override fun observeSubtitlesEnabled(): Flow<Boolean> = subtitlesEnabledFlow
    override suspend fun setSubtitlesEnabled(enabled: Boolean) { subtitlesEnabledFlow.value = enabled }
    
    override fun observeSubtitleLanguage(): Flow<String> = subtitleLanguageFlow
    override suspend fun setSubtitleLanguage(language: String) { subtitleLanguageFlow.value = language }
    
    override fun observeSubtitleSize(): Flow<Int> = subtitleSizeFlow
    override suspend fun setSubtitleSize(size: Int) { subtitleSizeFlow.value = size }
    
    override fun observeSubtitleFont(): Flow<String> = subtitleFontFlow
    override suspend fun setSubtitleFont(font: String) { subtitleFontFlow.value = font }
    
    override fun observeSeekGestureEnabled(): Flow<Boolean> = seekGestureEnabledFlow
    override suspend fun setSeekGestureEnabled(enabled: Boolean) { seekGestureEnabledFlow.value = enabled }
    
    override fun observeVolumeGestureEnabled(): Flow<Boolean> = volumeGestureEnabledFlow
    override suspend fun setVolumeGestureEnabled(enabled: Boolean) { volumeGestureEnabledFlow.value = enabled }
    
    override fun observeBrightnessGestureEnabled(): Flow<Boolean> = brightnessGestureEnabledFlow
    override suspend fun setBrightnessGestureEnabled(enabled: Boolean) { brightnessGestureEnabledFlow.value = enabled }
    
    override fun observePipEnabled(): Flow<Boolean> = pipEnabledFlow
    override suspend fun setPipEnabled(enabled: Boolean) { pipEnabledFlow.value = enabled }
    
    override fun observeBackgroundPlayEnabled(): Flow<Boolean> = backgroundPlayEnabledFlow
    override suspend fun setBackgroundPlayEnabled(enabled: Boolean) { backgroundPlayEnabledFlow.value = enabled }
    
    override suspend fun saveResumePosition(videoId: String, positionMs: Long) {
        resumePositionsFlow.update { it + (videoId to positionMs) }
    }
    
    override suspend fun getResumePosition(videoId: String): Long? {
        return resumePositionsFlow.value[videoId]
    }
    
    override suspend fun clearResumePosition(videoId: String) {
        resumePositionsFlow.update { it - videoId }
    }
    
    override fun observeResumePositions(): Flow<Map<String, Long>> = resumePositionsFlow
    
    override suspend fun addToHistory(item: PlaybackHistoryItem) {
        historyFlow.update { listOf(item) + it.take(99) }
    }
    
    override suspend fun clearHistory() {
        historyFlow.value = emptyList()
    }
    
    override fun observeHistory(): Flow<List<PlaybackHistoryItem>> = historyFlow
    
    override suspend fun clearAllPreferences() {
        darkModeFlow.value = DarkMode.SYSTEM
        dynamicColorFlow.value = true
        fontScaleFlow.value = 100
        appLanguageFlow.value = AppLanguage.AUTO
        firstRunFlow.value = true
        onboardingCompletedFlow.value = false
        defaultSortTypeFlow.value = SortType.DATE
        defaultSortOrderFlow.value = SortOrder.DESCENDING
        defaultViewTypeFlow.value = ViewType.GRID
        storagePermissionFlow.value = false
        storagePathsFlow.value = emptyList()
        
        playbackSpeedFlow.value = 1.0f
        defaultQualityFlow.value = VideoQuality.AUTO
        autoPlayFlow.value = true
        repeatModeFlow.value = RepeatMode.NONE
        shuffleModeFlow.value = false
        volumeFlow.value = 0.8f
        muteFlow.value = false
        equalizerEnabledFlow.value = false
        equalizerBandsFlow.value = listOf(0, 0, 0, 0, 0)
        subtitlesEnabledFlow.value = false
        subtitleLanguageFlow.value = "en"
        subtitleSizeFlow.value = 16
        subtitleFontFlow.value = "sans-serif"
        seekGestureEnabledFlow.value = true
        volumeGestureEnabledFlow.value = true
        brightnessGestureEnabledFlow.value = true
        pipEnabledFlow.value = false
        backgroundPlayEnabledFlow.value = false
        
        resumePositionsFlow.value = emptyMap()
        historyFlow.value = emptyList()
    }
}
