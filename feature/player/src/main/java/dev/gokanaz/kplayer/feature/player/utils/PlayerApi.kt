package dev.gokanaz.kplayer.feature.player.utils

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import dev.gokanaz.kplayer.core.model.Video
import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.model.player.VideoContentScale
import dev.gokanaz.kplayer.feature.player.model.Subtitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class PlayerResult<out T> {
    data class Success<T>(val data: T) : PlayerResult<T>()
    data class Error(val message: String, val cause: Throwable? = null) : PlayerResult<Nothing>()
    object Loading : PlayerResult<Nothing>()
}

interface PlayerApi {
    
    fun play()
    fun pause()
    fun togglePlay()
    fun seekTo(positionMs: Long)
    fun seekForward(deltaMs: Long)
    fun seekBackward(deltaMs: Long)
    fun skipNext()
    fun skipPrevious()
    fun stop()
    
    fun selectVideoTrack(index: Int)
    fun selectAudioTrack(index: Int)
    fun selectSubtitleTrack(index: Int)
    fun disableSubtitle()
    
    fun setPlaybackSpeed(speed: Float)
    fun setVolume(volume: Float, isMuted: Boolean = false)
    fun setRepeatMode(mode: LoopMode)
    fun setShuffleMode(enabled: Boolean)
    fun setVideoScale(scale: VideoContentScale)
    
    fun playMedia(uri: Uri, title: String? = null)
    fun playVideo(video: Video)
    fun playPlaylist(videos: List<Video>, startIndex: Int = 0)
    fun addSubtitle(subtitle: Subtitle)
    fun removeSubtitle(subtitleId: String)
    
    fun observePlaybackState(): Flow<Int>
    fun observeCurrentPosition(): Flow<Long>
    fun observeBufferingState(): Flow<Boolean>
    fun observePlayWhenReady(): Flow<Boolean>
    fun observeVolume(): Flow<Float>
    fun observePlaybackSpeed(): Flow<Float>
    fun observeRepeatMode(): Flow<LoopMode>
    fun observeShuffleMode(): Flow<Boolean>
    
    suspend fun getVideoInfo(uri: Uri): PlayerResult<Video>
    fun getAvailableVideoTracks(): List<Format>
    fun getAvailableAudioTracks(): List<Format>
    fun getAvailableSubtitleTracks(): List<Format>
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun getBufferedPosition(): Long
    
    fun addEventListener(listener: PlayerEventListener)
    fun removeEventListener(listener: PlayerEventListener)
    fun addErrorListener(listener: PlayerErrorListener)
    fun removeErrorListener(listener: PlayerErrorListener)
}

interface PlayerEventListener {
    fun onPlaybackStateChanged(state: Int)
    fun onBufferingStarted()
    fun onBufferingEnded()
    fun onMediaItemTransition(mediaItem: Video?)
    fun onPlaybackEnded()
    fun onSeekCompleted()
    fun onPlaybackSpeedChanged(speed: Float)
    fun onVolumeChanged(volume: Float, isMuted: Boolean)
    fun onRepeatModeChanged(mode: LoopMode)
    fun onShuffleModeChanged(enabled: Boolean)
}

interface PlayerErrorListener {
    fun onPlayerError(error: Throwable, isFatal: Boolean)
    fun onPlaybackError(video: Video, error: Throwable)
    fun onNetworkError(video: Video, error: Throwable)
    fun onDecoderError(video: Video, error: Throwable)
    fun onSubtitleError(subtitleId: String, error: Throwable)
}

