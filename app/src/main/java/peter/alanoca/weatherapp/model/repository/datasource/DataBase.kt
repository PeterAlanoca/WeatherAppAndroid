package peter.alanoca.weatherapp.model.repository.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import peter.alanoca.weatherapp.model.entity.Place
import peter.alanoca.weatherapp.model.repository.datasource.local.PlaceDao

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {

    abstract fun placeDao(): PlaceDao

}