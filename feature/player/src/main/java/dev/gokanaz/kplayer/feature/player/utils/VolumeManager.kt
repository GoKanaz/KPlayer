package dev.gokanaz.kplayer.feature.player.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VolumeManager(private val context: Context) {
    
    private val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    
    private val _volume = MutableStateFlow(getCurrentVolume())
    val volume: StateFlow<Int> = _volume.asStateFlow()
    
    private val _isMuted = MutableStateFlow(isMuted())
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()
    
    private val _isHeadsetPlugged = MutableStateFlow(isHeadsetPlugged())
    val isHeadsetPlugged: StateFlow<Boolean> = _isHeadsetPlugged.asStateFlow()
    
    private val volumeChangeListener = VolumeChangeListener()
    private val headsetReceiver = HeadsetReceiver()
    
    init {
        registerVolumeChangeListener()
        registerHeadsetReceiver()
    }
    
    private fun registerVolumeChangeListener() {
        volumeChangeListener.register()
    }
    
    private fun registerHeadsetReceiver() {
        val filter = IntentFilter().apply {
            addAction(AudioManager.ACTION_HEADSET_PLUG)
            addAction(Intent.ACTION_HEADSET_PLUG)
        }
        context.registerReceiver(headsetReceiver, filter)
    }
    
    fun getCurrentVolume(): Int {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }
    
    fun getMaxVolume(): Int {
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }
    
    fun getMinVolume(): Int {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC)
        } else {
            0
        }
    }
    
    fun setVolume(volume: Int, flags: Int = 0) {
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volume.coerceIn(getMinVolume(), getMaxVolume()),
            flags
        )
        _volume.value = volume
    }
    
    fun increaseVolume(step: Int = 1) {
        val newVolume = (getCurrentVolume() + step).coerceAtMost(getMaxVolume())
        setVolume(newVolume, AudioManager.FLAG_SHOW_UI)
    }
    
    fun decreaseVolume(step: Int = 1) {
        val newVolume = (getCurrentVolume() - step).coerceAtLeast(getMinVolume())
        setVolume(newVolume, AudioManager.FLAG_SHOW_UI)
    }
    
    fun mute() {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_MUTE,
            AudioManager.FLAG_SHOW_UI
        )
        _isMuted.value = true
    }
    
    fun unmute() {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_UNMUTE,
            AudioManager.FLAG_SHOW_UI
        )
        _isMuted.value = false
    }
    
    fun toggleMute() {
        if (isMuted()) {
            unmute()
        } else {
            mute()
        }
    }
    
    fun isMuted(): Boolean {
        return audioManager.isStreamMute(AudioManager.STREAM_MUSIC)
    }
    
    fun setPlayerVolume(volume: Float, isMuted: Boolean = false) {
        _volume.value = (volume * getMaxVolume()).toInt()
        _isMuted.value = isMuted
    }
    
    fun fadeVolume(targetVolume: Float, durationMs: Long, onComplete: (() -> Unit)? = null) {
        val handler = Handler(Looper.getMainLooper())
        val startVolume = _volume.value.toFloat() / getMaxVolume()
        val steps = 20
        val stepDuration = durationMs / steps
        
        for (i in 0..steps) {
            handler.postDelayed({
                val progress = i.toFloat() / steps
                val currentVolume = startVolume + (targetVolume - startVolume) * progress
                setPlayerVolume(currentVolume)
                
                if (i == steps) {
                    onComplete?.invoke()
                }
            }, stepDuration * i)
        }
    }
    
    fun isHeadsetPlugged(): Boolean {
        return audioManager.isWiredHeadsetOn || audioManager.isBluetoothA2dpOn
    }
    
    fun getAudioSessionId(): Int {
        return audioManager.generateAudioSessionId()
    }
    
    fun setVolumeNormalization(enabled: Boolean) {
        // Implement volume normalization using AudioEffect
    }
    
    fun saveVolumePreset(name: String, volume: Int) {
        // Save volume preset to DataStore
    }
    
    fun loadVolumePreset(name: String): Int? {
        // Load volume preset from DataStore
        return null
    }
    
    fun cleanup() {
        volumeChangeListener.unregister()
        context.unregisterReceiver(headsetReceiver)
    }
    
    private inner class VolumeChangeListener : AudioManager.OnAudioFocusChangeListener {
        
        fun register() {
            // Register for volume changes
        }
        
        fun unregister() {
            // Unregister volume changes
        }
        
        override fun onAudioFocusChange(focusChange: Int) {
            // Handle audio focus changes for ducking
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    setPlayerVolume(0.3f)
                }
                AudioManager.AUDIOFOCUS_GAIN -> {
                    setPlayerVolume(_volume.value.toFloat() / getMaxVolume())
                }
            }
        }
    }
    
    private inner class HeadsetReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AudioManager.ACTION_HEADSET_PLUG,
                Intent.ACTION_HEADSET_PLUG -> {
                    _isHeadsetPlugged.value = isHeadsetPlugged()
                }
            }
        }
    }
}

class VolumePreset(
    val id: String,
    val name: String,
    val volumeLevel: Int,
    val isNightMode: Boolean = false,
    val isDefault: Boolean = false
)
