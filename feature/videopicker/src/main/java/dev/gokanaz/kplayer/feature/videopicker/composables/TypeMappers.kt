package dev.gokanaz.kplayer.feature.videopicker.composables

import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.SortOrder as CoreSortOrder

fun SortOption.toSortType(): SortType {
    return when (this) {
        SortOption.Name -> SortType.NAME
        SortOption.Date -> SortType.DATE
        SortOption.Size -> SortType.SIZE
        SortOption.Duration -> SortType.DURATION
        SortOption.Resolution -> SortType.RESOLUTION
    }
}

fun SortType.toSortOption(): SortOption {
    return when (this) {
        SortType.NAME -> SortOption.Name
        SortType.DATE -> SortOption.Date
        SortType.SIZE -> SortOption.Size
        SortType.DURATION -> SortOption.Duration
        SortType.RESOLUTION -> SortOption.Resolution
        else -> SortOption.Date
    }
}

fun SortOrder.toCoreType(): CoreSortOrder {
    return when (this) {
        SortOrder.Ascending -> CoreSortOrder.ASCENDING
        SortOrder.Descending -> CoreSortOrder.DESCENDING
    }
}

fun CoreSortOrder.toComposableType(): SortOrder {
    return when (this) {
        CoreSortOrder.ASCENDING -> SortOrder.Ascending
        CoreSortOrder.DESCENDING -> SortOrder.Descending
    }
}

fun dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.FilterOptions.toComposableType(): FilterOptions {
    return FilterOptions(
        durationFilter = this.durationFilter,
        resolutionFilter = this.resolutionFilter,
        dateFilter = this.dateFilter,
        selectedFolders = this.selectedFolders
    )
}

fun FilterOptions.toViewModelType(): dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.FilterOptions {
    return dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.FilterOptions(
        durationFilter = this.durationFilter,
        resolutionFilter = this.resolutionFilter,
        dateFilter = this.dateFilter,
        selectedFolders = this.selectedFolders
    )
}

fun dev.gokanaz.kplayer.core.model.MediaLayoutMode.toDisplayMode(): DisplayMode {
    return when (this) {
        dev.gokanaz.kplayer.core.model.MediaLayoutMode.GRID -> DisplayMode.Grid
        dev.gokanaz.kplayer.core.model.MediaLayoutMode.LIST -> DisplayMode.List
    }
}
