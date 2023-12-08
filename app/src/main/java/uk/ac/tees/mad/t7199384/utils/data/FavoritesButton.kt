package uk.ac.tees.mad.t7199384.utils.data

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.db.FavViewModel
import uk.ac.tees.mad.t7199384.utils.data.db.Favorites
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FavoritesButton(viewModel: FavViewModel?, itemID: Long, name: String) {
    var isFavorite by remember { mutableStateOf(false) }
    val myCoroutineScope = CoroutineScope(Dispatchers.IO)

    LaunchedEffect(itemID) {
        val favorite = viewModel?.findFavoriteById(itemID)
        isFavorite = favorite != null
    }

    IconButton(onClick = {
        viewModel?.let {
            myCoroutineScope.launch {
                isFavorite = if (isFavorite) {
                    it.deleteFav(it.findFavoriteById(itemID)!!)
                    false
                } else {
                    it.insertFav(Favorites(itemName = name, itemID = itemID))
                    true
                }
            }
        }
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
    ICATheme{
        FavoritesButton(null,2,"Egg")
    }
}