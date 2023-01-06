package peter.alanoca.weatherapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import peter.alanoca.weatherapp.model.repository.PlaceRepository
import peter.alanoca.weatherapp.model.repository.WeatherRepository
import peter.alanoca.weatherapp.model.repository.datasource.local.PlaceDao
import peter.alanoca.weatherapp.model.repository.datasource.remote.WeatherService

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun providesWeatherRepository(weatherService: WeatherService) = WeatherRepository(weatherService)

    @Provides
    fun providesPlaceRepository(placeDao: PlaceDao) = PlaceRepository(placeDao)
}
