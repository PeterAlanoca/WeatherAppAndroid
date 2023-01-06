package peter.alanoca.weatherapp.model.repository

import io.reactivex.Observable
import peter.alanoca.weatherapp.BuildConfig
import peter.alanoca.weatherapp.model.entity.response.WeatherResponse
import peter.alanoca.weatherapp.model.repository.datasource.remote.WeatherService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {

    fun getCurrentData(latitude: Double, longitude: Double) : Observable<WeatherResponse?> {
        return weatherService.getCurrentData(latitude, longitude, "metric", "es", BuildConfig.OPEN_WEATHER_API_KEY)
    }

}
