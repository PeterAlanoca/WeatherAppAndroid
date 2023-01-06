package peter.alanoca.weatherapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import peter.alanoca.weatherapp.BuildConfig
import peter.alanoca.weatherapp.model.repository.datasource.DataBase
import peter.alanoca.weatherapp.model.repository.datasource.remote.WeatherService
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(context, DataBase::class.java, BuildConfig.DATABASE_NAME).build()

    @Provides
    @Singleton
    fun providesPlaceDao(dataBase: DataBase) = dataBase.placeDao()

    @Provides
    @Singleton
    fun providesBaseUrl() = BuildConfig.OPEN_WEATHER_API_URL

    @Provides
    @Singleton
    fun providesLoggingInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(httpLoggingInterceptor)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun providesConverterFactory() : Converter.Factory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun providesRetrofitClient(okHttpClient: OkHttpClient, baseUrl: String, converterFactory: Converter.Factory) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesWeatherService(retrofit: Retrofit) : WeatherService = retrofit.create(WeatherService::class.java)

}