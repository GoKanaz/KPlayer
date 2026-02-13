package dev.gokanaz.kplayer.feature.settings.screens.appearance

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.gokanaz.kplayer.core.model.DarkMode
import dev.gokanaz.kplayer.core.model.Font
import dev.gokanaz.kplayer.core.model.ThemeColor
import dev.gokanaz.kplayer.core.model.ViewType
import dev.gokanaz.kplayer.core.ui.R
import dev.gokanaz.kplayer.feature.settings.extensions.toColor
import dev.gokanaz.kplayer.feature.settings.extensions.toDisplayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearancePreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: AppearancePreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.appearance)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::resetToDefaults) {
                        Icon(Icons.Default.ColorLens, contentDescription = "Reset")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.theme))
            }
            
            item {
                DarkModeSelector(
                    selectedMode = uiState.darkMode,
                    onModeSelected = viewModel::updateDarkMode
                )
            }
            
            item {
                ThemeColorGrid(
                    selectedColor = uiState.themeColor,
                    onColorSelected = viewModel::updateThemeColor
                )
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ColorLens, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.dynamic_color))
                    }
                    Switch(
                        checked = uiState.dynamicColor,
                        onCheckedChange = viewModel::updateDynamicColor
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                FontScaleSlider(
                    scale = uiState.fontScale,
                    onScaleChange = viewModel::updateFontScale
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.language))
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Show language dialog */ }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.app_language),
                            fontSize = 16.sp
                        )
                        Text(
                            text = uiState.appLanguage.toDisplayName(LocalContext.current),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Text(
                        text = "Auto",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.view))
            }
            
            item {
                ViewTypeSelector(
                    selectedType = uiState.viewType,
                    onTypeSelected = viewModel::updateViewType
                )
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.sort_type))
                    }
                    
                    Row {
                        SortChip(
                            text = "Name",
                            selected = uiState.sortType == SortType.Name,
                            onClick = { viewModel.updateSortType(SortType.Name) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SortChip(
                            text = "Date",
                            selected = uiState.sortType == SortType.Date,
                            onClick = { viewModel.updateSortType(SortType.Date) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SortChip(
                            text = "Size",
                            selected = uiState.sortType == SortType.Size,
                            onClick = { viewModel.updateSortType(SortType.Size) }
                        )
                    }
                }
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(stringResource(dev.gokanaz.kplayer.core.R.string.sort_order))
                    }
                    
                    Row {
                        SortChip(
                            text = "Asc",
                            selected = uiState.sortOrder == SortOrder.Ascending,
                            onClick = { viewModel.updateSortOrder(SortOrder.Ascending) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        SortChip(
                            text = "Desc",
                            selected = uiState.sortOrder == SortOrder.Descending,
                            onClick = { viewModel.updateSortOrder(SortOrder.Descending) }
                        )
                    }
                }
            }
            
            item {
                GridColumnsSlider(
                    columns = uiState.gridColumns,
                    onColumnsChange = viewModel::updateGridColumns
                )
            }
            
            item {
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.preview))
            }
            
            item {
                ThemePreview(
                    darkMode = uiState.darkMode,
                    themeColor = uiState.themeColor,
                    fontScale = uiState.fontScale
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun DarkModeSelector(
    selectedMode: DarkMode,
    onModeSelected: (DarkMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DarkModeOption(
            mode = DarkMode.SYSTEM,
            icon = Icons.Default.BrightnessAuto,
            selected = selectedMode == DarkMode.SYSTEM,
            onClick = { onModeSelected(DarkMode.SYSTEM) }
        )
        
        DarkModeOption(
            mode = DarkMode.LIGHT,
            icon = Icons.Default.Brightness5,
            selected = selectedMode == DarkMode.LIGHT,
            onClick = { onModeSelected(DarkMode.LIGHT) }
        )
        
        DarkModeOption(
            mode = DarkMode.DARK,
            icon = Icons.Default.Brightness2,
            selected = selectedMode == DarkMode.DARK,
            onClick = { onModeSelected(DarkMode.DARK) }
        )
    }
}

@Composable
fun DarkModeOption(
    mode: DarkMode,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Text(
                text = mode.toDisplayName(LocalContext.current),
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ThemeColorGrid(
    selectedColor: ThemeColor,
    onColorSelected: (ThemeColor) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ColorLens, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(stringResource(dev.gokanaz.kplayer.core.R.string.theme_color))
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(120.dp)
        ) {
            items(ThemeColor.entries) { color ->
                ColorSwatch(
                    color = color,
                    isSelected = selectedColor == color,
                    onClick = { onColorSelected(color) }
                )
            }
        }
    }
}

@Composable
fun ColorSwatch(
    color: ThemeColor,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .background(color.toColor())
            .border(
                width = if (isSelected) 3.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
    )
}

@Composable
fun FontScaleSlider(
    scale: Float,
    onScaleChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TextFields, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(dev.gokanaz.kplayer.core.R.string.font_scale))
            }
            
            Text(
                text = "${(scale * 100).toInt()}%",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Slider(
            value = scale,
            onValueChange = onScaleChange,
            valueRange = 0.5f..1.5f,
            steps = 10
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("50%", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("100%", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("150%", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "Preview text with current font scale",
                modifier = Modifier.padding(16.dp),
                fontSize = (16 * scale).sp
            )
        }
    }
}

@Composable
fun ViewTypeSelector(
    selectedType: ViewType,
    onTypeSelected: (ViewType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ViewTypeOption(
            type = ViewType.GRID,
            icon = Icons.Default.GridView,
            selected = selectedType == ViewType.GRID,
            onClick = { onTypeSelected(ViewType.GRID) }
        )
        
        ViewTypeOption(
            type = ViewType.LIST,
            icon = Icons.Default.List,
            selected = selectedType == ViewType.LIST,
            onClick = { onTypeSelected(ViewType.LIST) }
        )
        
        ViewTypeOption(
            type = ViewType.CARD,
            icon = Icons.Default.ViewModule,
            selected = selectedType == ViewType.CARD,
            onClick = { onTypeSelected(ViewType.CARD) }
        )
    }
}

@Composable
fun ViewTypeOption(
    type: ViewType,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Text(
                text = type.name,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun GridColumnsSlider(
    columns: Int,
    onColumnsChange: (Int) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ViewStream, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(stringResource(dev.gokanaz.kplayer.core.R.string.grid_columns))
            }
            
            Text(
                text = "$columns columns",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Slider(
            value = columns.toFloat(),
            onValueChange = { onColumnsChange(it.toInt()) },
            valueRange = 2f..5f,
            steps = 3
        )
    }
}

@Composable
fun ThemePreview(
    darkMode: DarkMode,
    themeColor: ThemeColor,
    fontScale: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Sample Video Title",
                fontSize = (18 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = themeColor.toColor()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Duration: 2:30 • 1080p • 150 MB",
                fontSize = (14 * fontScale).sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                )
            }
        }
    }
}

@Composable
fun SortChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