class DefaultPlayerApi(
    private val player: Player,
    private val coroutineScope: CoroutineScope
) : PlayerApi {
    
    private val _playbackState = MutableStateFlow(Player.STATE_IDLE)
    private val _currentPosition = MutableStateFlow(0L)
    private val _isBuffering = MutableStateFlow(false)
    private val _playWhenReady = MutableStateFlow(false)
    private val _volume = MutableStateFlow(1.0f)
    private val _playbackSpeed = MutableStateFlow(1.0f)
    private val _repeatMode = MutableStateFlow(LoopMode.NONE)
    private val _shuffleMode = MutableStateFlow(false)
    
    private val eventListeners = mutableSetOf<PlayerEventListener>()
    private val errorListeners = mutableSetOf<PlayerErrorListener>()
    
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            _playbackState.value = playbackState
            eventListeners.forEach { it.onPlaybackStateChanged(playbackState) }
            
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    _isBuffering.value = true
                    eventListeners.forEach { it.onBufferingStarted() }
                }
                Player.STATE_READY -> {
                    _isBuffering.value = false
                    eventListeners.forEach { it.onBufferingEnded() }
                }
                Player.STATE_ENDED -> {
                    eventListeners.forEach { it.onPlaybackEnded() }
                }
            }
        }
        
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playWhenReady.value = isPlaying
        }
        
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            _playbackSpeed.value = playbackParameters.speed
            eventListeners.forEach { it.onPlaybackSpeedChanged(playbackParameters.speed) }
        }
        
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val video = mediaItem?.toVideo()
            eventListeners.forEach { it.onMediaItemTransition(video) }
        }
        
        override fun onRepeatModeChanged(repeatMode: Int) {
            val mode = when (repeatMode) {
                Player.REPEAT_MODE_OFF -> LoopMode.NONE
                Player.REPEAT_MODE_ONE -> LoopMode.ONE
                else -> LoopMode.ALL
            }
            _repeatMode.value = mode
            eventListeners.forEach { it.onRepeatModeChanged(mode) }
        }
        
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _shuffleMode.value = shuffleModeEnabled
            eventListeners.forEach { it.onShuffleModeChanged(shuffleModeEnabled) }
        }
        
        override fun onPlayerError(error: PlaybackException) {
            errorListeners.forEach { it.onPlayerError(error, true) }
        }
    }
    
    init {
        player.addListener(playerListener)
        coroutineScope.launch {
            launchPlayerStateObserver()
        }
    }
    
    private suspend fun launchPlayerStateObserver() {
        while (coroutineScope.isActive) {
            if (player.playbackState == Player.STATE_READY) {
                _currentPosition.value = player.currentPosition
            }
            delay(100)
        }
    }
    
    override fun play() {
        player.play()
    }
    
    override fun pause() {
        player.pause()
    }
    
    override fun togglePlay() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }
    
    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs.coerceAtLeast(0))
        eventListeners.forEach { it.onSeekCompleted() }
    }
    
    override fun seekForward(deltaMs: Long) {
        player.seekTo(player.currentPosition + deltaMs)
        eventListeners.forEach { it.onSeekCompleted() }
    }
    
    override fun seekBackward(deltaMs: Long) {
        player.seekTo(player.currentPosition - deltaMs)
        eventListeners.forEach { it.onSeekCompleted() }
    }
    
    override fun skipNext() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
        }
    }
    
    override fun skipPrevious() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        }
    }
    
    override fun stop() {
        player.stop()
    }
    
    override fun selectVideoTrack(index: Int) {
    }
    
    override fun selectAudioTrack(index: Int) {
    }
    
    override fun selectSubtitleTrack(index: Int) {
    }
    
    override fun disableSubtitle() {
    }
    
    override fun setPlaybackSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }
    
    override fun setVolume(volume: Float, isMuted: Boolean) {
        player.volume = if (isMuted) 0f else volume
        _volume.value = volume
        eventListeners.forEach { it.onVolumeChanged(volume, isMuted) }
    }
    
    override fun setRepeatMode(mode: LoopMode) {
        player.repeatMode = when (mode) {
            LoopMode.NONE -> Player.REPEAT_MODE_OFF
            LoopMode.ONE -> Player.REPEAT_MODE_ONE
            LoopMode.ALL -> Player.REPEAT_MODE_ALL
        }
    }
    
    override fun setShuffleMode(enabled: Boolean) {
        player.shuffleModeEnabled = enabled
    }
    
    override fun setVideoScale(scale: VideoContentScale) {
    }
    
    override fun playMedia(uri: Uri, title: String?) {
        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title ?: uri.lastPathSegment ?: "Unknown")
                    .build()
            )
            .build()
        
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    
    override fun playVideo(video: Video) {
        playMedia(Uri.parse(video.uri), video.title)
    }
    
    override fun playPlaylist(videos: List<Video>, startIndex: Int) {
        val mediaItems = videos.map { video ->
            MediaItem.Builder()
                .setUri(video.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(video.title)
                        .setExtras(Bundle().apply {
                            putLong("duration", video.duration)
                        })
                        .build()
                )
                .build()
        }
        
        player.setMediaItems(mediaItems, startIndex, 0)
        player.prepare()
        player.play()
    }
    
    override fun addSubtitle(subtitle: Subtitle) {
    }
    
    override fun removeSubtitle(subtitleId: String) {
    }
    
    override fun observePlaybackState(): Flow<Int> = _playbackState.asStateFlow()
    override fun observeCurrentPosition(): Flow<Long> = _currentPosition.asStateFlow()
    override fun observeBufferingState(): Flow<Boolean> = _isBuffering.asStateFlow()
    override fun observePlayWhenReady(): Flow<Boolean> = _playWhenReady.asStateFlow()
    override fun observeVolume(): Flow<Float> = _volume.asStateFlow()
    override fun observePlaybackSpeed(): Flow<Float> = _playbackSpeed.asStateFlow()
    override fun observeRepeatMode(): Flow<LoopMode> = _repeatMode.asStateFlow()
    override fun observeShuffleMode(): Flow<Boolean> = _shuffleMode.asStateFlow()
    
    override suspend fun getVideoInfo(uri: Uri): PlayerResult<Video> {
        return try {
            PlayerResult.Success(
                Video(
                    id = uri.toString().hashCode().toString(),
                    title = uri.lastPathSegment ?: "Unknown",
                    fileName = uri.lastPathSegment ?: "Unknown",
                    filePath = uri.path ?: "",
                    uri = uri.toString()
                )
            )
        } catch (e: Exception) {
            PlayerResult.Error("Failed to get video info", e)
        }
    }
    
    override fun getAvailableVideoTracks(): List<Format> = emptyList()
    override fun getAvailableAudioTracks(): List<Format> = emptyList()
    override fun getAvailableSubtitleTracks(): List<Format> = emptyList()
    override fun getCurrentPosition(): Long = player.currentPosition
    override fun getDuration(): Long = player.duration
    override fun getBufferedPosition(): Long = player.bufferedPosition
    
    override fun addEventListener(listener: PlayerEventListener) {
        eventListeners.add(listener)
    }
    
    override fun removeEventListener(listener: PlayerEventListener) {
        eventListeners.remove(listener)
    }
    
    override fun addErrorListener(listener: PlayerErrorListener) {
        errorListeners.add(listener)
    }
    
    override fun removeErrorListener(listener: PlayerErrorListener) {
        errorListeners.remove(listener)
    }
}

fun MediaItem.toVideo(): Video {
    return Video(
        id = mediaId,
        title = mediaMetadata.title?.toString() ?: "Unknown",
        fileName = mediaMetadata.title?.toString() ?: "Unknown",
        filePath = localConfiguration?.uri?.path ?: "",
        uri = localConfiguration?.uri?.toString() ?: ""
    )
}
