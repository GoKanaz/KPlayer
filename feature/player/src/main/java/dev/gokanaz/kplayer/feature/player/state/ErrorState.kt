package dev.gokanaz.kplayer.feature.player.state

import androidx.compose.runtime.Immutable

@Immutable
data class ErrorState(
    val currentError: PlayerError? = null,
    val isDialogVisible: Boolean = false,
    val retryCount: Int = 0,
    val timestamp: Long = 0L
) {
    companion object {
        val Initial = ErrorState()
        val Sample = ErrorState(
            currentError = PlayerError.NetworkError(
                message = "Failed to connect to server",
                canRetry = true
            ),
            isDialogVisible = true,
            retryCount = 1,
            timestamp = System.currentTimeMillis()
        )
    }
    
    fun withError(error: PlayerError, showDialog: Boolean = true): ErrorState {
        return copy(
            currentError = error,
            isDialogVisible = showDialog,
            retryCount = 0,
            timestamp = System.currentTimeMillis()
        )
    }
    
    fun withRetry(): ErrorState {
        return copy(
            retryCount = retryCount + 1,
            isDialogVisible = false,
            currentError = null
        )
    }
    
    fun withDialogDismissed(): ErrorState {
        return copy(
            isDialogVisible = false
        )
    }
    
    fun withErrorCleared(): ErrorState {
        return copy(
            currentError = null,
            isDialogVisible = false
        )
    }
    
    fun shouldRetry(): Boolean {
        return currentError?.canRetry == true && retryCount < 3
    }
    
    fun getUserFriendlyMessage(): String {
        return currentError?.getUserFriendlyMessage() ?: "Unknown error occurred"
    }
}

sealed class PlayerError(
    open val message: String,
    open val canRetry: Boolean = false,
    open val cause: Throwable? = null
) {
    data class NetworkError(
        override val message: String,
        override val canRetry: Boolean = true,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    data class DecoderError(
        override val message: String,
        override val canRetry: Boolean = true,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    data class DrmError(
        override val message: String,
        override val canRetry: Boolean = false,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    data class UnsupportedFormatError(
        override val message: String,
        override val canRetry: Boolean = false,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    data class FileNotFoundError(
        override val message: String,
        override val canRetry: Boolean = false,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    data class TimeoutError(
        override val message: String,
        override val canRetry: Boolean = true,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    data class UnknownError(
        override val message: String = "Unknown error",
        override val canRetry: Boolean = false,
        override val cause: Throwable? = null
    ) : PlayerError(message, canRetry, cause)
    
    fun getUserFriendlyMessage(): String {
        return when (this) {
            is NetworkError -> "Network connection failed. Please check your internet connection."
            is DecoderError -> "Unable to decode video format. Try switching to software decoder."
            is DrmError -> "This video is protected and cannot be played."
            is UnsupportedFormatError -> "Video format is not supported."
            is FileNotFoundError -> "Video file not found or has been moved."
            is TimeoutError -> "Connection timeout. Please try again."
            is UnknownError -> "An unexpected error occurred."
        }
    }
}
