package dev.gokanaz.kplayer.feature.player.ui.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme
import dev.gokanaz.kplayer.feature.player.state.ControlsVisibilityState
import dev.gokanaz.kplayer.feature.player.state.MetadataState
import dev.gokanaz.kplayer.feature.player.state.PlaybackParametersState
import dev.gokanaz.kplayer.feature.player.state.TracksState
import dev.gokanaz.kplayer.feature.player.extensions.formatDuration
import dev.gokanaz.kplayer.feature.player.extensions.toIcon
import dev.gokanaz.kplayer.feature.player.R

@Composable
fun ControlsBottomView(
    playbackState: PlaybackParametersState,
    tracksState: TracksState,
    metadataState: MetadataState,
    controlsState: ControlsVisibilityState,
    isPlaying: Boolean,
    repeatMode: LoopMode,
    isShuffleEnabled: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Long) -> Unit,
    onRepeatModeChange: (LoopMode) -> Unit,
    onShuffleToggle: (Boolean) -> Unit,
    onSubtitleClick: () -> Unit,
    onAudioTrackClick: () -> Unit,
    onSpeedClick: () -> Unit,
    onFullscreenToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.Black.copy(alpha = 0.7f)
        ),
        startY = 0f,
        endY = 200f
    )
    
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    
    AnimatedVisibility(
        visible = controlsState.areControlsVisible && !controlsState.isPlayerLocked,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                PlayerSeekBar(
                    currentPosition = metadataState.currentPosition,
                    duration = metadataState.duration,
                    bufferedPosition = metadataState.bufferedPosition,
                    onSeek = onSeek,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        RepeatButton(
                            repeatMode = repeatMode,
                            onClick = { onRepeatModeChange(repeatMode.next()) }
                        )
                        
                        ShuffleButton(
                            isShuffleOn = isShuffleEnabled,
                            onClick = { onShuffleToggle(!isShuffleEnabled) }
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onPrevious,
                            enabled = true,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = NextIconPainter(NextIcon.SkipPrevious),
                                contentDescription = stringResource(id = R.string.player_previous),
                                tint = Color.White
                            )
                        }
                        
                        PlayPauseButton(
                            isPlaying = isPlaying,
                            onClick = onPlayPause,
                            modifier = Modifier.size(64.dp)
                        )
                        
                        IconButton(
                            onClick = onNext,
                            enabled = true,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = NextIconPainter(NextIcon.SkipNext),
                                contentDescription = stringResource(id = R.string.player_next),
                                tint = Color.White
                            )
                        }
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (tracksState.subtitleTracks.isNotEmpty()) {
                            IconButton(
                                onClick = onSubtitleClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = NextIconPainter(NextIcon.Subtitles),
                                    contentDescription = stringResource(id = R.string.player_subtitles),
                                    tint = if (tracksState.isSubtitleEnabled && tracksState.selectedSubtitleTrackIndex >= 0) {
                                        AppTheme.colorScheme.primary
                                    } else {
                                        Color.White.copy(alpha = 0.7f)
                                    }
                                )
                            }
                        }
                        
                        if (tracksState.audioTracks.size > 1) {
                            IconButton(
                                onClick = onAudioTrackClick,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = NextIconPainter(NextIcon.Audiotrack),
                                    contentDescription = stringResource(id = R.string.player_audio),
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        
                        IconButton(
                            onClick = onSpeedClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text(
                                text = playbackState.formatSpeedForDisplay(),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        
                        IconButton(
                            onClick = onFullscreenToggle,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = NextIconPainter(
                                    if (isLandscape) NextIcon.FullscreenExit else NextIcon.Fullscreen
                                ),
                                contentDescription = stringResource(
                                    id = if (isLandscape) R.string.player_exit_fullscreen else R.string.player_fullscreen
                                ),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerSeekBar(
    currentPosition: Long,
    duration: Long,
    bufferedPosition: Long,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember(currentPosition) {
        mutableStateOf(currentPosition.toFloat())
    }
    
    Column(
        modifier = modifier
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = { onSeek(sliderPosition.toLong()) },
            valueRange = 0f..duration.toFloat(),
            colors = SliderDefaults.colors(
                thumbColor = AppTheme.colorScheme.primary,
                activeTrackColor = AppTheme.colorScheme.primary,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentPosition.formatDuration(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier.alpha(0.9f)
            )
            
            Text(
                text = (-(duration - currentPosition)).formatDuration(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.medium)
    ) {
        Icon(
            painter = NextIconPainter(
                if (isPlaying) NextIcon.Pause else NextIcon.Play,
                filled = true
            ),
            contentDescription = stringResource(
                id = if (isPlaying) R.string.player_pause else R.string.player_play
            ),
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun RepeatButton(
    repeatMode: LoopMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            painter = NextIconPainter(repeatMode.toIcon()),
            contentDescription = stringResource(id = R.string.player_repeat),
            tint = when (repeatMode) {
                LoopMode.NONE -> Color.White.copy(alpha = 0.5f)
                else -> AppTheme.colorScheme.primary
            }
        )
    }
}

@Composable
fun ShuffleButton(
    isShuffleOn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            painter = NextIconPainter(NextIcon.Shuffle),
            contentDescription = stringResource(id = R.string.player_shuffle),
            tint = if (isShuffleOn) AppTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f)
        )
    }
}
