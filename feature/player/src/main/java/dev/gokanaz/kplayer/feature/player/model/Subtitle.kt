package dev.gokanaz.kplayer.feature.player.model

import android.net.Uri
import java.io.InputStream
import java.util.Locale

enum class SubtitleFormat {
    SRT,
    VTT,
    ASS,
    SSA,
    TXT,
    UNKNOWN;
    
    companion object {
        fun fromExtension(extension: String): SubtitleFormat {
            return when (extension.lowercase(Locale.US)) {
                "srt" -> SRT
                "vtt", "webvtt" -> VTT
                "ass" -> ASS
                "ssa" -> SSA
                "txt" -> TXT
                else -> UNKNOWN
            }
        }
        
        fun fromMimeType(mimeType: String): SubtitleFormat {
            return when (mimeType) {
                "text/x-subviewer",
                "text/x-srt",
                "application/x-subrip" -> SRT
                "text/vtt",
                "text/webvtt" -> VTT
                "text/x-ssa",
                "text/x-ass" -> ASS
                else -> UNKNOWN
            }
        }
    }
}

enum class SubtitleAlignment {
    LEFT,
    CENTER,
    RIGHT,
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,
    MIDDLE_LEFT,
    MIDDLE_CENTER,
    MIDDLE_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_CENTER,
    BOTTOM_RIGHT
}

data class Subtitle(
    val id: String,
    val uri: String,
    val format: SubtitleFormat,
    val language: String = "und",
    val title: String = "Subtitles",
    val isDefault: Boolean = false,
    val isForced: Boolean = false,
    val isExternal: Boolean = true,
    val items: List<SubtitleItem> = emptyList()
)

data class SubtitleItem(
    val index: Int,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val segments: List<SubtitleSegment> = emptyList(),
    val position: SubtitlePosition? = null,
    val alignment: SubtitleAlignment = SubtitleAlignment.BOTTOM_CENTER,
    val layer: Int = 0,
    val style: String? = null
)

data class SubtitleSegment(
    val text: String,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false,
    val isStrikethrough: Boolean = false,
    val fontName: String? = null,
    val fontSize: Float? = null,
    val color: Int? = null,
    val bgColor: Int? = null,
    val url: String? = null
)

data class SubtitlePosition(
    val x: Float,
    val y: Float,
    val relativeToVideo: Boolean = true
)

interface SubtitleParser {
    fun parse(inputStream: InputStream, encoding: String? = null): List<SubtitleItem>
    fun canParse(uri: Uri): Boolean
    fun getFormat(): SubtitleFormat
}

class SrtParser : SubtitleParser {
    override fun parse(inputStream: InputStream, encoding: String?): List<SubtitleItem> {
        val items = mutableListOf<SubtitleItem>()
        val content = inputStream.bufferedReader(encoding ?: "UTF-8").readText()
        val blocks = content.split(Regex("\\n\\s*\\n"))
        
        for (block in blocks) {
            val lines = block.trim().split("\n")
            if (lines.size < 3) continue
            
            try {
                val index = lines[0].toIntOrNull() ?: continue
                val timeMatch = Regex("(\\d{2}:\\d{2}:\\d{2},\\d{3})\\s*-->\\s*(\\d{2}:\\d{2}:\\d{2},\\d{3})")
                    .find(lines[1]) ?: continue
                
                val startTime = parseTimecode(timeMatch.groupValues[1])
                val endTime = parseTimecode(timeMatch.groupValues[2])
                val text = lines.subList(2, lines.size).joinToString("\n")
                
                items.add(
                    SubtitleItem(
                        index = index,
                        startTimeMs = startTime,
                        endTimeMs = endTime,
                        segments = listOf(SubtitleSegment(text = text))
                    )
                )
            } catch (e: Exception) {
                continue
            }
        }
        
        return items
    }
    
    override fun canParse(uri: Uri): Boolean {
        return uri.toString().lowercase(Locale.US).endsWith(".srt")
    }
    
    override fun getFormat(): SubtitleFormat = SubtitleFormat.SRT
    
    private fun parseTimecode(timecode: String): Long {
        val parts = timecode.split(":", ",")
        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val seconds = parts[2].toLong()
        val millis = parts[3].toLong()
        return hours * 3600000 + minutes * 60000 + seconds * 1000 + millis
    }
}

class VttParser : SubtitleParser {
    override fun parse(inputStream: InputStream, encoding: String?): List<SubtitleItem> {
        val items = mutableListOf<SubtitleItem>()
        val content = inputStream.bufferedReader(encoding ?: "UTF-8").readText()
        
        if (!content.startsWith("WEBVTT")) return emptyList()
        
        val lines = content.split("\n")
        var index = 1
        var i = 1
        
        while (i < lines.size) {
            val line = lines[i].trim()
            
            if (line.isEmpty()) {
                i++
                continue
            }
            
            if (line.contains("-->")) {
                val timeParts = line.split("-->").map { it.trim() }
                if (timeParts.size >= 2) {
                    val startTime = parseWebVTTTimecode(timeParts[0])
                    val endTime = parseWebVTTTimecode(timeParts[1])
                    val text = StringBuilder()
                    
                    i++
                    while (i < lines.size && lines[i].trim().isNotEmpty()) {
                        if (text.isNotEmpty()) text.append("\n")
                        text.append(lines[i].trim())
                        i++
                    }
                    
                    items.add(
                        SubtitleItem(
                            index = index++,
                            startTimeMs = startTime,
                            endTimeMs = endTime,
                            segments = listOf(SubtitleSegment(text = text.toString()))
                        )
                    )
                }
            }
            i++
        }
        
        return items
    }
    
