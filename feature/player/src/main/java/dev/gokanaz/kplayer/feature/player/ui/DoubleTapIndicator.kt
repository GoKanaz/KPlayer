package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@Composable
fun DoubleTapIndicator(
    isVisible: Boolean,
    isForward: Boolean,
    seekSeconds: Int = 10,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 400f
        ), label = "double_tap_scale"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(100)) + scaleIn(initialScale = 0.5f, animationSpec = tween(200)),
        exit = fadeOut(animationSpec = tween(150)) + scaleOut(targetScale = 0.5f, animationSpec = tween(150)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = NextIconPainter(
                        if (isForward) NextIcon.SkipNext else NextIcon.SkipPrevious
                    ),
                    contentDescription = null,
                    tint = if (isForward) AppTheme.colorScheme.primary else AppTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                
                Text(
                    text = "${seekSeconds}s",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun MultiTapIndicator(
    tapCount: Int,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (tapCount > 0) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 400f
        ), label = "multi_tap_scale"
    )
    
    AnimatedVisibility(
        visible = tapCount > 0,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$tapCount×",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}
