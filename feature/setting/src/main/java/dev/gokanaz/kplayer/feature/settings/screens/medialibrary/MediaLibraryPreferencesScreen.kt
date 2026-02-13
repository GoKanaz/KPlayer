package dev.gokanaz.kplayer.feature.settings.screens.medialibrary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Scan
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StoragePathInfo(
    val path: String,
    val videoCount: Int,
    val totalSize: Long,
    val lastScanTime: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaLibraryPreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: MediaLibraryPreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    var showExcludeDialog by remember { mutableStateOf(false) }
    var showClearAllDialog by remember { mutableStateOf(false) }
    var excludePattern by remember { mutableStateOf("") }
    var selectedScanInterval by remember { mutableStateOf("Daily") }
    var showIntervalMenu by remember { mutableStateOf(false) }
    
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.media_library)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::syncNow) {
                        if (uiState.isSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = "Sync")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.storage_paths))
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            value = uiState.storagePaths.size.toString(),
                            label = "Folders",
                            icon = Icons.Default.Folder
                        )
                        StatItem(
                            value = uiState.totalVideos.toString(),
                            label = "Videos",
                            icon = Icons.Default.PlayCircle
                        )
                        StatItem(
                            value = formatFileSize(uiState.totalSize),
                            label = "Total Size",
                            icon = Icons.Default.Storage
                        )
                    }
                }
            }
            
            items(uiState.storagePaths) { pathInfo ->
                StoragePathItem(
                    pathInfo = pathInfo,
                    dateFormat = dateFormat,
                    onRemove = { viewModel.removeStoragePath(it) }
                )
            }
            
            item {
                Button(
                    onClick = viewModel::addStoragePath,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(dev.gokanaz.kplayer.core.R.string.add_folder))
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_settings))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Scan,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_on_startup),
                    checked = uiState.scanOnStartup,
                    onCheckedChange = viewModel::updateScanOnStartup
                )
                
                SwitchPreference(
                    icon = Icons.Default.Scan,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_periodically),
                    checked = uiState.scanPeriodically,
                    onCheckedChange = viewModel::updateScanPeriodically
                )
                
                if (uiState.scanPeriodically) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.scan_interval),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Box {
                            Text(
                                text = uiState.scanInterval,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { showIntervalMenu = true }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                            
                            DropdownMenu(
                                expanded = showIntervalMenu,
                                onDismissRequest = { showIntervalMenu = false }
                            ) {
                                listOf("15 min", "30 min", "1 hour", "3 hours", "6 hours", "12 hours", "Daily").forEach { interval ->
                                    DropdownMenuItem(
                                        text = { Text(interval) },
                                        onClick = {
                                            viewModel.updateScanInterval(interval)
                                            showIntervalMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.Wifi,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_only_on_wifi),
                    checked = uiState.scanOnlyOnWifi,
                    onCheckedChange = viewModel::updateScanOnlyOnWifi
                )
                
                SwitchPreference(
                    icon = Icons.Default.Settings,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_only_when_charging),
                    checked = uiState.scanOnlyWhenCharging,
                    onCheckedChange = viewModel::updateScanOnlyWhenCharging
                )
                
                SwitchPreference(
                    icon = Icons.Default.FolderOpen,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_hidden_folders),
                    checked = uiState.scanHiddenFolders,
                    onCheckedChange = viewModel::updateScanHiddenFolders
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.max_scan_depth))
                    }
                    
                    Text(
                        text = "${uiState.maxScanDepth} levels",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = uiState.maxScanDepth.toFloat(),
                    onValueChange = { viewModel.updateMaxScanDepth(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.excluded_folders))
            }
            
            items(uiState.excludedFolders) { folder ->
                ExcludedFolderItem(
                    folder = folder,
                    onRemove = { viewModel.removeExcludedFolder(folder) }
                )
            }
            
            item {
                OutlinedButton(
                    onClick = { showExcludeDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(dev.gokanaz.kplayer.core.R.string.add_exclude_pattern))
                }
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pattern Examples:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• .* - Hidden files/folders\n• cache/ - Cache folders\n• temp/ - Temporary files\n• *.tmp - Files with .tmp extension",
                            fontSize = 12.sp
                        )
                    }
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.media_management))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Image,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.show_thumbnails),
                    checked = uiState.showThumbnails,
                    onCheckedChange = viewModel::updateShowThumbnails
                )
                
                if (uiState.showThumbnails) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.thumbnail_quality),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = uiState.thumbnailQuality,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = when (uiState.thumbnailQuality) {
                            "Low" -> 0f
                            "Medium" -> 1f
                            else -> 2f
                        },
                        onValueChange = { value ->
                            viewModel.updateThumbnailQuality(
                                when (value.toInt()) {
                                    0 -> "Low"
                                    1 -> "Medium"
                                    else -> "High"
                                }
                            )
                        },
                        valueRange = 0f..2f,
                        steps = 2,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                
                CacheItem(
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.thumbnail_cache),
                    size = uiState.thumbnailCacheSize,
                    onClear = viewModel::clearThumbnailCache
                )
                
                CacheItem(
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.metadata_cache),
                    size = uiState.metadataCacheSize,
                    onClear = viewModel::clearMetadataCache
                )
                
                OutlinedButton(
                    onClick = { /* Show regenerate confirmation */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(dev.gokanaz.kplayer.core.R.string.regenerate_thumbnails))
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.auto_sync))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Sync,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.auto_sync_on_resume),
                    checked = uiState.autoSyncOnResume,
                    onCheckedChange = viewModel::updateAutoSyncOnResume
                )
                
                SwitchPreference(
                    icon = Icons.Default.Info,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.auto_sync_on_new_files),
                    checked = uiState.autoSyncOnNewFiles,
                    onCheckedChange = viewModel::updateAutoSyncOnNewFiles
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(dev.gokanaz.kplayer.core.R.string.last_sync),
                        fontSize = 16.sp
                    )
                    
                    Text(
                        text = dateFormat.format(Date(uiState.lastSyncTime)),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.clear_all_media_data),
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "This will remove all media cache, scan data, and preferences",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        
                        Button(
                            onClick = { showClearAllDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Clear All")
                        }
                    }
                }
            }
        }
    }
    
    if (showExcludeDialog) {
        AlertDialog(
            onDismissRequest = { showExcludeDialog = false },
            title = { Text("Add Exclude Pattern") },
            text = {
                Column {
                    Text("Enter folder name or pattern to exclude from scanning:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = excludePattern,
                        onValueChange = { excludePattern = it },
                        placeholder = { Text("e.g., cache/, .*, temp") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (excludePattern.isNotBlank()) {
                            viewModel.addExcludedFolder(excludePattern)
                            excludePattern = ""
                            showExcludeDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExcludeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            title = { Text("Clear All Media Data") },
            text = { Text("Are you absolutely sure? This action cannot be undone. All media cache, scan history, and preferences will be permanently deleted.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllMediaData()
                        showClearAllDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Yes, Clear Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearAllDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatItem(
    value: String,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StoragePathItem(
    pathInfo: StoragePathInfo,
    dateFormat: SimpleDateFormat,
    onRemove: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(40.dp))
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pathInfo.path,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                
                Text(
                    text = "${pathInfo.videoCount} videos • ${formatFileSize(pathInfo.totalSize)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Last scan: ${dateFormat.format(Date(pathInfo.lastScanTime))}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { onRemove(pathInfo.path) }) {
                Icon(Icons.Default.Clear, contentDescription = "Remove")
            }
        }
    }
}

@Composable
fun CacheItem(
    title: String,
    size: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp
                )
                Text(
                    text = size,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        TextButton(onClick = onClear) {
            Text(stringResource(dev.gokanaz.kplayer.core.R.string.clear))
        }
    }
}

@Composable
fun ExcludedFolderItem(
    folder: String,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(24.dp))
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = folder,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
        
        IconButton(onClick = { onRemove(folder) }) {
            Icon(Icons.Default.Clear, contentDescription = "Remove")
        }
    }
    
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun SwitchPreference(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun PreferenceSection(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

private fun formatFileSize(size: Long): String {
    val units = listOf("B", "KB", "MB", "GB", "TB")
    var fileSize = size.toFloat()
    var unitIndex = 0
    
    while (fileSize >= 1024 && unitIndex < units.lastIndex) {
        fileSize /= 1024
        unitIndex++
    }
    
    return String.format("%.1f %s", fileSize, units[unitIndex])
}
