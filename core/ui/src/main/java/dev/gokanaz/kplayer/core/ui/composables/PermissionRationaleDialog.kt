package dev.gokanaz.kplayer.core.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.R
import dev.gokanaz.kplayer.core.ui.components.NextCustomDialog
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun PermissionRationaleDialog(
    title: String,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    NextCustomDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = NextIconPainter(NextIcon.Info),
                contentDescription = null,
                tint = AppTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = AppTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.permission_continue))
            }
            
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.permission_cancel))
            }
        }
    }
}
