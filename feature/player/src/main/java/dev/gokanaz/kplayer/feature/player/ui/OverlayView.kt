package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gokanaz.kplayer.core.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun OverlayView(
    isVisible: Boolean,
    icon: ImageVector? = null,
    value: Float,
    valueFormat: (Float) -> String = { "${(it * 100).toInt()}%" },
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                VerticalProgressView(
                    progress = value,
                    modifier = Modifier
                        .height(80.dp)
                        .width(8.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = valueFormat(value),
                    color = Color.White,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun SeekOverlayView(
    isVisible: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalDuration > 0) {
        currentPosition.toFloat() / totalDuration.toFloat()
    } else 0f
    
    OverlayView(
        isVisible = isVisible,
        icon = androidx.compose.material.icons.Icons.Default.SkipNext,
        value = percentage,
        valueFormat = { currentPosition.formatDuration() },
        modifier = modifier
    )
}
