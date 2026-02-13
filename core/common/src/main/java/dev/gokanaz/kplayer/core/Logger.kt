package dev.gokanaz.kplayer.core

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger @Inject constructor() {
    private var isDebugMode = true

    fun d(message: String, tag: String? = null) {
        if (isDebugMode) {
            tag?.let { Timber.tag(it) } ?: Timber.d(message)
        }
    }

    fun i(message: String, tag: String? = null) {
        if (isDebugMode) {
            tag?.let { Timber.tag(it) } ?: Timber.i(message)
        }
    }

    fun w(message: String, tag: String? = null) {
        tag?.let { Timber.tag(it) } ?: Timber.w(message)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String? = null) {
        tag?.let { Timber.tag(it) }
        throwable?.let { Timber.e(it, message) } ?: Timber.e(message)
    }

    fun wtf(message: String, throwable: Throwable? = null, tag: String? = null) {
        tag?.let { Timber.tag(it) }
        throwable?.let { Timber.wtf(it, message) } ?: Timber.wtf(message)
    }

    fun setDebugMode(enabled: Boolean) {
        isDebugMode = enabled
        if (enabled) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
