package dev.gokanaz.kplayer.core.model.preferences

import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.model.player.Resume
import dev.gokanaz.kplayer.core.model.player.DecoderPriority
import dev.gokanaz.kplayer.core.model.player.FastSeek
import dev.gokanaz.kplayer.core.model.player.VideoContentScale
import dev.gokanaz.kplayer.core.model.player.DoubleTapConfig
import dev.gokanaz.kplayer.core.model.ui.ControlButtonsPosition
import dev.gokanaz.kplayer.core.model.ui.Font

data class PlayerPreferences(
    val playbackSpeed: Float = 1.0f,
    val defaultQuality: String = "auto",
    val autoPlay: Boolean = true,
    val repeatMode: LoopMode = LoopMode.NONE,
    val shuffleMode: Boolean = false,
    val volume: Int = 100,
    val isMuted: Boolean = false,
    val equalizerEnabled: Boolean = false,
    val equalizerBands: Map<Int, Int> = emptyMap(),
    val subtitlesEnabled: Boolean = true,
    val subtitleLanguage: String = "en",
    val subtitleSize: Int = 16,
    val subtitleFont: Font = Font.SYSTEM,
    val seekGestureEnabled: Boolean = true,
    val volumeGestureEnabled: Boolean = true,
    val brightnessGestureEnabled: Boolean = true,
    val pipEnabled: Boolean = true,
    val backgroundPlayEnabled: Boolean = false,
    val resumePositions: Map<String, Long> = emptyMap(),
    val playbackHistory: List<String> = emptyList(),
    val videoContentScale: VideoContentScale = VideoContentScale.FIT,
    val doubleTapConfig: DoubleTapConfig = DoubleTapConfig(),
    val controlButtonsPosition: ControlButtonsPosition = ControlButtonsPosition.BOTTOM,
    val decoderPriority: DecoderPriority = DecoderPriority.AUTO,
    val fastSeek: FastSeek = FastSeek.WIFI_ONLY,
    val resumePreference: Resume = Resume.ASK
)
