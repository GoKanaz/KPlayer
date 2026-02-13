package dev.gokanaz.kplayer.feature.player.service

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import androidx.media3.common.Player
import androidx.media3.session.MediaSession

object CustomCommands {
    
    // Custom command constants
    const val SEEK_FORWARD = "SEEK_FORWARD"
    const val SEEK_BACKWARD = "SEEK_BACKWARD"
    const val CHANGE_SPEED = "CHANGE_SPEED"
    const val TOGGLE_REPEAT = "TOGGLE_REPEAT"
    const val TOGGLE_SHUFFLE = "TOGGLE_SHUFFLE"
    const val CHANGE_QUALITY = "CHANGE_QUALITY"
    const val SELECT_AUDIO_TRACK = "SELECT_AUDIO_TRACK"
    const val SELECT_SUBTITLE_TRACK = "SELECT_SUBTITLE_TRACK"
    const val TOGGLE_SUBTITLE = "TOGGLE_SUBTITLE"
    const val ENTER_PIP = "ENTER_PIP"
    const val LOCK_CONTROLS = "LOCK_CONTROLS"
    const val SET_PLAYBACK_SPEED = "SET_PLAYBACK_SPEED"
    const val SET_VIDEO_SCALE = "SET_VIDEO_SCALE"
    
    // Command extras keys
    const val EXTRA_SEEK_DELTA = "SEEK_DELTA"
    const val EXTRA_PLAYBACK_SPEED = "PLAYBACK_SPEED"
    const val EXTRA_REPEAT_MODE = "REPEAT_MODE"
    const val EXTRA_SHUFFLE_ENABLED = "SHUFFLE_ENABLED"
    const val EXTRA_QUALITY_INDEX = "QUALITY_INDEX"
    const val EXTRA_TRACK_INDEX = "TRACK_INDEX"
    const val EXTRA_SUBTITLE_ENABLED = "SUBTITLE_ENABLED"
    const val EXTRA_VIDEO_SCALE = "VIDEO_SCALE"
    
    // Media3 custom commands
    fun createSeekForwardCommand(deltaMs: Long = 10000): MediaSession.Command {
        val extras = Bundle().apply {
            putLong(EXTRA_SEEK_DELTA, deltaMs)
        }
        return MediaSession.Command(SEEK_FORWARD, extras)
    }
    
    fun createSeekBackwardCommand(deltaMs: Long = 10000): MediaSession.Command {
        val extras = Bundle().apply {
            putLong(EXTRA_SEEK_DELTA, deltaMs)
        }
        return MediaSession.Command(SEEK_BACKWARD, extras)
    }
    
    fun createChangeSpeedCommand(speed: Float): MediaSession.Command {
        val extras = Bundle().apply {
            putFloat(EXTRA_PLAYBACK_SPEED, speed)
        }
        return MediaSession.Command(CHANGE_SPEED, extras)
    }
    
    fun createToggleRepeatCommand(): MediaSession.Command {
        return MediaSession.Command(TOGGLE_REPEAT, Bundle())
    }
    
    fun createToggleShuffleCommand(): MediaSession.Command {
        return MediaSession.Command(TOGGLE_SHUFFLE, Bundle())
    }
    
    fun createSelectAudioTrackCommand(index: Int): MediaSession.Command {
        val extras = Bundle().apply {
            putInt(EXTRA_TRACK_INDEX, index)
        }
        return MediaSession.Command(SELECT_AUDIO_TRACK, extras)
    }
    
    fun createSelectSubtitleTrackCommand(index: Int, enabled: Boolean = true): MediaSession.Command {
        val extras = Bundle().apply {
            putInt(EXTRA_TRACK_INDEX, index)
            putBoolean(EXTRA_SUBTITLE_ENABLED, enabled)
        }
        return MediaSession.Command(SELECT_SUBTITLE_TRACK, extras)
    }
}

class CustomCommandCallback(
    private val onCustomCommand: (MediaSession.Command, Bundle) -> Boolean
) : MediaSession.Callback {
    
    override fun onCustomCommand(
        session: MediaSession,
        controller: MediaSession.ControllerInfo,
        customCommand: MediaSession.Command,
        args: Bundle
    ): MediaSession.CommandResult {
        return if (onCustomCommand(customCommand, args)) {
            MediaSession.CommandResult(LegacyBrowser)
        } else {
            MediaSession.CommandResult.rejected()
        }
    }
}

class CustomCommandHandler(private val player: Player) {
    
    fun handleCommand(command: MediaSession.Command, extras: Bundle): Boolean {
        return when (command.customAction) {
            CustomCommands.SEEK_FORWARD -> {
                val delta = extras.getLong(CustomCommands.EXTRA_SEEK_DELTA, 10000)
                player.seekForward(delta)
                true
            }
            CustomCommands.SEEK_BACKWARD -> {
                val delta = extras.getLong(CustomCommands.EXTRA_SEEK_DELTA, 10000)
                player.seekBackward(delta)
                true
            }
            CustomCommands.CHANGE_SPEED -> {
                val speed = extras.getFloat(CustomCommands.EXTRA_PLAYBACK_SPEED, 1.0f)
                player.setPlaybackSpeed(speed)
                true
            }
            CustomCommands.TOGGLE_REPEAT -> {
                val nextMode = when (player.repeatMode) {
                    Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                    Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                    else -> Player.REPEAT_MODE_OFF
                }
                player.repeatMode = nextMode
                true
            }
            CustomCommands.TOGGLE_SHUFFLE -> {
                player.shuffleModeEnabled = !player.shuffleModeEnabled
                true
            }
            else -> false
        }
    }
}
