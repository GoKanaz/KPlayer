package dev.gokanaz.kplayer.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun NextAlertDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    icon: ImageVector? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = dismissText)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = AppTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = title)
            }
        },
        text = {
            Text(text = text)
        },
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        containerColor = AppTheme.colorScheme.surface,
        titleContentColor = AppTheme.colorScheme.onSurface,
        textContentColor = AppTheme.colorScheme.onSurfaceVariant,
        iconContentColor = AppTheme.colorScheme.primary
    )
}

@Composable
fun <T> NextListDialog(
    title: String,
    items: List<T>,
    onDismissRequest: () -> Unit,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Close")
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            LazyColumn {
                items(items) { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item) },
                        color = androidx.compose.ui.graphics.Color.Transparent
                    ) {
                        itemContent(item)
                    }
                }
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        containerColor = AppTheme.colorScheme.surface
    )
}

@Composable
fun <T> NextRadioDialog(
    title: String,
    items: List<T>,
    selectedItem: T,
    onDismissRequest: () -> Unit,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemLabel: @Composable (T) -> String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "OK")
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            LazyColumn {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemSelected(item) }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = item == selectedItem,
                            onClick = { onItemSelected(item) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = itemLabel(item),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        containerColor = AppTheme.colorScheme.surface
    )
}

@Composable
fun <T> NextCheckboxDialog(
    title: String,
    items: List<T>,
    selectedItems: Set<T>,
    onDismissRequest: () -> Unit,
    onItemToggle: (T, Boolean) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    itemLabel: @Composable (T) -> String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            LazyColumn {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val isSelected = item in selectedItems
                                onItemToggle(item, !isSelected)
                            }
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item in selectedItems,
                            onCheckedChange = { onItemToggle(item, it) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AppTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = itemLabel(item),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        containerColor = AppTheme.colorScheme.surface
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextCustomDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        content = {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = AppTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(24.dp)
            ) {
                content()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextFullScreenDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.fillMaxSize(),
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = AppTheme.colorScheme.surface
            ) {
                content()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = AppTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content()
        }
    }
}
