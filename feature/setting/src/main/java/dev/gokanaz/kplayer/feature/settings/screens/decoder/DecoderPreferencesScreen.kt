package dev.gokanaz.kplayer.feature.settings.screens.decoder

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
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VideoLibrary
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

enum class DecoderPriority {
    HARDWARE, SOFTWARE, AUTO
}

data class CodecInfo(
    val name: String,
    val description: String,
    val hardwareSupported: Boolean,
    val softwareSupported: Boolean,
    val isEnabled: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecoderPreferencesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHardware: () -> Unit,
    onNavigateToSoftware: () -> Unit,
    onNavigateToCodecPriority: () -> Unit,
    onNavigateToAdvanced: () -> Unit,
    onNavigateToTestPlayback: () -> Unit,
    viewModel: DecoderPreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.decoder)) },
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
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.decoder_priority))
            }
            
            item {
                DecoderPrioritySelector(
                    selectedPriority = uiState.decoderPriority,
                    onPrioritySelected = viewModel::updateDecoderPriority
                )
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = when (uiState.decoderPriority) {
                            DecoderPriority.HARDWARE -> "Hardware decoding uses dedicated hardware for better performance and battery life."
                            DecoderPriority.SOFTWARE -> "Software decoding uses CPU, more compatible but may consume more battery."
                            DecoderPriority.AUTO -> "Automatically choose best decoder based on device capabilities and video format."
                        },
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.codec_settings))
            }
            
            item {
                CodecList(
                    codecs = uiState.codecs,
                    onCodecToggle = viewModel::toggleCodec
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
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.preferred_codec))
                    }
                    
                    Text(
                        text = uiState.preferredCodec,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onNavigateToCodecPriority() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.advanced_decoder))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.max_resolution))
                    }
                    
                    Text(
                        text = uiState.maxResolution,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { /* Show resolution picker */ }
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
                        Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.max_frame_rate))
                    }
                    
                    Text(
                        text = "${uiState.maxFrameRate} fps",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = uiState.maxFrameRate.toFloat(),
                    onValueChange = { viewModel.updateMaxFrameRate(it.toInt()) },
                    valueRange = 30f..120f,
                    steps = 4,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                SwitchPreference(
                    icon = Icons.Default.SettingsInputComponent,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.enable_frame_skipping),
                    checked = uiState.frameSkipping,
                    onCheckedChange = viewModel::updateFrameSkipping
                )
                
                SwitchPreference(
                    icon = Icons.Default.Memory,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.enable_tunneling),
                    checked = uiState.tunnelingEnabled,
                    onCheckedChange = viewModel::updateTunnelingEnabled,
                    subtitle = "Experimental feature"
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.audio_decoder_priority))
                    }
                    
                    Text(
                        text = uiState.audioDecoderPriority,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { /* Show audio decoder picker */ }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.debug))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Info,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.show_decoder_info),
                    checked = uiState.showDecoderInfo,
                    onCheckedChange = viewModel::updateShowDecoderInfo
                )
            }
            
            if (uiState.showDecoderInfo) {
                item {
                    DecoderStatistics(
                        stats = uiState.decoderStats
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNavigateToTestPlayback,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.BugReport, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.test_video))
                    }
                    
                    OutlinedButton(
                        onClick = viewModel::clearDecoderCache,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Memory, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.clear_cache))
                    }
                }
            }
        }
    }
}

@Composable
fun DecoderPrioritySelector(
    selectedPriority: DecoderPriority,
    onPrioritySelected: (DecoderPriority) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PriorityOption(
            priority = DecoderPriority.HARDWARE,
            title = "Hardware",
            selected = selectedPriority == DecoderPriority.HARDWARE,
            onClick = { onPrioritySelected(DecoderPriority.HARDWARE) }
        )
        
        PriorityOption(
            priority = DecoderPriority.SOFTWARE,
            title = "Software",
            selected = selectedPriority == DecoderPriority.SOFTWARE,
            onClick = { onPrioritySelected(DecoderPriority.SOFTWARE) }
        )
        
        PriorityOption(
            priority = DecoderPriority.AUTO,
            title = "Auto",
            selected = selectedPriority == DecoderPriority.AUTO,
            onClick = { onPrioritySelected(DecoderPriority.AUTO) }
        )
    }
}

@Composable
fun PriorityOption(
    priority: DecoderPriority,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun CodecList(
    codecs: List<CodecInfo>,
    onCodecToggle: (String, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        codecs.forEach { codec ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = codec.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = codec.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (codec.hardwareSupported) {
                            Chip(text = "HW", color = MaterialTheme.colorScheme.primary)
                        }
                        if (codec.softwareSupported) {
                            Chip(text = "SW", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
                
                Switch(
                    checked = codec.isEnabled,
                    onCheckedChange = { onCodecToggle(codec.name, it) }
                )
            }
            
            HorizontalDivider()
        }
    }
}

@Composable
fun Chip(
    text: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f),
            contentColor = color
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp
        )
    }
}

@Composable
fun DecoderStatistics(
    stats: Map<String, String>
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(dev.gokanaz.kplayer.core.R.string.decoder_statistics),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            stats.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = key,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
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
