package dev.gokanaz.kplayer.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun RadioTextButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.primary,
    enabled: Boolean = true,
    description: String? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (enabled) {
                    Modifier.clickable(onClick = onClick)
                } else Modifier
            ),
        color = androidx.compose.ui.graphics.Color.Transparent,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = AppTheme.colorScheme.primary,
                    unselectedColor = AppTheme.colorScheme.onSurfaceVariant,
                    disabledSelectedColor = AppTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledUnselectedColor = AppTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
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
                    text = text,
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
        }
    }
}
