package dev.gokanaz.kplayer.core.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.ui.designsystem.NextIcon
import dev.gokanaz.kplayer.core.ui.designsystem.NextIconPainter
import dev.gokanaz.kplayer.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        modifier = modifier.shadow(elevation = 4.dp),
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colorScheme.surface,
            scrolledContainerColor = AppTheme.colorScheme.surface,
            navigationIconContentColor = AppTheme.colorScheme.onSurface,
            titleContentColor = AppTheme.colorScheme.onSurface,
            actionIconContentColor = AppTheme.colorScheme.onSurfaceVariant
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextLargeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    LargeTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = AppTheme.colorScheme.surface,
            scrolledContainerColor = AppTheme.colorScheme.surface,
            navigationIconContentColor = AppTheme.colorScheme.onSurface,
            titleContentColor = AppTheme.colorScheme.onSurface,
            actionIconContentColor = AppTheme.colorScheme.onSurfaceVariant
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NextSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit = {}
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp),
        placeholder = {
            Text(text = placeholder)
        },
        leadingIcon = leadingIcon ?: {
            Icon(
                painter = NextIconPainter(NextIcon.Search),
                contentDescription = "Search",
                tint = AppTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = trailingIcon ?: {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = NextIconPainter(NextIcon.Close),
                        contentDescription = "Clear",
                        tint = AppTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = AppTheme.colorScheme.surfaceVariant,
            dividerColor = AppTheme.colorScheme.outlineVariant,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = AppTheme.colorScheme.onSurface,
                unfocusedTextColor = AppTheme.colorScheme.onSurface,
                disabledTextColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                focusedContainerColor = AppTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = AppTheme.colorScheme.surfaceVariant,
                disabledContainerColor = AppTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = AppTheme.colorScheme.primary,
                unfocusedLeadingIconColor = AppTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                focusedTrailingIconColor = AppTheme.colorScheme.primary,
                unfocusedTrailingIconColor = AppTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                focusedLabelColor = AppTheme.colorScheme.primary,
                unfocusedLabelColor = AppTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                focusedPlaceholderColor = AppTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = AppTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = AppTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                cursorColor = AppTheme.colorScheme.primary,
                errorCursorColor = AppTheme.colorScheme.error,
                selectionColors = TextFieldDefaults.colors().selectionColors
            )
        ),
        content = content
    )
}
