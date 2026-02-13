package dev.gokanaz.kplayer.feature.settings.screens.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import dev.gokanaz.kplayer.core.model.RepeatMode
import dev.gokanaz.kplayer.core.model.Resume
import dev.gokanaz.kplayer.core.model.VideoQuality
import dev.gokanaz.kplayer.core.model.ScreenOrientation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: PlayerPreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    var showQualityMenu by remember { mutableStateOf(false) }
    var showRepeatMenu by remember { mutableStateOf(false) }
    var showResumeMenu by remember { mutableStateOf(false) }
    var showOrientationMenu by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.player)) },
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
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.playback))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { showQualityMenu = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.default_quality),
                            fontSize = 16.sp
                        )
                        Text(
                            text = uiState.defaultQuality.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Box {
                        Text(
                            text = uiState.defaultQuality.name,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        DropdownMenu(
                            expanded = showQualityMenu,
                            onDismissRequest = { showQualityMenu = false }
                        ) {
                            VideoQuality.entries.forEach { quality ->
                                DropdownMenuItem(
                                    text = { Text(quality.name) },
                                    onClick = {
                                        viewModel.updateDefaultQuality(quality)
                                        showQualityMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.PlayCircle,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.auto_play_next),
                    checked = uiState.autoPlayNext,
                    onCheckedChange = viewModel::updateAutoPlayNext
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { showRepeatMenu = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Repeat, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.repeat_mode),
                            fontSize = 16.sp
                        )
                        Text(
                            text = uiState.repeatMode.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Box {
                        Text(
                            text = uiState.repeatMode.name,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        DropdownMenu(
                            expanded = showRepeatMenu,
                            onDismissRequest = { showRepeatMenu = false }
                        ) {
                            RepeatMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.name) },
                                    onClick = {
                                        viewModel.updateRepeatMode(mode)
                                        showRepeatMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.Shuffle,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.shuffle_mode),
                    checked = uiState.shuffleMode,
                    onCheckedChange = viewModel::updateShuffleMode
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { showResumeMenu = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.resume_playback),
                            fontSize = 16.sp
                        )
                        Text(
                            text = uiState.resumeMode.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Box {
                        Text(
                            text = uiState.resumeMode.name,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        DropdownMenu(
                            expanded = showResumeMenu,
                            onDismissRequest = { showResumeMenu = false }
                        ) {
                            Resume.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.name) },
                                    onClick = {
                                        viewModel.updateResumeMode(mode)
                                        showResumeMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(dev.gokanaz.kplayer.core.R.string.resume_threshold),
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    
                    Text(
                        text = "${uiState.resumeThreshold}s",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = uiState.resumeThreshold.toFloat(),
                    onValueChange = { viewModel.updateResumeThreshold(it.toInt()) },
                    valueRange = 5f..60f,
                    steps = 4,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.player_controls))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(dev.gokanaz.kplayer.core.R.string.show_controls_timeout),
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    
                    Text(
                        text = when (uiState.controlsTimeout) {
                            0 -> "Never"
                            else -> "${uiState.controlsTimeout}s"
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = uiState.controlsTimeout.toFloat(),
                    onValueChange = { viewModel.updateControlsTimeout(it.toInt()) },
                    valueRange = 2f..10f,
                    steps = 4,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                SwitchPreference(
                    icon = Icons.Default.Gesture,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.double_tap_to_seek),
                    checked = uiState.doubleTapToSeek,
                    onCheckedChange = viewModel::updateDoubleTapToSeek
                )
                
                if (uiState.doubleTapToSeek) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Seek duration:",
                            modifier = Modifier.weight(1f),
                            fontSize = 14.sp
                        )
                        
                        SeekChip(
                            text = "5s",
                            selected = uiState.seekDuration == 5,
                            onClick = { viewModel.updateSeekDuration(5) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SeekChip(
                            text = "10s",
                            selected = uiState.seekDuration == 10,
                            onClick = { viewModel.updateSeekDuration(10) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SeekChip(
                            text = "15s",
                            selected = uiState.seekDuration == 15,
                            onClick = { viewModel.updateSeekDuration(15) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SeekChip(
                            text = "30s",
                            selected = uiState.seekDuration == 30,
                            onClick = { viewModel.updateSeekDuration(30) }
                        )
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.VolumeUp,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.volume_gesture),
                    checked = uiState.volumeGesture,
                    onCheckedChange = viewModel::updateVolumeGesture
                )
                
                SwitchPreference(
                    icon = Icons.Default.Brightness6,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.brightness_gesture),
                    checked = uiState.brightnessGesture,
                    onCheckedChange = viewModel::updateBrightnessGesture
                )
                
                SwitchPreference(
                    icon = Icons.Default.Gesture,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.seek_gesture),
                    checked = uiState.seekGesture,
                    onCheckedChange = viewModel::updateSeekGesture
                )
                
                SwitchPreference(
                    icon = Icons.Default.ZoomIn,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.pinch_to_zoom),
                    checked = uiState.pinchToZoom,
                    onCheckedChange = viewModel::updatePinchToZoom
                )
                
                SwitchPreference(
                    icon = Icons.Default.Lock,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.lock_controls_fullscreen),
                    checked = uiState.lockControlsFullscreen,
                    onCheckedChange = viewModel::updateLockControlsFullscreen
                )
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.picture_in_picture))
            }
            
            item {
                if (!uiState.pipSupported) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "Picture-in-Picture is not supported on this device",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.PictureInPicture,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.enable_pip),
                    checked = uiState.pipEnabled && uiState.pipSupported,
                    onCheckedChange = viewModel::updatePipEnabled,
                    enabled = uiState.pipSupported
                )
                
                if (uiState.pipEnabled) {
                    SwitchPreference(
                        icon = Icons.Default.PictureInPicture,
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.auto_enter_pip),
                        checked = uiState.autoEnterPip,
                        onCheckedChange = viewModel::updateAutoEnterPip
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.pip_button_position),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Row {
                            listOf("Left", "Center", "Right").forEach { position ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = uiState.pipButtonPosition == position,
                                        onClick = { viewModel.updatePipButtonPosition(position) }
                                    )
                                    Text(position, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                    
                    SwitchPreference(
                        icon = Icons.Default.ControlCamera,
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.pip_custom_actions),
                        checked = uiState.pipCustomActions,
                        onCheckedChange = viewModel::updatePipCustomActions
                    )
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.background_play))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.PlayArrow,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.enable_background_play),
                    checked = uiState.backgroundPlayEnabled,
                    onCheckedChange = viewModel::updateBackgroundPlayEnabled
                )
                
                if (uiState.backgroundPlayEnabled) {
                    SwitchPreference(
                        icon = Icons.Default.Notifications,
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.keep_notification),
                        checked = uiState.keepNotification,
                        onCheckedChange = viewModel::updateKeepNotification
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.audio_focus_handling),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = uiState.audioFocusHandling,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { /* Show audio focus menu */ }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.screen))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { showOrientationMenu = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ScreenRotation, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.default_orientation),
                            fontSize = 16.sp
                        )
                        Text(
                            text = uiState.defaultOrientation.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Box {
                        Text(
                            text = uiState.defaultOrientation.name,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        DropdownMenu(
                            expanded = showOrientationMenu,
                            onDismissRequest = { showOrientationMenu = false }
                        ) {
                            ScreenOrientation.entries.forEach { orientation ->
                                DropdownMenuItem(
                                    text = { Text(orientation.name) },
                                    onClick = {
                                        viewModel.updateDefaultOrientation(orientation)
                                        showOrientationMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.Lock,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.lock_orientation_in_player),
                    checked = uiState.lockOrientationInPlayer,
                    onCheckedChange = viewModel::updateLockOrientationInPlayer
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(dev.gokanaz.kplayer.core.R.string.fullscreen_mode),
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )
                    
                    Row {
                        listOf("Immersive", "Edge-to-Edge", "Standard").forEach { mode ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = uiState.fullscreenMode == mode,
                                    onClick = { viewModel.updateFullscreenMode(mode) }
                                )
                                Text(mode.take(4), fontSize = 12.sp)
                            }
                        }
                    }
                }
                
                SwitchPreference(
                    icon = Icons.Default.Fullscreen,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.keep_screen_on),
                    checked = uiState.keepScreenOn,
                    onCheckedChange = viewModel::updateKeepScreenOn
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = viewModel::resetToDefaults,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reset to Defaults")
                    }
                    
                    Button(
                        onClick = viewModel::testPlayback,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Test Playback")
                    }
                }
            }
        }
    }
}

@Composable
fun SeekChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SwitchPreference(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null,
    enabled: Boolean = true
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
            onCheckedChange = onCheckedChange,
            enabled = enabled
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
