package dev.gokanaz.kplayer.core.datastore.datasource


import dev.gokanaz.kplayer.core.model.media.VideoQuality
import androidx.datastore.core.DataStore
import dev.gokanaz.kplayer.core.datastore.proto.PlaybackHistoryItemProto
import dev.gokanaz.kplayer.core.datastore.proto.PlayerSettingsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


enum class RepeatMode {
    NONE, ONE, ALL
}

data class PlaybackHistoryItem(
    val videoId: String,
    val videoTitle: String,
    val thumbnailPath: String?,
    val watchedAt: Long,
    val positionMs: Long,
    val durationMs: Long
)

@Singleton
class PlayerPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<PlayerSettingsProto>
) : PreferencesDataSource {
    
    override suspend fun <T> put(key: PreferencesKey<T>, value: T) {
    }

    override suspend fun <T> get(key: PreferencesKey<T>, defaultValue: T): T {
        return defaultValue
    }

    override suspend fun <T> remove(key: PreferencesKey<T>) {
    }

    override suspend fun clear() {
        dataStore.updateData { it.toBuilder().clear().build() }
    }

    override fun <T> observe(key: PreferencesKey<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { defaultValue }
    }

    suspend fun setPlaybackSpeed(speed: Float) {
        dataStore.updateData { settings ->
            settings.toBuilder().setPlaybackSpeed(speed).build()
        }
    }

    fun observePlaybackSpeed(): Flow<Float> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.playbackSpeed.let { if (it == 0f) 1.0f else it }
            }
    }

    suspend fun setDefaultQuality(quality: VideoQuality) {
        dataStore.updateData { settings ->
            settings.toBuilder().setDefaultQuality(quality.value).build()
        }
    }

    fun observeDefaultQuality(): Flow<VideoQuality> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                VideoQuality.fromValue(settings.defaultQuality)
            }
    }

    suspend fun setAutoPlay(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setAutoPlay(enabled).build()
        }
    }

    fun observeAutoPlay(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.autoPlay
            }
    }

    suspend fun setRepeatMode(mode: RepeatMode) {
        dataStore.updateData { settings ->
            settings.toBuilder().setRepeatMode(mode.ordinal).build()
        }
    }

    fun observeRepeatMode(): Flow<RepeatMode> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                when (settings.repeatMode) {
                    1 -> RepeatMode.ONE
                    2 -> RepeatMode.ALL
                    else -> RepeatMode.NONE
                }
            }
    }

    suspend fun setShuffleMode(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setShuffleMode(enabled).build()
        }
    }

    fun observeShuffleMode(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.shuffleMode
            }
    }

    suspend fun setVolume(volume: Float) {
        dataStore.updateData { settings ->
            settings.toBuilder().setVolume(volume.coerceIn(0f, 1f)).build()
        }
    }

    fun observeVolume(): Flow<Float> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.volume.let { if (it == 0f) 0.8f else it }
            }
    }

    suspend fun setMute(muted: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setMute(muted).build()
        }
    }

    fun observeMute(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.mute
            }
    }

    suspend fun setEqualizerEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setEqualizerEnabled(enabled).build()
        }
    }

    fun observeEqualizerEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.equalizerEnabled
            }
    }

    suspend fun setEqualizerBands(bands: List<Int>) {
        dataStore.updateData { settings ->
            settings.toBuilder().clearEqualizerBands().addAllEqualizerBands(bands).build()
        }
    }

    fun observeEqualizerBands(): Flow<List<Int>> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.equalizerBandsList
            }
    }

    suspend fun setSubtitlesEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setSubtitlesEnabled(enabled).build()
        }
    }

    fun observeSubtitlesEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.subtitlesEnabled
            }
    }

    suspend fun setSubtitleLanguage(language: String) {
        dataStore.updateData { settings ->
            settings.toBuilder().setSubtitleLanguage(language).build()
        }
    }

    fun observeSubtitleLanguage(): Flow<String> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.subtitleLanguage.ifEmpty { "en" }
            }
    }

    suspend fun setSubtitleSize(size: Int) {
        dataStore.updateData { settings ->
            settings.toBuilder().setSubtitleSize(size.coerceIn(12, 32)).build()
        }
    }

    fun observeSubtitleSize(): Flow<Int> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.subtitleSize.let { if (it == 0) 16 else it }
            }
    }

    suspend fun setSubtitleFont(font: String) {
        dataStore.updateData { settings ->
            settings.toBuilder().setSubtitleFont(font).build()
        }
    }

    fun observeSubtitleFont(): Flow<String> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.subtitleFont.ifEmpty { "sans-serif" }
            }
    }

    suspend fun setSeekGestureEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setSeekGestureEnabled(enabled).build()
        }
    }

    fun observeSeekGestureEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.seekGestureEnabled
            }
    }

    suspend fun setVolumeGestureEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setVolumeGestureEnabled(enabled).build()
        }
    }

    fun observeVolumeGestureEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.volumeGestureEnabled
            }
    }

    suspend fun setBrightnessGestureEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setBrightnessGestureEnabled(enabled).build()
        }
    }

    fun observeBrightnessGestureEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.brightnessGestureEnabled
            }
    }

    suspend fun setPipEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setPipEnabled(enabled).build()
        }
    }

    fun observePipEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.pipEnabled
            }
    }

    suspend fun setBackgroundPlayEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setBackgroundPlayEnabled(enabled).build()
        }
    }

    fun observeBackgroundPlayEnabled(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.backgroundPlayEnabled
            }
    }

    suspend fun saveResumePosition(videoId: String, positionMs: Long) {
        dataStore.updateData { settings ->
            settings.toBuilder().putResumePositions(videoId, positionMs).build()
        }
    }

    suspend fun getResumePosition(videoId: String): Long? {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.resumePositionsMap[videoId]
            }.first()
    }

    suspend fun clearResumePosition(videoId: String) {
        dataStore.updateData { settings ->
            settings.toBuilder().removeResumePositions(videoId).build()
        }
    }

    fun observeResumePositions(): Flow<Map<String, Long>> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.resumePositionsMap
            }
    }

    suspend fun addToHistory(item: PlaybackHistoryItem) {
        dataStore.updateData { settings ->
            val protoItem = PlaybackHistoryItemProto.newBuilder()
                .setVideoId(item.videoId)
                .setVideoTitle(item.videoTitle)
                .setWatchedAt(item.watchedAt)
                .setPositionMs(item.positionMs)
                .setDurationMs(item.durationMs)
                .apply {
                    item.thumbnailPath?.let { setThumbnailPath(it) }
                }
                .build()
            
            val currentHistory = settings.historyList
            val newHistory = listOf(protoItem) + currentHistory
            
            settings.toBuilder()
                .clearHistory()
                .addAllHistory(newHistory.take(100))
                .build()
        }
    }

    suspend fun clearHistory() {
        dataStore.updateData { settings ->
            settings.toBuilder().clearHistory().build()
        }
    }

    fun observeHistory(): Flow<List<PlaybackHistoryItem>> {
        return dataStore.data
            .catch { emit(PlayerSettingsProto.getDefaultInstance()) }
            .map { settings ->
                settings.historyList.map { proto ->
                    PlaybackHistoryItem(
                        videoId = proto.videoId,
                        videoTitle = proto.videoTitle,
                        thumbnailPath = if (proto.hasThumbnailPath()) proto.thumbnailPath else null,
                        watchedAt = proto.watchedAt,
                        positionMs = proto.positionMs,
                        durationMs = proto.durationMs
                    )
                }
            }
    }
}
