package uk.ac.tees.mad.t7199384.utils.data.classes

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.t7199384.models.api.FavoritesDao

@Database(entities = [Favorites::class], version=1)
abstract class FavsDatabase : RoomDatabase() {
    abstract fun FavoritesDao(): FavoritesDao
}