package peter.alanoca.weatherapp.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import peter.alanoca.weatherapp.databinding.BottomSheetWeatherBinding
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.entity.response.WeatherResponse
import peter.alanoca.weatherapp.utility.extension.capitalizeWords
import peter.alanoca.weatherapp.utility.extension.getResource

class WeatherBottomSheet : BottomSheetDialogFragment() {

    private var weatherResponse: WeatherResponse? = null
    private var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            weatherResponse = bundle.getSerializable(KEY_WEATHER) as WeatherResponse
            place = bundle.getSerializable(KEY_PLACE) as Place
        }
        if (savedInstanceState != null) {
            weatherResponse = savedInstanceState.getSerializable(KEY_WEATHER) as WeatherResponse
            place = savedInstanceState.getSerializable(KEY_PLACE) as Place
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetWeatherBinding.inflate(inflater, container, false)
        place?.let { p ->
            binding.countryTextView.text = p.country
            binding.addressTextView.text = p.address
        }
        weatherResponse?.let { r ->
            binding.tempTextView.text = r.info.temp.toInt().toString()
            binding.tempMaxTextView.text = r.info.tempMax.toInt().toString()
            binding.tempMinTextView.text = r.info.tempMin.toInt().toString()
            r.weather.firstOrNull()?.let { weather ->
                binding.descriptionTextView.text = weather.description.capitalizeWords()
                try {
                    requireContext().getResource("ic_" + weather.icon)?.let { icon ->
                        binding.iconImageView.setImageDrawable(icon)
                    }
                } catch (_: Exception) {

                }

            }
        }
        return binding.root
    }

    companion object {

        fun newInstance(weatherResponse: WeatherResponse, place: Place): WeatherBottomSheet {
            val fragment = WeatherBottomSheet()
            val bundle = Bundle()
            bundle.putSerializable(KEY_WEATHER, weatherResponse)
            bundle.putSerializable(KEY_PLACE, place)
            fragment.arguments = bundle
            return fragment
        }

        private const val KEY_WEATHER = "KEY_WEATHER"
        private const val KEY_PLACE = "KEY_PLACE"
    }
}
