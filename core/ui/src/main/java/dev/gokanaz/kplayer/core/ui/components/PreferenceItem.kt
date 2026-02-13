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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun PreferenceItem(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else Modifier
            ),
        color = androidx.compose.ui.graphics.Color.Transparent,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    color = AppTheme.colorScheme.onSurface
                )
                
                if (summary != null) {
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (trailingContent != null) {
                Spacer(modifier = Modifier.width(16.dp))
                trailingContent()
            }
        }
    }
}

@Composable
fun ClickablePreferenceItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
    iconTint: androidx.compose.ui.graphics.Color = AppTheme.colorScheme.primary,
    trailingContent: @Composable (() -> Unit)? = null
) {
    PreferenceItem(
        title = title,
        summary = summary,
        icon = icon,
        iconTint = iconTint,
        onClick = onClick,
        modifier = modifier,
        trailingContent = trailingContent
    )
}
