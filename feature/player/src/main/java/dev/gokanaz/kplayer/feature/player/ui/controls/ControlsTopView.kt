package dev.gokanaz.kplayer.feature.player.ui.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme
import dev.gokanaz.kplayer.feature.player.state.ControlsVisibilityState
import dev.gokanaz.kplayer.feature.player.state.MetadataState
import dev.gokanaz.kplayer.feature.player.state.PictureInPictureState
import dev.gokanaz.kplayer.feature.player.R

@Composable
fun ControlsTopView(
    metadataState: MetadataState,
    controlsState: ControlsVisibilityState,
    pipState: PictureInPictureState,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    onCastClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.7f),
            Color.Transparent
        ),
        startY = 0f,
        endY = 200f
    )
    
    AnimatedVisibility(
        visible = controlsState.areControlsVisible && !controlsState.isPlayerLocked,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = NextIconPainter(NextIcon.ArrowBack),
                            contentDescription = stringResource(id = R.string.player_back),
                            tint = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = metadataState.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.alpha(0.95f)
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (pipState.isPipAvailable && pipState.isPipEnabledInSettings) {
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = NextIconPainter(NextIcon.PictureInPicture),
                                contentDescription = stringResource(id = R.string.player_pip),
                                tint = Color.White
                            )
                        }
                    }
                    
                    onCastClick?.let {
                        IconButton(
                            onClick = it,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = NextIconPainter(NextIcon.Cast),
                                contentDescription = stringResource(id = R.string.player_cast),
                                tint = Color.White
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = NextIconPainter(NextIcon.MoreVert),
                            contentDescription = stringResource(id = R.string.player_settings),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
