package dev.gokanaz.kplayer.core.ui.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

fun PaddingValues.add(
    additionalPadding: PaddingValues,
    layoutDirection: LayoutDirection
): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection) + additionalPadding.calculateStartPadding(layoutDirection),
        top = this.calculateTopPadding() + additionalPadding.calculateTopPadding(),
        end = this.calculateEndPadding(layoutDirection) + additionalPadding.calculateEndPadding(layoutDirection),
        bottom = this.calculateBottomPadding() + additionalPadding.calculateBottomPadding()
    )
}

fun PaddingValues.add(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(LayoutDirection.Ltr) + horizontal,
        top = this.calculateTopPadding() + vertical,
        end = this.calculateEndPadding(LayoutDirection.Ltr) + horizontal,
        bottom = this.calculateBottomPadding() + vertical
    )
}

fun PaddingValues.remove(
    paddingToRemove: PaddingValues,
    layoutDirection: LayoutDirection
): PaddingValues {
    return PaddingValues(
        start = (this.calculateStartPadding(layoutDirection) - paddingToRemove.calculateStartPadding(layoutDirection)).coerceAtLeast(0.dp),
        top = (this.calculateTopPadding() - paddingToRemove.calculateTopPadding()).coerceAtLeast(0.dp),
        end = (this.calculateEndPadding(layoutDirection) - paddingToRemove.calculateEndPadding(layoutDirection)).coerceAtLeast(0.dp),
        bottom = (this.calculateBottomPadding() - paddingToRemove.calculateBottomPadding()).coerceAtLeast(0.dp)
    )
}

fun PaddingValues.toDp(): PaddingValues = this

fun PaddingValues.toArrangementSpacedBy(): Dp {
    return (this.calculateStartPadding(LayoutDirection.Ltr) + this.calculateEndPadding(LayoutDirection.Ltr)) / 2
}

fun PaddingValues.only(
    start: Boolean = false,
    top: Boolean = false,
    end: Boolean = false,
    bottom: Boolean = false
): PaddingValues {
    return PaddingValues(
        start = if (start) this.calculateStartPadding(LayoutDirection.Ltr) else 0.dp,
        top = if (top) this.calculateTopPadding() else 0.dp,
        end = if (end) this.calculateEndPadding(LayoutDirection.Ltr) else 0.dp,
        bottom = if (bottom) this.calculateBottomPadding() else 0.dp
    )
}

fun PaddingValues.horizontal(): Dp {
    return this.calculateStartPadding(LayoutDirection.Ltr) + this.calculateEndPadding(LayoutDirection.Ltr)
}

fun PaddingValues.vertical(): Dp {
    return this.calculateTopPadding() + this.calculateBottomPadding()
}
