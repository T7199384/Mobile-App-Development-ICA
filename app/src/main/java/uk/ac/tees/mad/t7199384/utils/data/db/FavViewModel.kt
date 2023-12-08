package uk.ac.tees.mad.t7199384.utils.data.db

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavViewModel(appContext: Context) : ViewModel() {
    val state = mutableStateOf(emptyList<Favorites>())

    private var favDao = FavsDatabase.getDaoInstance(appContext)

    init {
        getFavs()
    }

    private suspend fun getAllFavs() : List<Favorites> {
        return withContext(Dispatchers.IO) {
            return@withContext favDao.getAll()
        }
    }

    private fun getFavs() {
        viewModelScope.launch {
            val favs = getAllFavs()

            favs.forEach { Log.d(TAG, "----> ${it.itemID}\t${it.itemName}") }

            state.value = favs
        }
    }

    suspend fun insertFav(favorite: Favorites){
        withContext(Dispatchers.IO) {
            favDao.insertAll(favorite)
        }
    }

    suspend fun deleteFav(favorite: Favorites){
        withContext(Dispatchers.IO) {
            return@withContext favDao.delete(favorite)
        }
    }

    suspend fun findFavoriteById(itemId: Long): Favorites? {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            favDao.findById(itemId)
        }
    }


    companion object {
        val TAG = FavViewModel::class.simpleName
    }
}