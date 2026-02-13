package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable
import dev.gokanaz.kplayer.feature.player.model.SubtitleItem
import dev.gokanaz.kplayer.feature.player.model.SubtitleSegment
import dev.gokanaz.kplayer.feature.player.model.SubtitleAlignment

@Immutable
data class CuesState(
    val activeCues: List<SubtitleItem> = emptyList(),
    val upcomingCues: List<SubtitleItem> = emptyList(),
    val isSubtitleEnabled: Boolean = true,
    val selectedTrackId: String = "",
    val fontFamily: String = "sans-serif",
    val fontSize: Int = 16,
    val fontColor: Int = 0xFFFFFFFF.toInt(),
    val bgColor: Int = 0xCC000000.toInt(),
    val alignment: SubtitleAlignment = SubtitleAlignment.BOTTOM_CENTER,
    val positionX: Float = 0.5f,
    val positionY: Float = 0.9f,
    val delayMs: Int = 0,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false
) {
    companion object {
        val Initial = CuesState(
            isSubtitleEnabled = true,
            fontSize = 16,
            fontColor = 0xFFFFFFFF.toInt(),
            bgColor = 0xCC000000.toInt()
        )
        
        val Sample = CuesState(
            activeCues = listOf(
                SubtitleItem(
                    index = 1,
                    startTimeMs = 1000,
                    endTimeMs = 5000,
                    segments = listOf(
                        SubtitleSegment(text = "Hello, this is a sample subtitle")
                    )
                )
            ),
            isSubtitleEnabled = true,
            fontSize = 18,
            fontColor = 0xFFFFFF00.toInt(),
            bgColor = 0xCC000000.toInt()
        )
    }
    
    fun withActiveCues(positionMs: Long, cues: List<SubtitleItem>): CuesState {
        val active = cues.filter { cue ->
            positionMs >= cue.startTimeMs + delayMs && 
            positionMs <= cue.endTimeMs + delayMs
        }
        
        val upcoming = cues.filter { cue ->
            positionMs < cue.startTimeMs + delayMs
        }.sortedBy { it.startTimeMs }
        
        return copy(
            activeCues = active,
            upcomingCues = upcoming.take(5)
        )
    }
    
    fun withSubtitleEnabled(enabled: Boolean): CuesState {
        return copy(isSubtitleEnabled = enabled)
    }
    
    fun withTrackSelected(trackId: String): CuesState {
        return copy(selectedTrackId = trackId)
    }
    
    fun withDelay(delayMs: Int): CuesState {
        return copy(delayMs = delayMs)
    }
    
    fun withFontFamily(fontFamily: String): CuesState {
        return copy(fontFamily = fontFamily)
    }
    
    fun withFontSize(size: Int): CuesState {
        return copy(fontSize = size.coerceIn(10, 32))
    }
    
    fun withFontColor(color: Int): CuesState {
        return copy(fontColor = color)
    }
    
    fun withBackgroundColor(color: Int): CuesState {
        return copy(bgColor = color)
    }
    
    fun withAlignment(alignment: SubtitleAlignment): CuesState {
        return copy(alignment = alignment)
    }
    
    fun withPosition(x: Float, y: Float): CuesState {
        return copy(
            positionX = x.coerceIn(0f, 1f),
            positionY = y.coerceIn(0f, 1f)
        )
    }
    
    fun withBold(enabled: Boolean): CuesState {
        return copy(isBold = enabled)
    }
    
    fun withItalic(enabled: Boolean): CuesState {
        return copy(isItalic = enabled)
    }
    
    fun withUnderline(enabled: Boolean): CuesState {
        return copy(isUnderline = enabled)
    }
    
    fun clearCues(): CuesState {
        return copy(
            activeCues = emptyList(),
            upcomingCues = emptyList()
        )
    }
    
    fun getFormattedText(): String {
        return activeCues.joinToString("\n") { cue ->
            cue.segments.joinToString("") { it.text }
        }
    }
}
