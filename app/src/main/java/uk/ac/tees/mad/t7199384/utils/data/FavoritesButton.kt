package uk.ac.tees.mad.t7199384.utils.data

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import uk.ac.tees.mad.t7199384.models.api.FavoritesDao
import uk.ac.tees.mad.t7199384.utils.data.classes.FavsDatabase

@Composable
fun FavoritesButton(db:FavoritesDao,itemID: Long) {
    IconButton(onClick = {
        if(db.findById(itemID)!=null){

        }
    }) {

    }

}