package peter.alanoca.weatherapp.model.repository.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single
import peter.alanoca.weatherapp.model.entity.Place

@Dao
interface PlaceDao {

    @Query("SELECT * FROM Place ORDER BY id DESC LIMIT 30")
    fun getAll(): Flowable<List<Place>?>

    @Insert
    fun insert(place: Place): Single<Long?>

}