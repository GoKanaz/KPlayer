package dev.gokanaz.kplayer.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun PreferenceSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.primary,
    enabled: Boolean = true,
    steps: Int = 0,
    valueFormat: (Float) -> String = { "${it.toInt()}" },
    onValueChangeFinished: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = androidx.compose.ui.graphics.Color.Transparent,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (enabled) AppTheme.colorScheme.onSurface else AppTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                    
                    if (summary != null) {
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (enabled) AppTheme.colorScheme.onSurfaceVariant else AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                        )
                    }
                }
                
                Text(
                    text = valueFormat(value),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) AppTheme.colorScheme.primary else AppTheme.colorScheme.primary.copy(alpha = 0.38f)
                )
            }
            
            Slider(
                value = value,
                onValueChange = onValueChange,
                onValueChangeFinished = onValueChangeFinished,
                valueRange = valueRange,
                steps = steps,
                enabled = enabled,
                colors = SliderDefaults.colors(
                    thumbColor = AppTheme.colorScheme.primary,
                    activeTrackColor = AppTheme.colorScheme.primary,
                    inactiveTrackColor = AppTheme.colorScheme.surfaceVariant,
                    disabledThumbColor = AppTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledActiveTrackColor = AppTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledInactiveTrackColor = AppTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
