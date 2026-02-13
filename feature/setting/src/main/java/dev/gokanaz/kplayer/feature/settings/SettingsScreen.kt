package dev.gokanaz.kplayer.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.gokanaz.kplayer.core.model.DarkMode
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.ThemeColor
import dev.gokanaz.kplayer.core.model.player.LoopMode
import dev.gokanaz.kplayer.core.model.player.Resume
import dev.gokanaz.kplayer.core.model.player.DecoderPriority
import dev.gokanaz.kplayer.core.model.player.FastSeek
import dev.gokanaz.kplayer.core.model.player.DoubleTapGesture
import dev.gokanaz.kplayer.core.model.ui.ControlButtonsPosition
import dev.gokanaz.kplayer.core.ui.components.PreferenceItem
import dev.gokanaz.kplayer.core.ui.components.PreferenceSwitch
import dev.gokanaz.kplayer.core.ui.components.PreferenceSlider
import dev.gokanaz.kplayer.core.ui.theme.AppTheme
import dev.gokanaz.kplayer.feature.settings.composables.OptionDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_title)) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SettingsSection(title = stringResource(id = R.string.settings_appearance))
            }
            
            item {
                AppearanceSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_playback))
            }
            
            item {
                PlaybackSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_audio))
            }
            
            item {
                AudioSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_subtitle))
            }
            
            item {
                SubtitleSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_gesture))
            }
            
            item {
                GestureSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_storage))
            }
            
            item {
                StorageSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_advanced))
            }
            
            item {
                AdvancedSettings()
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.settings_about))
            }
            
            item {
                AboutSettings()
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = AppTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}

@Composable
fun AppearanceSettings() {
    var showDarkModeDialog by remember { mutableStateOf(false) }
    var showThemeColorDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showSortTypeDialog by remember { mutableStateOf(false) }
    var showSortOrderDialog by remember { mutableStateOf(false) }
    
    Column {
        PreferenceItem(
            title = stringResource(id = R.string.settings_dark_mode),
            summary = DarkMode.SYSTEM.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.DarkMode,
            onClick = { showDarkModeDialog = true }
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_theme_color),
            summary = ThemeColor.DEFAULT.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.ColorLens,
            onClick = { showThemeColorDialog = true }
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_dynamic_color),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.AutoAwesome
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_language),
            summary = "English",
            icon = androidx.compose.material.icons.Icons.Default.Language,
            onClick = { showLanguageDialog = true }
        )
        
        PreferenceSlider(
            title = stringResource(id = R.string.settings_font_scale),
            value = 1.0f,
            onValueChange = {},
            valueRange = 0.8f..1.2f,
            valueFormat = { "%.1fx".format(it) },
            icon = androidx.compose.material.icons.Icons.Default.TextFields
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_view_type),
            summary = "Grid",
            icon = androidx.compose.material.icons.Icons.Default.GridView,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_sort_type),
            summary = SortType.NAME.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.Sort,
            onClick = { showSortTypeDialog = true }
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_sort_order),
            summary = SortOrder.ASCENDING.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.SortByAlpha,
            onClick = { showSortOrderDialog = true }
        )
    }
    
    if (showDarkModeDialog) {
        OptionDialog(
            title = stringResource(id = R.string.settings_dark_mode),
            options = DarkMode.values().map { it.toDisplayName() },
            selectedIndex = 0,
            onDismiss = { showDarkModeDialog = false },
            onOptionSelected = { index ->
                showDarkModeDialog = false
            }
        )
    }
    
    if (showThemeColorDialog) {
        OptionDialog(
            title = stringResource(id = R.string.settings_theme_color),
            options = ThemeColor.values().map { it.toDisplayName() },
            selectedIndex = 0,
            onDismiss = { showThemeColorDialog = false },
            onOptionSelected = { index ->
                showThemeColorDialog = false
            }
        )
    }
    
    if (showSortTypeDialog) {
        OptionDialog(
            title = stringResource(id = R.string.settings_sort_type),
            options = SortType.values().map { it.toDisplayName() },
            selectedIndex = 0,
            onDismiss = { showSortTypeDialog = false },
            onOptionSelected = { index ->
                showSortTypeDialog = false
            }
        )
    }
    
    if (showSortOrderDialog) {
        OptionDialog(
            title = stringResource(id = R.string.settings_sort_order),
            options = SortOrder.values().map { it.toDisplayName() },
            selectedIndex = 0,
            onDismiss = { showSortOrderDialog = false },
            onOptionSelected = { index ->
                showSortOrderDialog = false
            }
        )
    }
}

