package uk.ac.tees.mad.t7199384.models.api

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.serialization.descriptors.PrimitiveKind
import uk.ac.tees.mad.t7199384.utils.data.classes.Favorites

interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): List<Favorites>

    @Query("SELECT * FROM favorites WHERE item_id LIKE :item")
    fun findById(item: Long): Favorites

    @Insert
    fun insertAll(vararg favorites: Favorites)

    @Delete
    fun delete(favorites: Favorites)
}