package dev.gokanaz.kplayer.core.ui.base

sealed class DataState<out T> {
    object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : DataState<Nothing>()
    object Empty : DataState<Nothing>()
}

fun <T> DataState<T>.getOrNull(): T? = when (this) {
    is DataState.Success -> data
    else -> null
}

fun <T, R> DataState<T>.map(transform: (T) -> R): DataState<R> = when (this) {
    is DataState.Loading -> DataState.Loading
    is DataState.Success -> DataState.Success(transform(data))
    is DataState.Error -> DataState.Error(message, throwable)
    is DataState.Empty -> DataState.Empty
}