@Composable
fun PlaybackSettings() {
    var showQualityDialog by remember { mutableStateOf(false) }
    var showRepeatModeDialog by remember { mutableStateOf(false) }
    var showResumeDialog by remember { mutableStateOf(false) }
    var showFastSeekDialog by remember { mutableStateOf(false) }
    var showDecoderDialog by remember { mutableStateOf(false) }
    
    Column {
        PreferenceItem(
            title = stringResource(id = R.string.settings_default_quality),
            summary = "Auto",
            icon = androidx.compose.material.icons.Icons.Default.Quality,
            onClick = { showQualityDialog = true }
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_auto_play),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.PlayArrow
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_repeat_mode),
            summary = LoopMode.NONE.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.Repeat,
            onClick = { showRepeatModeDialog = true }
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_shuffle),
            checked = false,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.Shuffle
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_resume_playback),
            summary = Resume.ASK.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.History,
            onClick = { showResumeDialog = true }
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_fast_seek),
            summary = FastSeek.WIFI_ONLY.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.FastForward,
            onClick = { showFastSeekDialog = true }
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_decoder_priority),
            summary = DecoderPriority.AUTO.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.Settings,
            onClick = { showDecoderDialog = true }
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_background_play),
            checked = false,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.PlayCircle
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_picture_in_picture),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.PictureInPicture
        )
    }
}

@Composable
fun AudioSettings() {
    Column {
        PreferenceSlider(
            title = stringResource(id = R.string.settings_default_volume),
            value = 0.8f,
            onValueChange = {},
            valueRange = 0f..1f,
            valueFormat = { "${(it * 100).toInt()}%" },
            icon = androidx.compose.material.icons.Icons.Default.VolumeUp
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_equalizer_preset),
            summary = "Normal",
            icon = androidx.compose.material.icons.Icons.Default.Equalizer,
            onClick = {}
        )
        
        PreferenceSlider(
            title = stringResource(id = R.string.settings_volume_gesture_sensitivity),
            value = 0.5f,
            onValueChange = {},
            valueRange = 0.1f..1f,
            valueFormat = { "%.1f".format(it) },
            icon = androidx.compose.material.icons.Icons.Default.Gesture
        )
    }
}

@Composable
fun SubtitleSettings() {
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }
    
    Column {
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_subtitle_enabled),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.Subtitles
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_subtitle_language),
            summary = "English",
            icon = androidx.compose.material.icons.Icons.Default.Language,
            onClick = { showLanguageDialog = true }
        )
        
        PreferenceSlider(
            title = stringResource(id = R.string.settings_subtitle_size),
            value = 16f,
            onValueChange = {},
            valueRange = 10f..32f,
            valueFormat = { "${it.toInt()}sp" },
            icon = androidx.compose.material.icons.Icons.Default.TextFields
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_subtitle_font),
            summary = "System",
            icon = androidx.compose.material.icons.Icons.Default.FontDownload,
            onClick = { showFontDialog = true }
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_subtitle_color),
            summary = "White",
            icon = androidx.compose.material.icons.Icons.Default.Palette,
            onClick = {}
        )
        
        PreferenceSlider(
            title = stringResource(id = R.string.settings_subtitle_background_opacity),
            value = 0.8f,
            onValueChange = {},
            valueRange = 0f..1f,
            valueFormat = { "${(it * 100).toInt()}%" },
            icon = androidx.compose.material.icons.Icons.Default.FormatColorFill
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_subtitle_position),
            summary = "Bottom",
            icon = androidx.compose.material.icons.Icons.Default.VerticalAlignBottom,
            onClick = {}
        )
        
        PreferenceSlider(
            title = stringResource(id = R.string.settings_subtitle_sync_offset),
            value = 0f,
            onValueChange = {},
            valueRange = -5000f..5000f,
            valueFormat = { "${(it / 1000).toInt()}s" },
            icon = androidx.compose.material.icons.Icons.Default.Sync
        )
    }
}

