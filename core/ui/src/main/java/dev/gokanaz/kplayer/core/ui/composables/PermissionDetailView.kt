package dev.gokanaz.kplayer.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.R
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun PermissionDetailView(
    permissionName: String,
    permissionDescription: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = NextIconPainter(icon?.let { NextIcon.Info } ?: NextIcon.Info),
            contentDescription = null,
            tint = AppTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = permissionName,
            style = MaterialTheme.typography.headlineSmall,
            color = AppTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = permissionDescription,
            style = MaterialTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.permission_open_settings))
        }
    }
}
