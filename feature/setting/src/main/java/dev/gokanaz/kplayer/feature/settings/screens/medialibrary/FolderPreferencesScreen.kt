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
import androidx.compose.material.icons.filled.Scan
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import dev.gokanaz.kplayer.core.ui.R

data class StoragePath(
    val path: String,
    val videoCount: Int,
    val totalSize: String,
    val lastScan: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderPreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: MediaLibraryPreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    var showExcludeDialog by remember { mutableStateOf(false) }
    var showClearCacheDialog by remember { mutableStateOf(false) }
    var pathToRemove by remember { mutableStateOf<String?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.media_library)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            
            items(uiState.storagePaths) { path ->
                StoragePathItem(
                    path = path,
                    onRemove = { pathToRemove = it.path }
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
                        
                        Text(
                            text = uiState.scanInterval,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { /* Show interval picker */ }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.Wifi,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_only_on_wifi),
                    checked = uiState.scanOnlyOnWifi,
                    onCheckedChange = viewModel::updateScanOnlyOnWifi
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
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.file_extensions))
                    }
                    
                    Text(
                        text = uiState.fileExtensions.joinToString(", "),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        modifier = Modifier
                            .clickable { /* Show extensions editor */ }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
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
                    Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(dev.gokanaz.kplayer.core.R.string.add_excluded_folder))
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
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.thumbnail_cache),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.thumbnailCacheSize,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    TextButton(onClick = { viewModel.clearThumbnailCache() }) {
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.clear))
                    }
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.video_cache),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.videoCacheSize,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    TextButton(onClick = { showClearCacheDialog = true }) {
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.clear))
                    }
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.auto_scan_triggers))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Settings,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_when_charging),
                    checked = uiState.scanWhenCharging,
                    onCheckedChange = viewModel::updateScanWhenCharging
                )
                
                SwitchPreference(
                    icon = Icons.Default.Info,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.scan_on_new_files),
                    checked = uiState.scanOnNewFiles,
                    onCheckedChange = viewModel::updateScanOnNewFiles
                )
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
                            Icons.Default.Delete,
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
                                text = "This will remove all media cache and scan data",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        
                        Button(
                            onClick = { /* Show confirmation dialog */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Clear")
                        }
                    }
                }
            }
        }
    }
    
    if (pathToRemove != null) {
        AlertDialog(
            onDismissRequest = { pathToRemove = null },
            title = { Text("Remove Storage Path") },
            text = { Text("Are you sure you want to remove this storage path? Videos from this folder will no longer be scanned.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeStoragePath(pathToRemove!!)
                        pathToRemove = null
                    }
                ) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pathToRemove = null }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text("Clear Video Cache") },
            text = { Text("This will clear all cached video data. Videos will need to be reloaded when played.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearVideoCache()
                        showClearCacheDialog = false
                    }
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StoragePathItem(
    path: StoragePath,
    onRemove: (StoragePath) -> Unit
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
                    text = path.path,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${path.videoCount} videos • ${path.totalSize}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Last scan: ${path.lastScan}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { onRemove(path) }) {
                Icon(Icons.Default.Clear, contentDescription = "Remove")
            }
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
}

@Composable
fun SwitchPreference(
    icon: ImageVector,
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
