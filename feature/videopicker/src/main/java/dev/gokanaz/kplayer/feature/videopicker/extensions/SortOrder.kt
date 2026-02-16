package dev.gokanaz.kplayer.feature.videopicker.extensions

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType

fun SortOrder.toDisplayName(context: Context): String {
    return when (this) {
        SortOrder.ASCENDING -> "Ascending"
        SortOrder.DESCENDING -> "Descending"
    }
}

fun SortOrder.toIcon(): ImageVector {
    return when (this) {
        SortOrder.ASCENDING -> Icons.Default.ArrowUpward
        SortOrder.DESCENDING -> Icons.Default.ArrowDownward
    }
}

fun SortOrder.toggle(): SortOrder {
    return when (this) {
        SortOrder.ASCENDING -> SortOrder.DESCENDING
        SortOrder.DESCENDING -> SortOrder.ASCENDING
    }
}

fun SortOrder.getSortDirection(): Int {
    return when (this) {
        SortOrder.ASCENDING -> 1
        SortOrder.DESCENDING -> -1
    }
}

fun <T> SortOrder.getComparatorForType(
    sortType: SortType,
    extractor: (T) -> Comparable<*>
): Comparator<T> {
    val baseComparator = Comparator<T> { a, b ->
        val valueA = extractor(a)
        val valueB = extractor(b)
        compareValues(valueA, valueB)
    }

    return when (this) {
        SortOrder.ASCENDING -> baseComparator
        SortOrder.DESCENDING -> baseComparator.reversed()
    }
}

fun SortOrder.getStringComparator(): Comparator<String> {
    return when (this) {
        SortOrder.ASCENDING -> Comparator.naturalOrder()
        SortOrder.DESCENDING -> Comparator.reverseOrder()
    }
}

fun <T : Comparable<T>> SortOrder.getNumberComparator(): Comparator<T> {
    return when (this) {
        SortOrder.ASCENDING -> Comparator.naturalOrder()
        SortOrder.DESCENDING -> Comparator.reverseOrder()
    }
}

fun SortOrder.getDateComparator(): Comparator<Long> {
    return when (this) {
        SortOrder.ASCENDING -> Comparator { a, b -> a.compareTo(b) }
        SortOrder.DESCENDING -> Comparator { a, b -> b.compareTo(a) }
    }
}

fun SortOrder.isAscending(): Boolean = this == SortOrder.ASCENDING

fun SortOrder.isDescending(): Boolean = this == SortOrder.DESCENDING

fun SortOrder.getContentDescription(context: Context): String {
    return when (this) {
        SortOrder.ASCENDING -> "Ascending order"
        SortOrder.DESCENDING -> "Descending order"
    }
}

fun SortOrder.toAnalyticsValue(): String {
    return when (this) {
        SortOrder.ASCENDING -> "asc"
        SortOrder.DESCENDING -> "desc"
    }
}

fun <T> SortOrder.applyTo(list: List<T>, comparator: Comparator<T>): List<T> {
    return when (this) {
        SortOrder.ASCENDING -> list.sortedWith(comparator)
        SortOrder.DESCENDING -> list.sortedWith(comparator.reversed())
    }
}

fun <T, R : Comparable<R>> SortOrder.applyTo(
    list: List<T>,
    keyExtractor: (T) -> R
): List<T> {
    return when (this) {
        SortOrder.ASCENDING -> list.sortedBy(keyExtractor)
        SortOrder.DESCENDING -> list.sortedByDescending(keyExtractor)
    }
}

fun SortOrder.next(): SortOrder {
    return when (this) {
        SortOrder.ASCENDING -> SortOrder.DESCENDING
        SortOrder.DESCENDING -> SortOrder.ASCENDING
    }
}

fun SortOrder.toSqlOrder(): String {
    return when (this) {
        SortOrder.ASCENDING -> "ASC"
        SortOrder.DESCENDING -> "DESC"
    }
}

fun SortOrder.getArrowRotation(): Float {
    return when (this) {
        SortOrder.ASCENDING -> 0f
        SortOrder.DESCENDING -> 180f
    }
}

fun SortOrder.matches(other: SortOrder): Boolean {
    return this == other
}
