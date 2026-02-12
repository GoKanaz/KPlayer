package dev.gokanaz.kplayer.crash

import android.app.Activity
import android.content.Intent
import android.os.Process
import dev.gokanaz.kplayer.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler @Inject constructor() : Thread.UncaughtExceptionHandler {
    
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var currentActivity: Activity? = null
    
    fun init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
    
    fun setCurrentActivity(activity: Activity) {
        currentActivity = activity
    }
    
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            currentActivity?.let { activity ->
                val intent = Intent(activity, CrashActivity::class.java).apply {
                    putExtra("throwable", throwable)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                activity.startActivity(intent)
                activity.finishAffinity()
            }
        } catch (e: Exception) {
            defaultHandler?.uncaughtException(thread, throwable)
        } finally {
            Process.killProcess(Process.myPid())
            System.exit(1)
        }
    }
    
    fun restartApplication(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        activity.startActivity(intent)
        activity.finishAffinity()
    }
}
