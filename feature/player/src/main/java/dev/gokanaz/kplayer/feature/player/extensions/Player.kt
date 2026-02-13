package dev.gokanaz.kplayer.feature.player.extensions

import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionParameters
import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.model.preferences.PlayerPreferences

fun Player.togglePlay() {
    if (isPlaying) {
        pause()
    } else {
        play()
    }
}

fun Player.seekForward(deltaMs: Long = 10000) {
    val newPosition = currentPosition + deltaMs
    seekTo(newPosition.coerceAtMost(duration))
}

fun Player.seekBackward(deltaMs: Long = 10000) {
    val newPosition = currentPosition - deltaMs
    seekTo(newPosition.coerceAtLeast(0))
}

fun Player.seekToPercentage(percentage: Float) {
    val position = (percentage * duration).toLong()
    seekTo(position.coerceIn(0, duration))
}

fun Player.seekToNext() {
    if (hasNextMediaItem()) {
        seekToNextMediaItem()
    }
}

fun Player.seekToPrevious() {
    if (hasPreviousMediaItem()) {
        seekToPreviousMediaItem()
    }
}

fun Player.isPlaying(): Boolean {
    return playbackState == Player.STATE_READY && isPlaying
}

fun Player.isIdle(): Boolean {
    return playbackState == Player.STATE_IDLE
}

fun Player.isBuffering(): Boolean {
    return playbackState == Player.STATE_BUFFERING
}

fun Player.isEnded(): Boolean {
    return playbackState == Player.STATE_ENDED
}

fun Player.currentPositionPercentage(): Float {
    return if (duration > 0) {
        (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
    } else 0f
}

fun Player.bufferedPercentage(): Int {
    return bufferedPercentage
}

fun Player.availableCommandsList(): List<Int> {
    return buildList {
        if (availableCommands.contains(Player.COMMAND_PLAY_PAUSE)) add(Player.COMMAND_PLAY_PAUSE)
        if (availableCommands.contains(Player.COMMAND_SEEK_TO_NEXT)) add(Player.COMMAND_SEEK_TO_NEXT)
        if (availableCommands.contains(Player.COMMAND_SEEK_TO_PREVIOUS)) add(Player.COMMAND_SEEK_TO_PREVIOUS)
        if (availableCommands.contains(Player.COMMAND_SEEK_TO)) add(Player.COMMAND_SEEK_TO)
        if (availableCommands.contains(Player.COMMAND_SET_SPEED_AND_PITCH)) add(Player.COMMAND_SET_SPEED_AND_PITCH)
        if (availableCommands.contains(Player.COMMAND_SET_TRACK_SELECTION_PARAMETERS)) add(Player.COMMAND_SET_TRACK_SELECTION_PARAMETERS)
    }
}

fun Player.getAvailableVideoTracks(): List<Format> {
    return currentTracks?.groups
        ?.filter { group ->
            group.type == C.TRACK_TYPE_VIDEO
        }
        ?.flatMap { group ->
            (0 until group.length).map { index -> group.getTrackFormat(index) }
        } ?: emptyList()
}

fun Player.getAvailableAudioTracks(): List<Format> {
    return currentTracks?.groups
        ?.filter { group ->
            group.type == C.TRACK_TYPE_AUDIO
        }
        ?.flatMap { group ->
            (0 until group.length).map { index -> group.getTrackFormat(index) }
        } ?: emptyList()
}

fun Player.getAvailableSubtitleTracks(): List<Format> {
    return currentTracks?.groups
        ?.filter { group ->
            group.type == C.TRACK_TYPE_TEXT
        }
        ?.flatMap { group ->
            (0 until group.length).map { index -> group.getTrackFormat(index) }
        } ?: emptyList()
}

fun Player.selectVideoTrack(index: Int) {
    val parameters = trackSelectionParameters
        .buildUpon()
        .setPreferredVideoMimeType(null)
        .build()
    trackSelectionParameters = parameters
}

fun Player.selectAudioTrack(index: Int) {
    val tracks = getAvailableAudioTracks()
    if (index < tracks.size) {
        val format = tracks[index]
        val parameters = trackSelectionParameters
            .buildUpon()
            .setPreferredAudioLanguage(format.language)
            .build()
        trackSelectionParameters = parameters
    }
}

fun Player.selectSubtitleTrack(index: Int) {
    val tracks = getAvailableSubtitleTracks()
    if (index < tracks.size) {
        val format = tracks[index]
        val parameters = trackSelectionParameters
            .buildUpon()
            .setPreferredTextLanguage(format.language)
            .setPreferredTextRoleFlags(C.ROLE_FLAG_SUBTITLE)
            .build()
        trackSelectionParameters = parameters
    }
}

fun Player.disableSubtitle() {
    val parameters = trackSelectionParameters
        .buildUpon()
        .setPreferredTextLanguage(null)
        .build()
    trackSelectionParameters = parameters
}

fun Player.setPlaybackSpeed(speed: Float) {
    val parameters = playbackParameters
        .buildUpon()
        .setSpeed(speed)
        .build()
    playbackParameters = parameters
}

fun Player.setVolume(volume: Float, isMuted: Boolean) {
    this.volume = if (isMuted) 0f else volume.coerceIn(0f, 1f)
}

fun Player.applyPreferences(prefs: PlayerPreferences) {
    setPlaybackSpeed(prefs.playbackSpeed)
    setVolume(prefs.volume / 100f, prefs.isMuted)
    
    repeatMode = when (prefs.repeatMode) {
        LoopMode.NONE -> Player.REPEAT_MODE_OFF
        LoopMode.ONE -> Player.REPEAT_MODE_ONE
        LoopMode.ALL -> Player.REPEAT_MODE_ALL
    }
}

fun Player.releaseSafely() {
    if (playbackState != Player.STATE_IDLE) {
        stop()
        clearMediaItems()
        release()
    }
}
