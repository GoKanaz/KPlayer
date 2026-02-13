package dev.gokanaz.kplayer.core.ui.preview

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone Portrait",
    group = "Devices",
    device = Devices.PHONE,
    showBackground = true
)
@Preview(
    name = "Phone Landscape",
    group = "Devices",
    device = Devices.PHONE,
    showBackground = true,
    widthDp = 640,
    heightDp = 360
)
@Preview(
    name = "Tablet Portrait",
    group = "Devices",
    device = Devices.TABLET,
    showBackground = true
)
@Preview(
    name = "Tablet Landscape",
    group = "Devices",
    device = Devices.TABLET,
    showBackground = true,
    widthDp = 800,
    heightDp = 600
)
@Preview(
    name = "Foldable",
    group = "Devices",
    device = "id:foldable",
    showBackground = true
)
@Preview(
    name = "Desktop",
    group = "Devices",
    device = "id:desktop_medium",
    showBackground = true
)
annotation class DevicePreviews

@Preview(
    name = "RTL Layout",
    group = "Localization",
    showBackground = true,
    locale = "ar"
)
annotation class RtlPreview
