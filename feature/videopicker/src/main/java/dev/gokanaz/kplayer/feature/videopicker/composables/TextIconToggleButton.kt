package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextIconToggleButton(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    text: String,
    icon: ImageVector,
    size: ToggleButtonSize = ToggleButtonSize.Medium,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected && enabled) {
            MaterialTheme.colorScheme.primary
        } else if (enabled) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "bg_color"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected && enabled) {
            MaterialTheme.colorScheme.onPrimary
        } else if (enabled) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "content_color"
    )

    val height by animateDpAsState(
        targetValue = size.height,
        animationSpec = tween(durationMillis = 200),
        label = "height"
    )

    val padding by animateDpAsState(
        targetValue = size.padding,
        animationSpec = tween(durationMillis = 200),
        label = "padding"
    )

    val iconSize by animateDpAsState(
        targetValue = size.iconSize,
        animationSpec = tween(durationMillis = 200),
        label = "icon_size"
    )

    val fontSize by animateDpAsState(
        targetValue = size.fontSize.value.dp,
        animationSpec = tween(durationMillis = 200),
        label = "font_size"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) size.elevation else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "elevation"
    )

    Surface(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(size.cornerRadius)),
        color = backgroundColor,
        contentColor = contentColor,
        shadowElevation = elevation,
        onClick = {
            if (enabled) {
                onSelectedChange(!selected)
            }
        },
        enabled = enabled,
        role = Role.Button,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = padding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(size.spacing)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )

            Text(
                text = text,
                fontSize = fontSize.sp,
                maxLines = 1
            )

            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    modifier = Modifier.size(iconSize * 0.8f)
                )
            }
        }
    }
}

@Composable
fun TextIconToggleButtonGroup(
    options: List<ToggleButtonOption>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    size: ToggleButtonSize = ToggleButtonSize.Medium,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            TextIconToggleButton(
                selected = selectedOption == option.id,
                onSelectedChange = { onOptionSelected(option.id) },
                text = option.text,
                icon = option.icon,
                size = size,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

enum class ToggleButtonSize(
    val height: Dp,
    val padding: Dp,
    val iconSize: Dp,
    val fontSize: TextUnit,
    val spacing: Dp,
    val cornerRadius: Dp,
    val elevation: Dp
) {
    Small(
        height = 32.dp,
        padding = 12.dp,
        iconSize = 16.dp,
        fontSize = 12.sp,
        spacing = 6.dp,
        cornerRadius = 16.dp,
        elevation = 2.dp
    ),

    Medium(
        height = 40.dp,
        padding = 16.dp,
        iconSize = 20.dp,
        fontSize = 14.sp,
        spacing = 8.dp,
        cornerRadius = 20.dp,
        elevation = 4.dp
    ),

    Large(
        height = 48.dp,
        padding = 20.dp,
        iconSize = 24.dp,
        fontSize = 16.sp,
        spacing = 10.dp,
        cornerRadius = 24.dp,
        elevation = 6.dp
    )
}

data class ToggleButtonOption(
    val id: String,
    val text: String,
    val icon: ImageVector
)

object ToggleButtons {
    val ViewMode = listOf(
        ToggleButtonOption("grid", "Grid", Icons.Default.ViewModule),
        ToggleButtonOption("list", "List", Icons.Default.ViewList)
    )

    val SortType = listOf(
        ToggleButtonOption("name", "Name", Icons.Default.SortByAlpha),
        ToggleButtonOption("date", "Date", Icons.Default.DateRange),
        ToggleButtonOption("size", "Size", Icons.Default.DataUsage)
    )

    val SelectionActions = listOf(
        ToggleButtonOption("select", "Select", Icons.Default.CheckBox),
        ToggleButtonOption("selectAll", "All", Icons.Default.SelectAll),
        ToggleButtonOption("clear", "Clear", Icons.Default.Clear)
    )

    val FilterChips = listOf(
        ToggleButtonOption("all", "All", Icons.Default.FilterList),
        ToggleButtonOption("today", "Today", Icons.Default.Today),
        ToggleButtonOption("week", "Week", Icons.Default.DateRange)
    )
}

@Composable
fun GridListToggleButton(
    isGrid: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextIconToggleButton(
            selected = !isGrid,
            onSelectedChange = { onToggle(false) },
            text = "List",
            icon = Icons.Default.ViewList,
            size = ToggleButtonSize.Small,
            modifier = Modifier.weight(1f)
        )

        TextIconToggleButton(
            selected = isGrid,
            onSelectedChange = { onToggle(true) },
            text = "Grid",
            icon = Icons.Default.ViewModule,
            size = ToggleButtonSize.Small,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SortOrderToggleButton(
    isAscending: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextIconToggleButton(
            selected = isAscending,
            onSelectedChange = { onToggle(true) },
            text = "Asc",
            icon = Icons.Default.ArrowUpward,
            size = ToggleButtonSize.Small,
            modifier = Modifier.weight(1f)
        )

        TextIconToggleButton(
            selected = !isAscending,
            onSelectedChange = { onToggle(false) },
            text = "Desc",
            icon = Icons.Default.ArrowDownward,
            size = ToggleButtonSize.Small,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SelectionActionButtons(
    onSelectAll: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextIconToggleButton(
            selected = false,
            onSelectedChange = { onSelectAll() },
            text = "Select all",
            icon = Icons.Default.SelectAll,
            size = ToggleButtonSize.Small,
            modifier = Modifier.weight(1f)
        )

        TextIconToggleButton(
            selected = false,
            onSelectedChange = { onClearAll() },
            text = "Clear",
            icon = Icons.Default.Clear,
            size = ToggleButtonSize.Small,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FilterChipToggle(
    filters: List<FilterChipItem>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter.id,
                onClick = { onFilterSelected(filter.id) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = filter.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = filter.text,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            )
        }
    }
}

data class FilterChipItem(
    val id: String,
    val text: String,
    val icon: ImageVector
)
