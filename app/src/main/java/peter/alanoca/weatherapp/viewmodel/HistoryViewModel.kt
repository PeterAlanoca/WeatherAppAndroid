package peter.alanoca.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.FlowableSubscriber
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.entity.response.WeatherResponse
import peter.alanoca.weatherapp.model.repository.PlaceRepository
import peter.alanoca.weatherapp.model.repository.WeatherRepository
import peter.alanoca.weatherapp.model.repository.datasource.Resource
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getPlaces(): LiveData<Resource<List<Place>?>> {
        val liveData = MutableLiveData<Resource<List<Place>?>>()
        liveData.value = Resource.loading()
        placeRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : FlowableSubscriber<List<Place>?> {
                override fun onSubscribe(s: Subscription) {
                    s.request(Long.MAX_VALUE);
                }
                override fun onNext(t: List<Place>?) {
                    liveData.value = Resource.success(t, "success")
                }
                override fun onError(t: Throwable?) {
                    t?.message?.let {
                        liveData.value = Resource.error(it)
                    }
                }
                override fun onComplete() {
                }
            })
        return liveData
    }

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
                    }                }
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


}
