package dev.gokanaz.kplayer.core.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Immutable
sealed class ScreenState<out T> {
    object Loading : ScreenState<Nothing>()
    data class Content<T>(val data: T) : ScreenState<T>()
    data class Error(
        val message: String,
        val retryAction: (() -> Unit)? = null
    ) : ScreenState<Nothing>()
    object Empty : ScreenState<Nothing>()
}

fun <T> ScreenState<T>.toDataState(): DataState<T> = when (this) {
    is ScreenState.Loading -> DataState.Loading
    is ScreenState.Content -> DataState.Success(data)
    is ScreenState.Error -> DataState.Error(message)
    is ScreenState.Empty -> DataState.Empty
}

@Composable
fun <T> rememberScreenState(
    dataState: DataState<T>,
    retryAction: (() -> Unit)? = null
): ScreenState<T> {
    return remember(dataState, retryAction) {
        when (dataState) {
            is DataState.Loading -> ScreenState.Loading
            is DataState.Success -> ScreenState.Content(dataState.data)
            is DataState.Error -> ScreenState.Error(dataState.message, retryAction)
            is DataState.Empty -> ScreenState.Empty
        }
    }
}
