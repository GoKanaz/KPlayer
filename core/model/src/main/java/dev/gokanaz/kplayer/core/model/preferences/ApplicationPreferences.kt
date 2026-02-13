package dev.gokanaz.kplayer.core.model.preferences

import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.ui.MediaViewMode

data class ApplicationPreferences(
    val darkMode: DarkModePreference = DarkModePreference.SYSTEM,
    val appLanguage: String = "system",
    val dynamicColorEnabled: Boolean = true,
    val firstRun: Boolean = true,
    val onboardingCompleted: Boolean = false,
    val defaultVideoSortType: SortType = SortType.NAME,
    val defaultSortOrder: SortOrder = SortOrder.ASCENDING,
    val defaultViewType: MediaViewMode = MediaViewMode.GRID,
    val storagePaths: List<String> = emptyList(),
    val storagePermissionGranted: Boolean = false
)

enum class DarkModePreference {
    SYSTEM,
    LIGHT,
    DARK
}
