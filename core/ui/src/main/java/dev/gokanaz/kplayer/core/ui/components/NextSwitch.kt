package dev.gokanaz.kplayer.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun NextSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    description: String? = null
) {
    if (label != null) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            color = androidx.compose.ui.graphics.Color.Transparent,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (enabled) AppTheme.colorScheme.onSurface else AppTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                    if (description != null) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (enabled) AppTheme.colorScheme.onSurfaceVariant else AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    enabled = enabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = AppTheme.colorScheme.primary,
                        checkedTrackColor = AppTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = AppTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = AppTheme.colorScheme.surfaceVariant,
                        disabledCheckedThumbColor = AppTheme.colorScheme.primary.copy(alpha = 0.38f),
                        disabledCheckedTrackColor = AppTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
                        disabledUncheckedThumbColor = AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                        disabledUncheckedTrackColor = AppTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
                    )
                )
            }
        }
    } else {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colorScheme.primary,
                checkedTrackColor = AppTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = AppTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = AppTheme.colorScheme.surfaceVariant
            )
        )
    }
}
