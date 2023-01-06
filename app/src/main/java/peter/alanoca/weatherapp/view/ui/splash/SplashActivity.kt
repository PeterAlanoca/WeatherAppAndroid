package peter.alanoca.weatherapp.view.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import peter.alanoca.weatherapp.databinding.ActivitySplashBinding
import peter.alanoca.weatherapp.view.ui.menu.MenuActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(applicationContext, MenuActivity::class.java))
            finish()
        }, TIME_DELAY)

    }

    companion object {
        private const val TIME_DELAY : Long = 2500
    }

}