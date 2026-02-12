package dev.gokanaz.kplayer.core.media.service

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.gokanaz.kplayer.core.media.model.MediaVideo
import dev.gokanaz.kplayer.core.media.services.LocalMediaService
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MediaService : MediaBrowserServiceCompat() {
    
    @Inject
    lateinit var localMediaService: LocalMediaService
    
    private lateinit var mediaSession: MediaSessionCompat
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    override fun onCreate() {
        super.onCreate()
        
        mediaSession = MediaSessionCompat(this, "MediaService").apply {
            setCallback(MediaSessionCallback())
            isActive = true
        }
        
        sessionToken = mediaSession.sessionToken
    }
    
    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot("media_root", null)
    }
    
    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.detach()
        
        when (parentId) {
            "media_root" -> {
                val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()
                
                val allVideos = localMediaService.allVideos.value
                mediaItems.addAll(allVideos.map { video ->
                    createMediaItem(video)
                })
                
                result.sendResult(mediaItems)
            }
            else -> {
                result.sendResult(mutableListOf())
            }
        }
    }
    
    private fun createMediaItem(video: MediaVideo): MediaBrowserCompat.MediaItem {
        val description = MediaDescriptionCompat.Builder()
            .setMediaId(video.id)
            .setTitle(video.title)
            .setSubtitle(video.artist)
            .setDescription(video.album)
            .setMediaUri(video.uri)
            .build()
        
        return MediaBrowserCompat.MediaItem(
            description,
            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        )
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        mediaSession.isActive = false
        mediaSession.release()
    }
    
    private inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()
        }
        
        override fun onPause() {
            super.onPause()
        }
        
        override fun onSkipToNext() {
            super.onSkipToNext()
        }
        
        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
        }
        
        override fun onStop() {
            super.onStop()
            stopSelf()
        }
        
        override fun onPlayFromMediaId(mediaId: String, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)
        }
    }
}
