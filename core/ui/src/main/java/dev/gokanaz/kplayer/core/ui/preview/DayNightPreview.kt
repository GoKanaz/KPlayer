package dev.gokanaz.kplayer.core.ui.preview

import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Mode",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Preview(
    name = "Dark Mode",
    group = "Theme",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
annotation class DayNightPreview

@Preview(
    name = "Light Mode - Large Font",
    group = "Accessibility",
    showBackground = true,
    fontScale = 1.5f
)
@Preview(
    name = "Dark Mode - Large Font",
    group = "Accessibility",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    fontScale = 1.5f
)
annotation class AccessibilityPreview
