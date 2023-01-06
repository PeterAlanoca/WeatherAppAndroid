package peter.alanoca.weatherapp.model.repository

import io.reactivex.Flowable
import io.reactivex.Single
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.repository.datasource.local.PlaceDao
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeDao: PlaceDao) {

    fun getAll(): Flowable<List<Place>?> {
        return placeDao.getAll()
    }

    fun insert(place: Place): Single<Long?> {
        return placeDao.insert(place)
    }

}
