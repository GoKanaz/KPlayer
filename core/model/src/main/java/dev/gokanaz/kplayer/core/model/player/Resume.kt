package dev.gokanaz.kplayer.core.model.player

enum class Resume {
    ALWAYS,
    ASK,
    NEVER
}

data class ResumePreference(
    val mode: Resume = Resume.ASK,
    val thresholdSeconds: Int = 10
)
