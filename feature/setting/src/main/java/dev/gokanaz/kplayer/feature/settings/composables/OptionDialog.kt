package dev.gokanaz.kplayer.feature.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.components.RadioTextButton

@Composable
fun OptionDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    onDismiss: () -> Unit,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (options.size > 5) 300.dp else (options.size * 60).dp)
            ) {
                itemsIndexed(options) { index, option ->
                    RadioTextButton(
                        text = option,
                        selected = index == selectedIndex,
                        onClick = { onOptionSelected(index) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        modifier = modifier
    )
}

@Composable
fun MultiSelectDialog(
    title: String,
    options: List<String>,
    selectedIndices: Set<Int>,
    onDismiss: () -> Unit,
    onConfirm: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    var localSelected by remember(selectedIndices) { mutableStateOf(selectedIndices.toMutableSet()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (options.size > 5) 300.dp else (options.size * 60).dp)
            ) {
                itemsIndexed(options) { index, option ->
                    CheckboxRow(
                        text = option,
                        checked = index in localSelected,
                        onCheckedChange = { checked ->
                            if (checked) {
                                localSelected.add(index)
                            } else {
                                localSelected.remove(index)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (index < options.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(localSelected) }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        modifier = modifier
    )
}
