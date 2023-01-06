package peter.alanoca.weatherapp.model.entity.response

import com.google.gson.annotations.SerializedName
import peter.alanoca.weatherapp.model.entity.Weather
import peter.alanoca.weatherapp.model.entity.WeatherInfo
import java.io.Serializable

data class WeatherResponse(
    @SerializedName("cod")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("main")
    val info: WeatherInfo
) : Serializable
