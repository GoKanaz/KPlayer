package dev.gokanaz.kplayer.crash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.gokanaz.kplayer.databinding.ActivityCrashBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CrashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCrashBinding
    
    @Inject
    lateinit var exceptionHandler: GlobalExceptionHandler
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val throwable = intent.getSerializableExtra("throwable") as? Throwable
        
        binding.errorMessage.text = throwable?.message ?: "Unknown error"
        binding.errorStack.text = throwable?.stackTraceToString() ?: "No stack trace"
        
        binding.btnRestart.setOnClickListener {
            exceptionHandler.restartApplication(this)
        }
        
        binding.btnClose.setOnClickListener {
            finishAffinity()
        }
    }
}
