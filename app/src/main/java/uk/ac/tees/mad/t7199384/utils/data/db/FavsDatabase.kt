package uk.ac.tees.mad.t7199384.utils.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorites::class], version=1, exportSchema = false)
abstract class FavsDatabase : RoomDatabase() {

    abstract val dao: FavoritesDao

    companion object{
        val TAG = FavsDatabase::class.simpleName

        @Volatile
        private var INSTANCE: FavoritesDao? = null

        fun getDaoInstance(context: Context): FavoritesDao {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = buildDatabase(context).dao
                    INSTANCE = instance
                }

                return instance
            }
        }
        private fun buildDatabase(context: Context): FavsDatabase =
            Room
                .databaseBuilder(
                    context,
                    FavsDatabase::class.java,
                    "favs.db")
                .createFromAsset("favs.db")
                .build()
    }
}