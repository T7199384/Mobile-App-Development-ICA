package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton
import uk.ac.tees.mad.t7199384.utils.data.db.FavViewModel
import uk.ac.tees.mad.t7199384.utils.data.db.Favorites

class FavoritesActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {
    init {
        app = this
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Column(
                        Modifier
                            .padding(start = 5.dp)
                            .fillMaxHeight()
                    ) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Box(
                                Modifier
                                    .height(55.dp)
                                    .weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                FavoritesGreeting()
                            }
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(.15f)
                            ) {
                                WorldChangeButton()
                            }
                        }
                        FavoritesScreen(FavViewModel(getAppContext()))
                    }
                }
        }
    }
}

    companion object {
        private lateinit var app: FavoritesActivity
        fun getAppContext() : Context = app.applicationContext
    }

    
@Composable
fun FavoritesGreeting() {
    Text(
        text = "Favorites' List",
    )
}

@Composable
fun DeleteButton(index: Long?, viewModel: FavViewModel?) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .background(
                color = Color.Red,
                shape = CircleShape.copy(all = CornerSize(5.dp))
            )
    )
    {
        IconButton(onClick = {
            viewModel?.viewModelScope?.launch {
                viewModel.deleteFav(viewModel.findFavoriteById(index!!))
                recreate()
            }
        },
            content = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            })
    }
}

@Composable
fun FavoritesScreen(viewModel: FavViewModel){

    LazyColumn(modifier=Modifier.fillMaxWidth()){
        items(viewModel.state.value) {
                FavoriteItem(it) {}
        }
    }
    if(viewModel.state.value.isEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "No favorites have been added")
        }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FavoriteItem(
        fav: Favorites,
        onSelect: (fav: Favorites) -> Unit = {}) {
        Card(
            onClick = { val intent = Intent(this@FavoritesActivity, ItemActivity::class.java)
                intent.putExtra("itemId", fav.itemID)
                intent.putExtra("itemName", fav.itemName)
                this@FavoritesActivity.startActivity(intent) }
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${fav.itemID}")
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .weight(.5f)
                        .fillMaxWidth()){
                    Text(text = "${fav.itemName}", maxLines = 2, modifier=Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.weight(.01f))
                DeleteButton(fav.itemID, FavViewModel(getAppContext()))
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            }
        }
    }

@Preview(showBackground = true)


@Composable
fun FavoritesPreview() {

    val favList = listOf(
        Favorites(1.toLong(),"Boiled Egg", 6240),
        Favorites(2.toLong(),"Leather", 30454),
        Favorites(3.toLong(),"Hard Leather", 2342),
        Favorites(4.toLong(),"Professional's Boots of Gathering", 123412),
    )

    //favList= listOf<Favorites>()

    ICATheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier
                    .padding(start = 5.dp)
                    .fillMaxHeight()
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        Modifier
                            .height(55.dp)
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        FavoritesGreeting()
                    }
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(.15f)
                    ) {
                        WorldChangeButton()
                    }
                }
                LazyColumn(modifier=Modifier.fillMaxWidth()){
                    items(favList.size) { index ->
                        val favItem = favList[index]
                        Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "${favItem.itemID}")
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(.5f)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${favItem.itemName}",
                                        maxLines = 2,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                Spacer(modifier = Modifier.weight(.01f))
                                DeleteButton(index.toLong(), null)
                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                            }
                    }
                }
                if(favList.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "No favorites have been added")
                    }
                }
            }
        }
    }
}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}

