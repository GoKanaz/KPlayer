package dev.gokanaz.kplayer.feature.player.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import dev.gokanaz.kplayer.core.model.player.VideoContentScale
import dev.gokanaz.kplayer.feature.player.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoContentScaleSelectorView(
    currentScale: VideoContentScale,
    onScaleSelected: (VideoContentScale) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val scope = rememberCoroutineScope()
    val scaleOptions = listOf(
        VideoContentScale.FIT to R.string.scale_fit,
        VideoContentScale.FILL to R.string.scale_fill,
        VideoContentScale.ZOOM to R.string.scale_zoom,
        VideoContentScale.STRETCH to R.string.scale_stretch,
        VideoContentScale.CROP to R.string.scale_crop,
        VideoContentScale.ORIGINAL to R.string.scale_original
    )
    
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
                text = stringResource(id = R.string.player_video_scale),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(400.dp)
            ) {
                items(scaleOptions) { (scale, labelResId) ->
                    ScaleOptionCard(
                        scale = scale,
                        label = stringResource(id = labelResId),
                        isSelected = scale == currentScale,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                onScaleSelected(scale)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ScaleOptionCard(
    scale: VideoContentScale,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Black.copy(alpha = 0.1f), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                ScalePreviewIcon(scale)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun ScalePreviewIcon(scale: VideoContentScale) {
    val painter = when (scale) {
        VideoContentScale.FIT -> painterResource(id = R.drawable.ic_scale_fit)
        VideoContentScale.FILL -> painterResource(id = R.drawable.ic_scale_fill)
        VideoContentScale.ZOOM -> painterResource(id = R.drawable.ic_scale_zoom)
        VideoContentScale.STRETCH -> painterResource(id = R.drawable.ic_scale_stretch)
        VideoContentScale.CROP -> painterResource(id = R.drawable.ic_scale_crop)
        VideoContentScale.ORIGINAL -> painterResource(id = R.drawable.ic_scale_original)
    }
    
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(48.dp)
    )
}
