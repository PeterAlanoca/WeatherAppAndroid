package peter.alanoca.weatherapp.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherInfo(
    @SerializedName("temp")
    val temp: Float,
    @SerializedName("feels_like")
    val feelsLike: Float,
    @SerializedName("temp_min")
    val tempMin: Float,
    @SerializedName("temp_max")
    val tempMax: Float,
    @SerializedName("pressure")
    val pressure: Float,
    @SerializedName("humidity")
    val humidity: Float
) : Serializable
