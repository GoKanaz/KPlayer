package dev.gokanaz.kplayer.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun PreferenceSwitchWithDivider(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.primary,
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        PreferenceSwitch(
            title = title,
            checked = checked,
            onCheckedChange = onCheckedChange,
            summary = summary,
            icon = icon,
            iconTint = iconTint,
            enabled = enabled
        )
        HorizontalDivider(
            modifier = Modifier.padding(
                start = if (icon != null) 56.dp else 16.dp,
                end = 16.dp
            ),
            color = AppTheme.colorScheme.outlineVariant
        )
    }
}
