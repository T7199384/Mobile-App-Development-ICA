package uk.ac.tees.mad.t7199384.utils.data

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.classes.Favorites
import uk.ac.tees.mad.t7199384.utils.data.classes.FavsDatabase

@Composable
fun FavoritesButton(db: FavsDatabase?, itemID: Long, name: String) {
    var isFavorite :Boolean = db?.FavoritesDao()?.findById(itemID) ?: null  != null


    IconButton(onClick = {
        isFavorite = if(isFavorite){
            db?.FavoritesDao()?.delete(db.FavoritesDao().findById(itemID)); false
        } else {db?.FavoritesDao()?.insertAll(Favorites(itemName=name, itemID = itemID)); true }
    }) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                "Favorite",
                tint= if (isFavorite) Color.Red else Color.DarkGray,
                modifier= Modifier.clip(CircleShape))
        }

}

@Preview(showBackground = true)
@Composable
fun FavoriteButtonPreview() {
    ICATheme() {
        FavoritesButton(null,2,"Egg")
    }
}