package uk.ac.tees.mad.t7199384.utils.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): List<Favorites>

    @Query("SELECT * FROM favorites WHERE item_id LIKE :item")
    fun findById(item: Long): Favorites

    @Query("SELECT * FROM favorites WHERE uid LIKE :item")
    fun findByIndex(item: Long): Favorites

    @Insert
    fun insertAll(vararg favorites: Favorites)

    @Delete
    fun delete(favorites: Favorites)
}