    override fun canParse(uri: Uri): Boolean {
        val uriStr = uri.toString().lowercase(Locale.US)
        return uriStr.endsWith(".vtt") || uriStr.endsWith(".webvtt")
    }
    
    override fun getFormat(): SubtitleFormat = SubtitleFormat.VTT
    
    private fun parseWebVTTTimecode(timecode: String): Long {
        val cleanTimecode = timecode.replace(".", ",")
        return SrtParser().parseTimecode(cleanTimecode)
    }
}

class AssParser : SubtitleParser {
    override fun parse(inputStream: InputStream, encoding: String?): List<SubtitleItem> {
        val items = mutableListOf<SubtitleItem>()
        val content = inputStream.bufferedReader(encoding ?: "UTF-8").readText()
        val lines = content.split("\n")
        
        var formatLine: String? = null
        var index = 1
        
        for (line in lines) {
            val trimmed = line.trim()
            
            if (trimmed.startsWith("Format:")) {
                formatLine = trimmed.substring(7).trim()
                continue
            }
            
            if (trimmed.startsWith("Dialogue:")) {
                if (formatLine == null) continue
                
                val parts = trimmed.split(",")
                if (parts.size < 10) continue
                
                try {
                    val startTime = parseAssTimecode(parts[1].trim())
                    val endTime = parseAssTimecode(parts[2].trim())
                    val text = parts.subList(9, parts.size).joinToString(",")
                    
                    val segments = parseAssTags(text)
                    
                    items.add(
                        SubtitleItem(
                            index = index++,
                            startTimeMs = startTime,
                            endTimeMs = endTime,
                            segments = segments
                        )
                    )
                } catch (e: Exception) {
                    continue
                }
            }
        }
        
        return items
    }
    
    override fun canParse(uri: Uri): Boolean {
        val uriStr = uri.toString().lowercase(Locale.US)
        return uriStr.endsWith(".ass") || uriStr.endsWith(".ssa")
    }
    
    override fun getFormat(): SubtitleFormat = SubtitleFormat.ASS
    
    private fun parseAssTimecode(timecode: String): Long {
        val parts = timecode.split(":")
        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        val secondsParts = parts[2].split(".")
        val seconds = secondsParts[0].toLong()
        val hundredths = secondsParts.getOrNull(1)?.toLong() ?: 0
        
        return hours * 3600000 + minutes * 60000 + seconds * 1000 + hundredths * 10
    }
    
    private fun parseAssTags(text: String): List<SubtitleSegment> {
        val segments = mutableListOf<SubtitleSegment>()
        var currentText = StringBuilder()
        var isBold = false
        var isItalic = false
        var isUnderline = false
        var color: Int? = null
        
        var i = 0
        while (i < text.length) {
            when {
                text[i] == '{' -> {
                    if (currentText.isNotEmpty()) {
                        segments.add(
                            SubtitleSegment(
                                text = currentText.toString(),
                                isBold = isBold,
                                isItalic = isItalic,
                                isUnderline = isUnderline,
                                color = color
                            )
                        )
                        currentText = StringBuilder()
                    }
                    
                    val tagEnd = text.indexOf('}', i)
                    if (tagEnd > i) {
                        val tag = text.substring(i + 1, tagEnd)
                        when {
                            "\\b1" in tag -> isBold = true
                            "\\b0" in tag -> isBold = false
                            "\\i1" in tag -> isItalic = true
                            "\\i0" in tag -> isItalic = false
                            "\\u1" in tag -> isUnderline = true
                            "\\u0" in tag -> isUnderline = false
                        }
                        
                        val colorMatch = Regex("\\\\c&H([0-9A-Fa-f]{6})&?").find(tag)
                        if (colorMatch != null) {
                            color = android.graphics.Color.parseColor("#${colorMatch.groupValues[1]}")
                        }
                        
                        i = tagEnd
                    }
                }
                else -> {
                    currentText.append(text[i])
                }
            }
            i++
        }
        
        if (currentText.isNotEmpty()) {
            segments.add(
                SubtitleSegment(
                    text = currentText.toString(),
                    isBold = isBold,
                    isItalic = isItalic,
                    isUnderline = isUnderline,
                    color = color
                )
            )
        }
        
        return segments
    }
}

class TxtParser : SubtitleParser {
    override fun parse(inputStream: InputStream, encoding: String?): List<SubtitleItem> {
        val content = inputStream.bufferedReader(encoding ?: "UTF-8").readText()
        return listOf(
            SubtitleItem(
                index = 1,
                startTimeMs = 0,
                endTimeMs = Long.MAX_VALUE,
                segments = listOf(SubtitleSegment(text = content))
            )
        )
    }
    
    override fun canParse(uri: Uri): Boolean {
        return uri.toString().lowercase(Locale.US).endsWith(".txt")
    }
    
    override fun getFormat(): SubtitleFormat = SubtitleFormat.TXT
}

class SubtitleParserFactory {
    private val parsers = listOf(
        SrtParser(),
        VttParser(),
        AssParser(),
        TxtParser()
    )
    
    fun getParser(uri: Uri): SubtitleParser? {
        return parsers.firstOrNull { it.canParse(uri) }
    }
    
    fun getParser(format: SubtitleFormat): SubtitleParser? {
        return when (format) {
            SubtitleFormat.SRT -> SrtParser()
            SubtitleFormat.VTT -> VttParser()
            SubtitleFormat.ASS, SubtitleFormat.SSA -> AssParser()
            SubtitleFormat.TXT -> TxtParser()
            SubtitleFormat.UNKNOWN -> null
        }
    }
}
