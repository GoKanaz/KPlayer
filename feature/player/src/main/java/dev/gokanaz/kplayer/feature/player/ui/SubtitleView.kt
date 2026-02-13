package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.gokanaz.kplayer.feature.player.model.SubtitleItem
import dev.gokanaz.kplayer.feature.player.state.CuesState

@Composable
fun SubtitleView(
    cuesState: CuesState,
    modifier: Modifier = Modifier
) {
    if (!cuesState.isSubtitleEnabled || cuesState.activeCues.isEmpty()) {
        return
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 64.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 24.dp)
        ) {
            cuesState.activeCues.forEach { cue ->
                SubtitleCueView(
                    cue = cue,
                    cuesState = cuesState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SubtitleCueView(
    cue: SubtitleItem,
    cuesState: CuesState,
    modifier: Modifier = Modifier
) {
    val backgroundColor = Color(cuesState.bgColor)
    val textColor = Color(cuesState.fontColor)
    
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        cue.segments.forEach { segment ->
            Text(
                text = segment.text,
                color = segment.color?.let { Color(it) } ?: textColor,
                fontSize = cuesState.fontSize.sp,
                fontFamily = FontFamily(cuesState.fontFamily),
                fontWeight = if (segment.isBold || cuesState.isBold) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (segment.isItalic || cuesState.isItalic) androidx.compose.ui.text.font.FontStyle.Italic else androidx.compose.ui.text.font.FontStyle.Normal,
                textAlign = TextAlign.Center,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
