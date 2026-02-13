package dev.gokanaz.kplayer.feature.player.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import dev.gokanaz.kplayer.feature.player.R

class PlayerService : Service(), AudioManager.OnAudioFocusChangeListener {
    
    private val binder = PlayerBinder()
    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession
    private lateinit var mediaSessionCompat: MediaSessionCompat
    private lateinit var audioManager: AudioManager
    private var audioFocusRequest: AudioFocusRequest? = null
    private var wakeLock: PowerManager.WakeLock? = null
    private var isAudioFocused = false
    
    companion object {
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "kplayer_playback_channel"
        const val CHANNEL_NAME = "Playback"
    }
    
    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
        fun getPlayer(): ExoPlayer = player
        fun getMediaSession(): MediaSession = mediaSession
    }
    
    override fun onCreate() {
        super.onCreate()
        initializeAudioManager()
        initializeWakeLock()
        initializePlayer()
        initializeMediaSession()
        createNotificationChannel()
    }
    
    private fun initializeAudioManager() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    
    private fun initializeWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "kplayer::player_wakelock"
        ).apply {
            setReferenceCounted(false)
        }
    }
    
    private fun initializePlayer() {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(httpDataSourceFactory)
            )
            .build()
            .apply {
                addListener(playerListener)
                setHandleAudioBecomingNoisy(true)
            }
    }
    
    private fun initializeMediaSession() {
        mediaSessionCompat = MediaSessionCompat(this, "PlayerService").apply {
            setCallback(mediaSessionCallback)
            isActive = true
        }
        
        mediaSession = MediaSession.Builder(this, player)
            .setId("kplayer_media_session")
            .build()
        
        updatePlaybackState()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Playback controls"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlaybackState()
            updateNotification()
            
            when (playbackState) {
                Player.STATE_READY -> {
                    if (player.isPlaying) {
                        startForeground(NOTIFICATION_ID, createNotification())
                        wakeLock?.acquire(10 * 60 * 1000L)
                    }
                }
                Player.STATE_ENDED -> {
                    stopForeground(false)
                    wakeLock?.release()
                }
            }
        }
        
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updatePlaybackState()
            updateNotification()
            
            if (isPlaying) {
                requestAudioFocus()
            } else {
                abandonAudioFocus()
            }
        }
        
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateMetadata(mediaItem)
        }
    }
    
    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            player.play()
        }
        
        override fun onPause() {
            player.pause()
        }
        
        override fun onSkipToNext() {
            player.seekToNextMediaItem()
        }
        
        override fun onSkipToPrevious() {
            player.seekToPreviousMediaItem()
        }
        
        override fun onSeekTo(pos: Long) {
            player.seekTo(pos)
        }
        
        override fun onStop() {
            player.stop()
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }
    }
    
    private fun updatePlaybackState() {
        val state = when (player.playbackState) {
            Player.STATE_BUFFERING -> PlaybackStateCompat.STATE_BUFFERING
            Player.STATE_READY -> if (player.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
            Player.STATE_ENDED -> PlaybackStateCompat.STATE_STOPPED
            else -> PlaybackStateCompat.STATE_NONE
        }
        
        val playbackStateCompat = PlaybackStateCompat.Builder()
            .setState(state, player.currentPosition, player.playbackParameters.speed)
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                PlaybackStateCompat.ACTION_SEEK_TO or
                PlaybackStateCompat.ACTION_STOP
            )
            .build()
        
        mediaSessionCompat.setPlaybackState(playbackStateCompat)
    }
    
    private fun updateMetadata(mediaItem: MediaItem?) {
        mediaItem?.let { item ->
            val metadata = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, item.mediaMetadata.title?.toString() ?: "Unknown")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "KPlayer")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, item.mediaMetadata.extras?.getLong("duration", 0) ?: 0)
                .build()
            
            mediaSessionCompat.setMetadata(metadata)
        }
    }
    
    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createNotification(): Notification {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val playAction = NotificationCompat.Action(
            if (player.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
            if (player.isPlaying) "Pause" else "Play",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                this,
                if (player.isPlaying) PlaybackStateCompat.ACTION_PAUSE else PlaybackStateCompat.ACTION_PLAY
            )
        )
        
        val previousAction = NotificationCompat.Action(
            R.drawable.ic_skip_previous,
            "Previous",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                this,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )
        
        val nextAction = NotificationCompat.Action(
            R.drawable.ic_skip_next,
            "Next",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                this,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(mediaSessionCompat.controller.metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE) ?: "KPlayer")
            .setContentText("Playing video")
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_notification)
            .addAction(previousAction)
            .addAction(playAction)
            .addAction(nextAction)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionCompat.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            this,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )
            .setOngoing(player.isPlaying)
            .build()
    }
    
    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                player.volume = 1.0f
                if (!player.isPlaying) {
                    player.play()
                }
                isAudioFocused = true
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                player.pause()
                isAudioFocused = false
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                player.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                player.volume = 0.3f
            }
        }
    }
    
    private fun requestAudioFocus(): Boolean {
        if (isAudioFocused) return true
        
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                        .build()
                )
                .setOnAudioFocusChangeListener(this)
                .build()
            
            audioManager.requestAudioFocus(audioFocusRequest!!)
        } else {
            audioManager.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
        
        isAudioFocused = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        return isAudioFocused
    }
    
    private fun abandonAudioFocus() {
        if (!isAudioFocused) return
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let {
                audioManager.abandonAudioFocusRequest(it)
            }
        } else {
            audioManager.abandonAudioFocus(this)
        }
        
        isAudioFocused = false
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder = binder
    
    override fun onDestroy() {
        player.releaseSafely()
        mediaSession.release()
        mediaSessionCompat.release()
        wakeLock?.release()
        abandonAudioFocus()
        super.onDestroy()
    }
}
