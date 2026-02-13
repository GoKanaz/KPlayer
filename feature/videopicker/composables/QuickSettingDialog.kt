package dev.gokanaz.kplayer.feature.videopicker.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickSettingDialog(
    currentSort: SortOption,
    currentSortOrder: SortOrder,
    currentFilters: FilterOptions,
    onDismiss: () -> Unit,
    onApply: (SortOption, SortOrder, FilterOptions) -> Unit
) {
    var selectedSort by remember { mutableStateOf(currentSort) }
    var selectedSortOrder by remember { mutableStateOf(currentSortOrder) }
    var selectedFilters by remember { mutableStateOf(currentFilters) }
    
    var selectedTab by remember { mutableStateOf(0) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Sort & Filter")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Sort") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = null
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Filter") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null
                            )
                        }
                    )
                }
                
                when (selectedTab) {
                    0 -> SortTab(
                        selectedSort = selectedSort,
                        selectedSortOrder = selectedSortOrder,
                        onSortChange = { selectedSort = it },
                        onSortOrderChange = { selectedSortOrder = it }
                    )
                    1 -> FilterTab(
                        filters = selectedFilters,
                        onFiltersChange = { selectedFilters = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onApply(selectedSort, selectedSortOrder, selectedFilters)
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun SortTab(
    selectedSort: SortOption,
    selectedSortOrder: SortOrder,
    onSortChange: (SortOption) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Sort by",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        SortOption.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedSort == option,
                        onClick = { onSortChange(option) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedSort == option,
                    onClick = null
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = option.icon,
                    contentDescription = null
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = option.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Order",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SortOrder.entries.forEach { order ->
                FilterChip(
                    selected = selectedSortOrder == order,
                    onClick = { onSortOrderChange(order) },
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = order.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(order.displayName)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                
                if (order != SortOrder.entries.last()) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun FilterTab(
    filters: FilterOptions,
    onFiltersChange: (FilterOptions) -> Unit
) {
    var durationFilter by remember { mutableStateOf(filters.durationFilter) }
    var resolutionFilter by remember { mutableStateOf(filters.resolutionFilter) }
    var dateFilter by remember { mutableStateOf(filters.dateFilter) }
    var selectedFolders by remember { mutableStateOf(filters.selectedFolders) }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FilterSection(
                title = "Duration",
                icon = Icons.Default.AccessTime
            ) {
                DurationFilter.entries.forEach { duration ->
                    FilterChip(
                        selected = durationFilter == duration,
                        onClick = { 
                            durationFilter = duration
                            onFiltersChange(
                                filters.copy(
                                    durationFilter = duration,
                                    resolutionFilter = resolutionFilter,
                                    dateFilter = dateFilter,
                                    selectedFolders = selectedFolders
                                )
                            )
                        },
                        label = { Text(duration.displayName) },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
        
        item {
            FilterSection(
                title = "Resolution",
                icon = Icons.Default.HighQuality
            ) {
                ResolutionFilter.entries.forEach { resolution ->
                    FilterChip(
                        selected = resolutionFilter == resolution,
                        onClick = { 
                            resolutionFilter = resolution
                            onFiltersChange(
                                filters.copy(
                                    durationFilter = durationFilter,
                                    resolutionFilter = resolution,
                                    dateFilter = dateFilter,
                                    selectedFolders = selectedFolders
                                )
                            )
                        },
                        label = { Text(resolution.displayName) },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
        
        item {
            FilterSection(
                title = "Date",
                icon = Icons.Default.DateRange
            ) {
                DateFilter.entries.forEach { date ->
                    FilterChip(
                        selected = dateFilter == date,
                        onClick = { 
                            dateFilter = date
                            onFiltersChange(
                                filters.copy(
                                    durationFilter = durationFilter,
                                    resolutionFilter = resolutionFilter,
                                    dateFilter = date,
                                    selectedFolders = selectedFolders
                                )
                            )
                        },
                        label = { Text(date.displayName) },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
        }
        
        item {
            FilterSection(
                title = "Folders",
                icon = Icons.Default.Folder
            ) {
                // This would be populated from actual folders
                listOf("Downloads", "Camera", "Movies", "Recordings").forEach { folder ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = folder in selectedFolders,
                                onValueChange = { isSelected ->
                                    selectedFolders = if (isSelected) {
                                        selectedFolders + folder
                                    } else {
                                        selectedFolders - folder
                                    }
                                    onFiltersChange(
                                        filters.copy(
                                            durationFilter = durationFilter,
                                            resolutionFilter = resolutionFilter,
                                            dateFilter = dateFilter,
                                            selectedFolders = selectedFolders
                                        )
                                    )
                                },
                                role = Role.Checkbox
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = folder in selectedFolders,
                            onCheckedChange = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = folder,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            content()
        }
    }
}

enum class SortOption(
    val displayName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Name("Name", Icons.Default.SortByAlpha),
    Date("Date added", Icons.Default.DateRange),
    Size("File size", Icons.Default.DataUsage),
    Duration("Duration", Icons.Default.AccessTime),
    Resolution("Resolution", Icons.Default.HighQuality)
}

enum class SortOrder(
    val displayName: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Ascending("Ascending", Icons.Default.ArrowUpward),
    Descending("Descending", Icons.Default.ArrowDownward)
}

enum class DurationFilter(
    val displayName: String
) {
    All("All"),
    LessThan5Min("< 5 min"),
    Between5And15Min("5-15 min"),
    Between15And30Min("15-30 min"),
    Between30And60Min("30-60 min"),
    MoreThan60Min("> 60 min")
}

enum class ResolutionFilter(
    val displayName: String
) {
    All("All"),
    Resolution480p("480p"),
    Resolution720p("720p"),
    Resolution1080p("1080p"),
    Resolution4K("4K")
}

enum class DateFilter(
    val displayName: String
) {
    All("All"),
    Today("Today"),
    ThisWeek("This week"),
    ThisMonth("This month"),
    ThisYear("This year"),
    Custom("Custom")
}

data class FilterOptions(
    val durationFilter: DurationFilter = DurationFilter.All,
    val resolutionFilter: ResolutionFilter = ResolutionFilter.All,
    val dateFilter: DateFilter = DateFilter.All,
    val selectedFolders: Set<String> = emptySet(),
    val customDateRange: ClosedRange<Date>? = null
)
