package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import dev.gokanaz.kplayer.feature.player.state.TrackInfo
import dev.gokanaz.kplayer.feature.player.state.TracksState
import dev.gokanaz.kplayer.feature.player.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioTrackSelectorView(
    tracksState: TracksState,
    onTrackSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val scope = rememberCoroutineScope()
    
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
                text = stringResource(id = R.string.player_audio_tracks),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn {
                items(tracksState.audioTracks) { track ->
                    RadioButtonRow(
                        text = getTrackDisplayName(track),
                        selected = tracksState.audioTracks.indexOf(track) == tracksState.selectedAudioTrackIndex,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                onTrackSelected(tracksState.audioTracks.indexOf(track))
                            }
                        },
                        description = buildString {
                            if (track.bitrate > 0) {
                                append("${track.bitrate / 1000} kbps")
                            }
                            if (track.channelCount > 0) {
                                if (isNotEmpty()) append(" • ")
                                append("${track.channelCount} ch")
                            }
                            if (track.sampleRate > 0) {
                                if (isNotEmpty()) append(" • ")
                                append("${track.sampleRate / 1000} kHz")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SubtitleSelectorView(
    tracksState: TracksState,
    onTrackSelected: (Int) -> Unit,
    onAddExternalClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val scope = rememberCoroutineScope()
    
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
                text = stringResource(id = R.string.player_subtitles),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn {
                item {
                    RadioButtonRow(
                        text = stringResource(id = R.string.player_subtitles_off),
                        selected = tracksState.selectedSubtitleTrackIndex == -1 || !tracksState.isSubtitleEnabled,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                onTrackSelected(-1)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                items(tracksState.subtitleTracks) { track ->
                    RadioButtonRow(
                        text = getTrackDisplayName(track),
                        selected = tracksState.subtitleTracks.indexOf(track) == tracksState.selectedSubtitleTrackIndex,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                onTrackSelected(tracksState.subtitleTracks.indexOf(track))
                            }
                        },
                        description = buildString {
                            if (track.isForced) {
                                append(stringResource(id = R.string.player_subtitle_forced))
                            }
                            if (track.isDefault) {
                                if (isNotEmpty()) append(" • ")
                                append(stringResource(id = R.string.player_subtitle_default))
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = onAddExternalClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = NextIconPainter(NextIcon.Add),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(id = R.string.player_add_external_subtitle))
                    }
                }
            }
        }
    }
}

private fun getTrackDisplayName(track: TrackInfo): String {
    return when {
        track.label.isNotEmpty() -> track.label
        track.language.isNotEmpty() -> Locale(track.language).displayLanguage ?: track.language
        else -> "Unknown"
    }
}
