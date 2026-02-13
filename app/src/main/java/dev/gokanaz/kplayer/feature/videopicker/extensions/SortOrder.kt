package dev.gokanaz.kplayer.feature.videopicker.extensions

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.ui.graphics.vector.ImageVector
import dev.gokanaz.kplayer.core.model.SortOrder
import dev.gokanaz.kplayer.core.model.SortType
import dev.gokanaz.kplayer.feature.videopicker.R

/**
 * Extension functions for SortOrder enum
 */

/**
 * Get display name for the sort order based on current context
 */
fun SortOrder.toDisplayName(context: Context): String {
    return when (this) {
        SortOrder.ASCENDING -> context.getString(R.string.sort_order_ascending)
        SortOrder.DESCENDING -> context.getString(R.string.sort_order_descending)
    }
}

/**
 * Get icon representing the sort order
 */
fun SortOrder.toIcon(): ImageVector {
    return when (this) {
        SortOrder.ASCENDING -> Icons.Default.ArrowUpward
        SortOrder.DESCENDING -> Icons.Default.ArrowDownward
    }
}

/**
 * Toggle sort order
 */
fun SortOrder.toggle(): SortOrder {
    return when (this) {
        SortOrder.ASCENDING -> SortOrder.DESCENDING
        SortOrder.DESCENDING -> SortOrder.ASCENDING
    }
}

/**
 * Get sort direction multiplier (1 for ascending, -1 for descending)
 */
fun SortOrder.getSortDirection(): Int {
    return when (this) {
        SortOrder.ASCENDING -> 1
        SortOrder.DESCENDING -> -1
    }
}

/**
 * Get comparator for specific type based on sort order
 */
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

/**
 * Get comparator for strings
 */
fun SortOrder.getStringComparator(): Comparator<String> {
    return when (this) {
        SortOrder.ASCENDING -> Comparator.naturalOrder()
        SortOrder.DESCENDING -> Comparator.reverseOrder()
    }
}

/**
 * Get comparator for numbers
 */
fun <T : Comparable<T>> SortOrder.getNumberComparator(): Comparator<T> {
    return when (this) {
        SortOrder.ASCENDING -> Comparator.naturalOrder()
        SortOrder.DESCENDING -> Comparator.reverseOrder()
    }
}

/**
 * Get comparator for dates
 */
fun SortOrder.getDateComparator(): Comparator<Long> {
    return when (this) {
        SortOrder.ASCENDING -> Comparator { a, b -> a.compareTo(b) }
        SortOrder.DESCENDING -> Comparator { a, b -> b.compareTo(a) }
    }
}

/**
 * Check if order is ascending
 */
fun SortOrder.isAscending(): Boolean = this == SortOrder.ASCENDING

/**
 * Check if order is descending
 */
fun SortOrder.isDescending(): Boolean = this == SortOrder.DESCENDING

/**
 * Get description for accessibility
 */
fun SortOrder.getContentDescription(context: Context): String {
    return when (this) {
        SortOrder.ASCENDING -> context.getString(R.string.content_desc_ascending)
        SortOrder.DESCENDING -> context.getString(R.string.content_desc_descending)
    }
}

/**
 * Convert to analytics tracking value
 */
fun SortOrder.toAnalyticsValue(): String {
    return when (this) {
        SortOrder.ASCENDING -> "asc"
        SortOrder.DESCENDING -> "desc"
    }
}

/**
 * Apply sort order to a list with custom comparator
 */
fun <T> SortOrder.applyTo(list: List<T>, comparator: Comparator<T>): List<T> {
    return when (this) {
        SortOrder.ASCENDING -> list.sortedWith(comparator)
        SortOrder.DESCENDING -> list.sortedWith(comparator.reversed())
    }
}

/**
 * Apply sort order to a list with key extractor
 */
fun <T, R : Comparable<R>> SortOrder.applyTo(
    list: List<T>,
    keyExtractor: (T) -> R
): List<T> {
    return when (this) {
        SortOrder.ASCENDING -> list.sortedBy(keyExtractor)
        SortOrder.DESCENDING -> list.sortedByDescending(keyExtractor)
    }
}

/**
 * Get next order in cycle
 */
fun SortOrder.next(): SortOrder {
    return when (this) {
        SortOrder.ASCENDING -> SortOrder.DESCENDING
        SortOrder.DESCENDING -> SortOrder.ASCENDING
    }
}

/**
 * Get SQL ORDER BY clause
 */
fun SortOrder.toSqlOrder(): String {
    return when (this) {
        SortOrder.ASCENDING -> "ASC"
        SortOrder.DESCENDING -> "DESC"
    }
}

/**
 * Get arrow direction as rotation degrees
 */
fun SortOrder.getArrowRotation(): Float {
    return when (this) {
        SortOrder.ASCENDING -> 0f
        SortOrder.DESCENDING -> 180f
    }
}

/**
 * Check if this order matches another
 */
fun SortOrder.matches(other: SortOrder): Boolean {
    return this == other
}
