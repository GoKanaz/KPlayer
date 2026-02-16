package dev.gokanaz.kplayer.feature.videopicker.composables

import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.core.model.SortOrder as CoreSortOrder
import dev.gokanaz.kplayer.core.model.MediaLayoutMode as CoreMediaLayoutMode
import dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.DurationFilter as VMDurationFilter
import dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.ResolutionFilter as VMResolutionFilter
import dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.DateFilter as VMDateFilter
import dev.gokanaz.kplayer.feature.videopicker.screens.mediapicker.FilterOptions as VMFilterOptions

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

fun VMFilterOptions.toComposableType(): FilterOptions {
    return FilterOptions(
        durationFilter = when (this.durationFilter) {
            VMDurationFilter.All -> DurationFilter.All
            VMDurationFilter.LessThan5Min -> DurationFilter.LessThan5Min
            VMDurationFilter.Between5And15Min -> DurationFilter.Between5And15Min
            VMDurationFilter.Between15And30Min -> DurationFilter.Between15And30Min
            VMDurationFilter.Between30And60Min -> DurationFilter.Between30And60Min
            VMDurationFilter.MoreThan60Min -> DurationFilter.MoreThan60Min
        },
        resolutionFilter = when (this.resolutionFilter) {
            VMResolutionFilter.All -> ResolutionFilter.All
            VMResolutionFilter.Resolution480p -> ResolutionFilter.Resolution480p
            VMResolutionFilter.Resolution720p -> ResolutionFilter.Resolution720p
            VMResolutionFilter.Resolution1080p -> ResolutionFilter.Resolution1080p
            VMResolutionFilter.Resolution4K -> ResolutionFilter.Resolution4K
        },
        dateFilter = when (this.dateFilter) {
            VMDateFilter.All -> DateFilter.All
            VMDateFilter.Today -> DateFilter.Today
            VMDateFilter.ThisWeek -> DateFilter.ThisWeek
            VMDateFilter.ThisMonth -> DateFilter.ThisMonth
            VMDateFilter.ThisYear -> DateFilter.ThisYear
            VMDateFilter.Custom -> DateFilter.Custom
        },
        selectedFolders = this.selectedFolders
    )
}

fun FilterOptions.toViewModelType(): VMFilterOptions {
    return VMFilterOptions(
        durationFilter = when (this.durationFilter) {
            DurationFilter.All -> VMDurationFilter.All
            DurationFilter.LessThan5Min -> VMDurationFilter.LessThan5Min
            DurationFilter.Between5And15Min -> VMDurationFilter.Between5And15Min
            DurationFilter.Between15And30Min -> VMDurationFilter.Between15And30Min
            DurationFilter.Between30And60Min -> VMDurationFilter.Between30And60Min
            DurationFilter.MoreThan60Min -> VMDurationFilter.MoreThan60Min
        },
        resolutionFilter = when (this.resolutionFilter) {
            ResolutionFilter.All -> VMResolutionFilter.All
            ResolutionFilter.Resolution480p -> VMResolutionFilter.Resolution480p
            ResolutionFilter.Resolution720p -> VMResolutionFilter.Resolution720p
            ResolutionFilter.Resolution1080p -> VMResolutionFilter.Resolution1080p
            ResolutionFilter.Resolution4K -> VMResolutionFilter.Resolution4K
        },
        dateFilter = when (this.dateFilter) {
            DateFilter.All -> VMDateFilter.All
            DateFilter.Today -> VMDateFilter.Today
            DateFilter.ThisWeek -> VMDateFilter.ThisWeek
            DateFilter.ThisMonth -> VMDateFilter.ThisMonth
            DateFilter.ThisYear -> VMDateFilter.ThisYear
            DateFilter.Custom -> VMDateFilter.Custom
        },
        selectedFolders = this.selectedFolders
    )
}

fun CoreMediaLayoutMode.toDisplayMode(): DisplayMode {
    return when (this) {
        CoreMediaLayoutMode.GRID -> DisplayMode.Grid
        CoreMediaLayoutMode.LIST -> DisplayMode.List
    }
}
