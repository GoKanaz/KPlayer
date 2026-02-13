package dev.gokanaz.kplayer.feature.settings.screens.subtitle

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Shadow
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.gokanaz.kplayer.core.model.Font
import dev.gokanaz.kplayer.feature.settings.extensions.toDisplayName
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitlePreferencesScreen(
    onNavigateBack: () -> Unit,
    viewModel: SubtitlePreferencesViewModel = viewModel()
) {
    val uiState by remember { viewModel.uiState }
    var showLanguageMenu by remember { mutableStateOf(false) }
    var showFontMenu by remember { mutableStateOf(false) }
    var showEncodingMenu by remember { mutableStateOf(false) }
    var showFormatMenu by remember { mutableStateOf(false) }
    var previewText by remember { mutableStateOf("The quick brown fox jumps over the lazy dog") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(dev.gokanaz.kplayer.core.R.string.subtitles)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.general))
            }
            
            item {
                SwitchPreference(
                    icon = Icons.Default.Subtitles,
                    title = stringResource(dev.gokanaz.kplayer.core.R.string.enable_subtitles),
                    checked = uiState.subtitlesEnabled,
                    onCheckedChange = viewModel::updateSubtitlesEnabled
                )
            }
            
            if (uiState.subtitlesEnabled) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { showLanguageMenu = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.default_subtitle_language),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.subtitleLanguage,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Box {
                            Text(
                                text = uiState.subtitleLanguage,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            DropdownMenu(
                                expanded = showLanguageMenu,
                                onDismissRequest = { showLanguageMenu = false }
                            ) {
                                listOf("English", "Indonesian", "Japanese", "Korean", "Chinese", "French", "German", "Spanish", "Auto").forEach { language ->
                                    DropdownMenuItem(
                                        text = { Text(language) },
                                        onClick = {
                                            viewModel.updateSubtitleLanguage(language)
                                            showLanguageMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    SwitchPreference(
                        icon = Icons.Default.Translate,
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.download_subtitles_auto),
                        checked = uiState.autoDownloadSubtitles,
                        onCheckedChange = viewModel::updateAutoDownloadSubtitles
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.subtitle_delay),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = "${uiState.subtitleDelay}s",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = uiState.subtitleDelay.toFloat(),
                        onValueChange = { viewModel.updateSubtitleDelay(it.toInt()) },
                        valueRange = -5f..5f,
                        steps = 10,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            
            if (uiState.subtitlesEnabled) {
                item {
                    PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.appearance))
                }
                
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { showFontMenu = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.FontDownload, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.font_family),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.fontFamily.name,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Box {
                            Text(
                                text = uiState.fontFamily.name,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            DropdownMenu(
                                expanded = showFontMenu,
                                onDismissRequest = { showFontMenu = false }
                            ) {
                                Font.entries.forEach { font ->
                                    DropdownMenuItem(
                                        text = { Text(font.name) },
                                        onClick = {
                                            viewModel.updateFontFamily(font)
                                            showFontMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.FormatSize, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.font_size),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = when (uiState.fontSize) {
                                0 -> "Small"
                                1 -> "Normal"
                                2 -> "Large"
                                else -> "X-Large"
                            },
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = uiState.fontSize.toFloat(),
                        onValueChange = { viewModel.updateFontSize(it.toInt()) },
                        valueRange = 0f..3f,
                        steps = 3,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
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
                            Text(stringResource(dev.gokanaz.kplayer.core.R.string.font_color))
                        }
                        
                        ColorPickerButton(
                            color = uiState.fontColor,
                            onColorSelected = viewModel::updateFontColor
                        )
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FormatBold, contentDescription = null, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(dev.gokanaz.kplayer.core.R.string.bold_text))
                        }
                        
                        Switch(
                            checked = uiState.boldText,
                            onCheckedChange = viewModel::updateBoldText
                        )
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FormatItalic, contentDescription = null, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(dev.gokanaz.kplayer.core.R.string.italic_text))
                        }
                        
                        Switch(
                            checked = uiState.italicText,
                            onCheckedChange = viewModel::updateItalicText
                        )
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shadow, contentDescription = null, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(stringResource(dev.gokanaz.kplayer.core.R.string.text_shadow))
                        }
                        
                        Switch(
                            checked = uiState.textShadow,
                            onCheckedChange = viewModel::updateTextShadow
                        )
                    }
                    
                    if (uiState.textShadow) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 40.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.shadow_opacity),
                                modifier = Modifier.weight(1f),
                                fontSize = 14.sp
                            )
                            
                            Text(
                                text = "${(uiState.shadowOpacity * 100).toInt()}%",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Slider(
                            value = uiState.shadowOpacity,
                            onValueChange = viewModel::updateShadowOpacity,
                            valueRange = 0.1f..1f,
                            modifier = Modifier.padding(start = 40.dp, end = 16.dp)
                        )
                    }
                    
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
                            Text(stringResource(dev.gokanaz.kplayer.core.R.string.background_color))
                        }
                        
                        ColorPickerButton(
                            color = uiState.backgroundColor,
                            onColorSelected = viewModel::updateBackgroundColor
                        )
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.background_opacity),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = "${(uiState.backgroundOpacity * 100).toInt()}%",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = uiState.backgroundOpacity,
                        onValueChange = viewModel::updateBackgroundOpacity,
                        valueRange = 0f..1f,
                        steps = 10,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    SwitchPreference(
                        icon = Icons.Default.Build,
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.background_blur),
                        checked = uiState.backgroundBlur,
                        onCheckedChange = viewModel::updateBackgroundBlur
                    )
                }
                
                item {
                    PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.position))
                }
                
                item {
                    Text(
                        text = stringResource(dev.gokanaz.kplayer.core.R.string.subtitle_position),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Top", "Center", "Bottom").forEach { position ->
                            PositionOption(
                                position = position,
                                selected = uiState.subtitlePosition == position,
                                onClick = { viewModel.updateSubtitlePosition(position) }
                            )
                        }
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.vertical_offset),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = "${uiState.verticalOffset}%",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = uiState.verticalOffset.toFloat(),
                        onValueChange = { viewModel.updateVerticalOffset(it.toInt()) },
                        valueRange = 0f..100f,
                        steps = 20,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.max_lines),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = uiState.maxLines.toString(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Slider(
                        value = uiState.maxLines.toFloat(),
                        onValueChange = { viewModel.updateMaxLines(it.toInt()) },
                        valueRange = 1f..5f,
                        steps = 4,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    
                    Text(
                        text = stringResource(dev.gokanaz.kplayer.core.R.string.text_alignment),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AlignmentOption(
                            icon = Icons.Default.FormatAlignLeft,
                            selected = uiState.textAlignment == "Left",
                            onClick = { viewModel.updateTextAlignment("Left") }
                        )
                        AlignmentOption(
                            icon = Icons.Default.FormatAlignCenter,
                            selected = uiState.textAlignment == "Center",
                            onClick = { viewModel.updateTextAlignment("Center") }
                        )
                        AlignmentOption(
                            icon = Icons.Default.FormatAlignRight,
                            selected = uiState.textAlignment == "Right",
                            onClick = { viewModel.updateTextAlignment("Right") }
                        )
                    }
                }
                
                item {
                    PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.advanced))
                }
                
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { showEncodingMenu = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.subtitle_encoding),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.subtitleEncoding,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Box {
                            Text(
                                text = uiState.subtitleEncoding,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            DropdownMenu(
                                expanded = showEncodingMenu,
                                onDismissRequest = { showEncodingMenu = false }
                            ) {
                                listOf("Auto", "UTF-8", "UTF-16", "ASCII", "ISO-8859-1", "ISO-8859-15", "Windows-1252").forEach { encoding ->
                                    DropdownMenuItem(
                                        text = { Text(encoding) },
                                        onClick = {
                                            viewModel.updateSubtitleEncoding(encoding)
                                            showEncodingMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { showFormatMenu = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Subtitles, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(dev.gokanaz.kplayer.core.R.string.subtitle_format_preference),
                                fontSize = 16.sp
                            )
                            Text(
                                text = uiState.subtitleFormat,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Box {
                            Text(
                                text = uiState.subtitleFormat,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            DropdownMenu(
                                expanded = showFormatMenu,
                                onDismissRequest = { showFormatMenu = false }
                            ) {
                                listOf("Auto", "SRT", "ASS/SSA", "VTT", "TXT", "MicroDVD").forEach { format ->
                                    DropdownMenuItem(
                                        text = { Text(format) },
                                        onClick = {
                                            viewModel.updateSubtitleFormat(format)
                                            showFormatMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    SwitchPreference(
                        icon = Icons.Default.Build,
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.override_ass_styling),
                        checked = uiState.overrideAssStyling,
                        onCheckedChange = viewModel::updateOverrideAssStyling
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(dev.gokanaz.kplayer.core.R.string.subtitle_sync_method),
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp
                        )
                        
                        Text(
                            text = uiState.syncMethod,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    CacheItem(
                        title = stringResource(dev.gokanaz.kplayer.core.R.string.subtitle_cache),
                        size = uiState.cacheSize,
                        onClear = viewModel::clearCache
                    )
                }
                
                item {
                    PreferenceSection(title = stringResource(dev.gokanaz.kplayer.core.R.string.preview))
                }
                
                item {
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            OutlinedTextField(
                                value = previewText,
                                onValueChange = { previewText = it },
                                label = { Text("Preview Text") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            SubtitlePreview(
                                text = previewText,
                                fontFamily = uiState.fontFamily,
                                fontSize = uiState.fontSize,
                                fontColor = uiState.fontColor,
                                bold = uiState.boldText,
                                italic = uiState.italicText,
                                shadow = uiState.textShadow,
                                shadowOpacity = uiState.shadowOpacity,
                                backgroundColor = uiState.backgroundColor,
                                backgroundOpacity = uiState.backgroundOpacity,
                                backgroundBlur = uiState.backgroundBlur,
                                position = uiState.subtitlePosition,
                                verticalOffset = uiState.verticalOffset,
                                alignment = uiState.textAlignment
                            )
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = viewModel::resetToDefaults,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset to Defaults")
                        }
                        
                        Button(
                            onClick = viewModel::testWithSampleSubtitle,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Test Sample")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubtitlePreview(
    text: String,
    fontFamily: Font,
    fontSize: Int,
    fontColor: Color,
    bold: Boolean,
    italic: Boolean,
    shadow: Boolean,
    shadowOpacity: Float,
    backgroundColor: Color,
    backgroundOpacity: Float,
    backgroundBlur: Boolean,
    position: String,
    verticalOffset: Int,
    alignment: String
) {
    val fontSizeSp = when (fontSize) {
        0 -> 12.sp
        1 -> 16.sp
        2 -> 20.sp
        else -> 24.sp
    }
    
    val fontFamilyStyle = when (fontFamily) {
        Font.SYSTEM -> FontFamily.Default
        Font.SANS_SERIF -> FontFamily.SansSerif
        Font.SERIF -> FontFamily.Serif
        Font.MONOSPACE -> FontFamily.Monospace
    }
    
    val fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
    val fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal
    
    val textAlign = when (alignment) {
        "Left" -> androidx.compose.ui.text.style.TextAlign.Left
        "Center" -> androidx.compose.ui.text.style.TextAlign.Center
        "Right" -> androidx.compose.ui.text.style.TextAlign.Right
        else -> androidx.compose.ui.text.style.TextAlign.Center
    }
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(8.dp),
        contentAlignment = when (position) {
            "Top" -> Alignment.TopCenter
            "Bottom" -> Alignment.BottomCenter
            else -> Alignment.Center
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .then(
                    if (verticalOffset != 50) {
                        Modifier.padding(vertical = ((verticalOffset - 50) / 50f * 40).dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (backgroundColor != Color.Transparent) {
                            Modifier.background(
                                color = backgroundColor.copy(alpha = backgroundOpacity),
                                shape = RoundedCornerShape(4.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .then(
                        if (shadow) {
                            Modifier.shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(4.dp),
                                spotColor = Color.Black.copy(alpha = shadowOpacity)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = text,
                    fontSize = fontSizeSp,
                    fontFamily = fontFamilyStyle,
                    fontWeight = fontWeight,
                    fontStyle = fontStyle,
                    color = fontColor,
                    textAlign = textAlign,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ColorPickerButton(
    color: Color,
    onColorSelected: (Color) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    
    Box {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color, RoundedCornerShape(4.dp))
                .clickable { showPicker = true }
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
        )
        
        if (showPicker) {
            AlertDialog(
                onDismissRequest = { showPicker = false },
                title = { Text("Select Color") },
                text = {
                    Column {
                        listOf(
                            Color.White,
                            Color.Black,
                            Color.Red,
                            Color.Green,
                            Color.Blue,
                            Color.Yellow,
                            Color.Cyan,
                            Color.Magenta,
                            Color(0xFFFFA500), // Orange
                            Color(0xFF800080) // Purple
                        ).chunked(5) { rowColors ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowColors.forEach { colorOption ->
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(colorOption, RoundedCornerShape(4.dp))
                                            .clickable {
                                                onColorSelected(colorOption)
                                                showPicker = false
                                            }
                                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPicker = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun PositionOption(
    position: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Text(
            text = position,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun AlignmentOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
fun CacheItem(
    title: String,
    size: String,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp
                )
                Text(
                    text = size,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        TextButton(onClick = onClear) {
            Text(stringResource(dev.gokanaz.kplayer.core.R.string.clear))
        }
    }
}

@Composable
fun SwitchPreference(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun PreferenceSection(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}
