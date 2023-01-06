package peter.alanoca.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.entity.response.WeatherResponse
import peter.alanoca.weatherapp.model.repository.PlaceRepository
import peter.alanoca.weatherapp.model.repository.WeatherRepository
import peter.alanoca.weatherapp.model.repository.datasource.Resource
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    fun getCurrentData(latitude: Double, longitude: Double): LiveData<Resource<WeatherResponse?>> {
        val liveData = MutableLiveData<Resource<WeatherResponse?>>()
        liveData.value = Resource.loading()
        weatherRepository.getCurrentData(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WeatherResponse?> {
                override fun onNext(t: WeatherResponse) {
                    if (t.code == 200) {
                        liveData.value = Resource.success(t, "success")
                    } else {
                        liveData.value = Resource.error(t.message)
                    }
                }
                override fun onError(e: Throwable) {
                    e.message?.let {
                        liveData.value = Resource.error(it)
                    }
                }
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
            })
        return liveData
    }

    fun insertPlace(place: Place): LiveData<Resource<Long?>> {
        val liveData = MutableLiveData<Resource<Long?>>()
        liveData.value = Resource.loading()
        placeRepository.insert(place)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Long?>() {
                override fun onSuccess(t: Long) {
                    liveData.value = Resource.success(t, "success")
                }
                override fun onError(e: Throwable) {
                    e.message?.let {
                        liveData.value = Resource.error(it)
                    }
                }
            })
        return liveData
    }

}
