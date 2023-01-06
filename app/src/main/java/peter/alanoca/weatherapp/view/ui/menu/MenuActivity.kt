package peter.alanoca.weatherapp.view.ui.menu

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import peter.alanoca.weatherapp.R
import peter.alanoca.weatherapp.databinding.ActivityMenuBinding
import peter.alanoca.weatherapp.utility.extension.back
import peter.alanoca.weatherapp.utility.extension.push
import peter.alanoca.weatherapp.view.ui.history.HistoryFragment
import peter.alanoca.weatherapp.view.ui.weather.WeatherFragment

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val weatherFragment = WeatherFragment.newInstance()
        val historyFragment = HistoryFragment.newInstance()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.actionWeather -> push(weatherFragment)
                R.id.actionHistory -> push(historyFragment)
            }
            return@setOnItemSelectedListener true
        }
        binding.bottomNavigationView.selectedItemId = R.id.actionWeather
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
            val menu: Menu = binding.bottomNavigationView.menu
            val currentFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.frameLayout)
            if (currentFragment is WeatherFragment) {
                menu.getItem(0).isChecked = true
            } else if (currentFragment is HistoryFragment) {
                menu.getItem(1).isChecked = true
            }
        } else {
            finish()
        }
    }
}