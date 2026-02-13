package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import dev.gokanaz.kplayer.feature.player.state.PlaybackParametersState
import dev.gokanaz.kplayer.feature.player.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackSpeedSelectorView(
    playbackState: PlaybackParametersState,
    onSpeedSelected: (Float) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val scope = rememberCoroutineScope()
    var currentSpeed by remember(playbackState.speed) {
        mutableStateOf(playbackState.speed)
    }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.player_playback_speed),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            SpeedPresetRow(
                presets = playbackState.availableSpeedPresets,
                selectedSpeed = currentSpeed,
                onSpeedSelected = { currentSpeed = it },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.player_custom),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.width(80.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Slider(
                    value = currentSpeed,
                    onValueChange = { currentSpeed = it },
                    valueRange = 0.25f..2.0f,
                    steps = 7,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "${currentSpeed.formatSpeed()}x",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.width(60.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(
                    onClick = {
                        currentSpeed = 1.0f
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.player_reset))
                }
                
                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            onSpeedSelected(currentSpeed)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(id = R.string.player_apply))
                }
            }
        }
    }
}

@Composable
fun SpeedPresetRow(
    presets: List<Float>,
    selectedSpeed: Float,
    onSpeedSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        presets.forEach { speed ->
            SpeedChip(
                speed = speed,
                isSelected = speed == selectedSpeed,
                onClick = { onSpeedSelected(speed) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SpeedChip(
    speed: Float,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = speed.formatSpeed(),
                style = MaterialTheme.typography.labelLarge
            )
        },
        modifier = modifier
    )
}

private fun Float.formatSpeed(): String {
    return if (this % 1.0f == 0f) {
        "${toInt()}x"
    } else {
        "${this}x"
    }
}
