package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onRename: (String, Boolean) -> Unit,
    itemType: RenameItemType = RenameItemType.File,
    isVisible: Boolean = true
) {
    if (!isVisible) return
    
    var newName by remember { mutableStateOf(currentName) }
    var preserveExtension by remember { mutableStateOf(true) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var isRenaming by remember { mutableStateOf(false) }
    var suggestedName by remember { mutableStateOf<String?>(null) }
    
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Rename ${itemType.displayName}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newValue ->
                        newName = newValue
                        validationError = validateName(newValue, itemType)
                        
                        if (validationError == null && newValue.isNotBlank() && newValue != currentName) {
                            scope.launch {
                                delay(500)
                                suggestedName = generateSuggestion(newValue)
                            }
                        } else {
                            suggestedName = null
                        }
                    },
                    label = { Text("New name") },
                    placeholder = { Text("Enter new name") },
                    isError = validationError != null,
                    supportingText = {
                        if (validationError != null) {
                            Text(
                                text = validationError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (validationError == null && newName.isNotBlank()) {
                                keyboardController?.hide()
                                onRename(newName, preserveExtension)
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
                
                if (itemType == RenameItemType.File) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = preserveExtension,
                            onCheckedChange = { preserveExtension = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Preserve file extension",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                if (suggestedName != null && suggestedName != newName) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Suggested name:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Text(
                                    text = suggestedName!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            
                            TextButton(
                                onClick = {
                                    newName = suggestedName!!
                                    suggestedName = null
                                    validationError = validateName(suggestedName!!, itemType)
                                }
                            ) {
                                Text("Use")
                            }
                        }
                    }
                }
                
                if (isRenaming) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (validationError == null && newName.isNotBlank()) {
                        isRenaming = true
                        keyboardController?.hide()
                        onRename(newName, preserveExtension)
                    }
                },
                enabled = validationError == null && newName.isNotBlank() && !isRenaming
            ) {
                Text("Rename")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isRenaming
            ) {
                Text("Cancel")
            }
        }
    )
}

private fun validateName(name: String, itemType: RenameItemType): String? {
    if (name.isBlank()) {
        return "Name cannot be empty"
    }
    
    if (name.length > 255) {
        return "Name is too long (max 255 characters)"
    }
    
    val invalidChars = when (itemType) {
        RenameItemType.File -> setOf('<', '>', ':', '"', '/', '\\', '|', '?', '*')
        RenameItemType.Folder -> setOf('<', '>', ':', '"', '/', '\\', '|', '?', '*')
    }
    
    val foundInvalidChar = name.firstOrNull { it in invalidChars }
    if (foundInvalidChar != null) {
        return "Invalid character: '$foundInvalidChar'"
    }
    
    if (name.startsWith('.') && itemType == RenameItemType.File) {
        return "Filename cannot start with '.'"
    }
    
    if (name.endsWith('.')) {
        return "Filename cannot end with '.'"
    }
    
    return null
}

private fun generateSuggestion(name: String): String {
    val timestamp = System.currentTimeMillis() % 1000
    return "$name ($timestamp)"
}

enum class RenameItemType(
    val displayName: String
) {
    File("File"),
    Folder("Folder")
}
