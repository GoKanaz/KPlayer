package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun VerticalProgressView(
    progress: Float,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    showPercentage: Boolean = true,
    discreteSteps: Int = 0
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "vertical_progress"
    )
    
    Box(
        modifier = modifier,
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
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(8.dp)
                ) {
                    val barHeight = size.height
                    val progressHeight = barHeight * animatedProgress
                    
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.3f),
                        size = Size(size.width, barHeight),
                        cornerRadius = CornerRadius(4f)
                    )
                    
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AppTheme.colorScheme.primary,
                                AppTheme.colorScheme.primary.copy(alpha = 0.7f)
                            )
                        ),
                        size = Size(size.width, progressHeight),
                        cornerRadius = CornerRadius(4f)
                    )
                    
                    if (discreteSteps > 0) {
                        val stepHeight = barHeight / discreteSteps
                        for (i in 0..discreteSteps) {
                            drawLine(
                                color = Color.White,
                                start = Offset(0f, i * stepHeight),
                                end = Offset(size.width, i * stepHeight),
                                strokeWidth = 1f
                            )
                        }
                    }
                }
            }
            
            if (showPercentage) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun VolumeVerticalProgressView(
    volume: Float,
    isMuted: Boolean,
    modifier: Modifier = Modifier
) {
    VerticalProgressView(
        progress = if (isMuted) 0f else volume,
        icon = if (isMuted) NextIcon.VolumeOff.outlined else NextIcon.VolumeUp.outlined,
        discreteSteps = 15,
        modifier = modifier
    )
}

@Composable
fun BrightnessVerticalProgressView(
    brightness: Float,
    modifier: Modifier = Modifier
) {
    VerticalProgressView(
        progress = brightness,
        icon = NextIcon.Brightness7.outlined,
        modifier = modifier
    )
}
