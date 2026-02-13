package dev.gokanaz.kplayer.feature.settings.screens.audio

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.gokanaz.kplayer.core.ui.R

enum class EqualizerPreset {
    NORMAL, CLASSICAL, DANCE, FLAT, JAZZ, POP, ROCK, CUSTOM
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: AudioPreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    var expandedPreset by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.audio)) },
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
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.volume))
            }
            
            item {
                VolumeSlider(
                    volume = uiState.volume,
                    onVolumeChange = viewModel::updateVolume
                )
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Headset, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.volume_gesture_sensitivity))
                    }
                    
                    Text(
                        text = when (uiState.volumeGestureSensitivity) {
                            0 -> "Low"
                            1 -> "Medium"
                            else -> "High"
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = uiState.volumeGestureSensitivity.toFloat(),
                    onValueChange = { viewModel.updateVolumeGestureSensitivity(it.toInt()) },
                    valueRange = 0f..2f,
                    steps = 2,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.MusicNote,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.mute_on_headset_unplug),
                    checked = uiState.muteOnHeadsetUnplug,
                    onCheckedChange = viewModel::updateMuteOnHeadsetUnplug
                )
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.SettingsInputComponent,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.audio_ducking),
                    checked = uiState.audioDucking,
                    onCheckedChange = viewModel::updateAudioDucking
                )
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.equalizer))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Equalizer,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.equalizer_enabled),
                    checked = uiState.equalizerEnabled,
                    onCheckedChange = viewModel::updateEqualizerEnabled
                )
            }
            
            if (uiState.equalizerEnabled) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { expandedPreset = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.equalizer_preset),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.equalizerPreset.name,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Text(
                            text = uiState.equalizerPreset.name,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        DropdownMenu(
                            expanded = expandedPreset,
                            onDismissRequest = { expandedPreset = false }
                        ) {
                            EqualizerPreset.entries.forEach { preset ->
                                DropdownMenuItem(
                                    text = { Text(preset.name) },
                                    onClick = {
                                        viewModel.updateEqualizerPreset(preset)
                                        expandedPreset = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                if (uiState.equalizerPreset == EqualizerPreset.CUSTOM) {
                    item {
                        EqualizerBands(
                            bands = uiState.equalizerBands,
                            onBandChange = viewModel::updateEqualizerBand
                        )
                    }
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.audio_track))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.preferred_audio_language))
                    }
                    
                    Text(
                        text = uiState.preferredAudioLanguage,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { /* Show language picker */ }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                
                SwitchPreference(
                    icon = Icons.Default.Speaker,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.prefer_stereo),
                    checked = uiState.preferStereo,
                    onCheckedChange = viewModel::updatePreferStereo
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.AudioFile, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.default_audio_track))
                    }
                    
                    Row {
                        AudioTrackChip(
                            text = "Default",
                            selected = uiState.defaultAudioTrack == 0,
                            onClick = { viewModel.updateDefaultAudioTrack(0) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AudioTrackChip(
                            text = "First",
                            selected = uiState.defaultAudioTrack == 1,
                            onClick = { viewModel.updateDefaultAudioTrack(1) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AudioTrackChip(
                            text = "Last",
                            selected = uiState.defaultAudioTrack == 2,
                            onClick = { viewModel.updateDefaultAudioTrack(2) }
                        )
                    }
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.advanced))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Speaker, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.audio_output))
                    }
                    
                    Text(
                        text = uiState.audioOutput,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { /* Show output picker */ }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.audio_buffer_size))
                    }
                    
                    Text(
                        text = "${uiState.audioBufferSize} ms",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = uiState.audioBufferSize.toFloat(),
                    onValueChange = { viewModel.updateAudioBufferSize(it.toInt()) },
                    valueRange = 50f..500f,
                    steps = 10,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                SwitchPreference(
                    icon = Icons.Default.SettingsInputComponent,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.audio_offload),
                    checked = uiState.audioOffload,
                    onCheckedChange = viewModel::updateAudioOffload
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = viewModel::testAudioOutput
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Headset, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.test_audio))
                    }
                }
            }
        }
    }
}

@Composable
fun VolumeSlider(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(dev.gokanaz.kplayer.core.R.string.default_volume))
            }
            
            Text(
                text = "${(volume * 100).toInt()}%",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Slider(
            value = volume,
            onValueChange = onVolumeChange,
            valueRange = 0f..1f,
            steps = 20
        )
    }
}

@Composable
fun EqualizerBands(
    bands: List<Int>,
    onBandChange: (Int, Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(dev.gokanaz.kplayer.core.R.string.equalizer_bands),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            bands.forEachIndexed { index, value ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 12.sp,
                        modifier = Modifier.width(24.dp)
                    )
                    
                    Slider(
                        value = value.toFloat(),
                        onValueChange = { onBandChange(index, it.toInt()) },
                        valueRange = -12f..12f,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Text(
                        text = "$value dB",
                        fontSize = 12.sp,
                        modifier = Modifier.width(48.dp)
                    )
                }
            }
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
fun AudioTrackChip(
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
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
