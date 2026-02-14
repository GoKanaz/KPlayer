package dev.gokanaz.kplayer.core.model.media

enum class VideoQuality(val value: Int) {
    AUTO(0),
    P144(144),
    P240(240),
    P360(360),
    P480(480),
    P720(720),
    P1080(1080),
    P1440(1440),
    P2160(2160);

    companion object {
        fun fromValue(value: Int): VideoQuality {
            return entries.find { it.value == value } ?: AUTO
        }
    }
}
