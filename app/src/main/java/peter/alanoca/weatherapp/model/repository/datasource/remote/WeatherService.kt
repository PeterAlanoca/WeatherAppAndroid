package peter.alanoca.weatherapp.model.repository.datasource.remote

import io.reactivex.Observable
import peter.alanoca.weatherapp.model.entity.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    fun getCurrentData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") apiKey: String,
    ): Observable<WeatherResponse?>
}