@Composable
fun GestureSettings() {
    var showDoubleTapActionDialog by remember { mutableStateOf(false) }
    var showSeekDurationDialog by remember { mutableStateOf(false) }
    
    Column {
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_seek_gesture),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.TouchApp
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_volume_gesture),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.VolumeUp
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_brightness_gesture),
            checked = true,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.Brightness7
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_double_tap_action),
            summary = DoubleTapGesture.SEEK_FORWARD_BACKWARD.toDisplayName(),
            icon = androidx.compose.material.icons.Icons.Default.TouchApp,
            onClick = { showDoubleTapActionDialog = true }
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_seek_duration),
            summary = "10 seconds",
            icon = androidx.compose.material.icons.Icons.Default.Timer,
            onClick = { showSeekDurationDialog = true }
        )
        
        PreferenceSlider(
            title = stringResource(id = R.string.settings_gesture_sensitivity),
            value = 0.5f,
            onValueChange = {},
            valueRange = 0.1f..1f,
            valueFormat = { "%.1f".format(it) },
            icon = androidx.compose.material.icons.Icons.Default.Speed
        )
    }
}

@Composable
fun StorageSettings() {
    Column {
        PreferenceItem(
            title = stringResource(id = R.string.settings_storage_paths),
            summary = "/storage/emulated/0",
            icon = androidx.compose.material.icons.Icons.Default.Folder,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_scan_media),
            summary = "Last scan: 2 minutes ago",
            icon = androidx.compose.material.icons.Icons.Default.Refresh,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_cache_size),
            summary = "256 MB",
            icon = androidx.compose.material.icons.Icons.Default.Storage,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_storage_permission),
            summary = "Granted",
            icon = androidx.compose.material.icons.Icons.Default.Security,
            onClick = {}
        )
    }
}

@Composable
fun AdvancedSettings() {
    Column {
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_debug_mode),
            checked = false,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.BugReport
        )
        
        PreferenceSwitch(
            title = stringResource(id = R.string.settings_enable_logging),
            checked = false,
            onCheckedChange = {},
            icon = androidx.compose.material.icons.Icons.Default.DocumentScanner
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_export_logs),
            icon = androidx.compose.material.icons.Icons.Default.Upload,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_clear_all_data),
            summary = "This action cannot be undone",
            icon = androidx.compose.material.icons.Icons.Default.Delete,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_device_info),
            summary = "Android 14, Pixel 6",
            icon = androidx.compose.material.icons.Icons.Default.Info,
            onClick = {}
        )
    }
}

@Composable
fun AboutSettings() {
    Column {
        PreferenceItem(
            title = stringResource(id = R.string.settings_app_version),
            summary = "1.0.0 (2024.02)",
            icon = androidx.compose.material.icons.Icons.Default.Info,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_developer_info),
            icon = androidx.compose.material.icons.Icons.Default.Person,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_open_source_licenses),
            icon = androidx.compose.material.icons.Icons.Default.DocumentScanner,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_privacy_policy),
            icon = androidx.compose.material.icons.Icons.Default.Policy,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_terms_of_service),
            icon = androidx.compose.material.icons.Icons.Default.Document,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_support_development),
            icon = androidx.compose.material.icons.Icons.Default.Favorite,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_github_repository),
            icon = androidx.compose.material.icons.Icons.Default.Code,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_report_issue),
            icon = androidx.compose.material.icons.Icons.Default.BugReport,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_rate_app),
            icon = androidx.compose.material.icons.Icons.Default.Star,
            onClick = {}
        )
        
        PreferenceItem(
            title = stringResource(id = R.string.settings_share_app),
            icon = androidx.compose.material.icons.Icons.Default.Share,
            onClick = {}
        )
    }
}
