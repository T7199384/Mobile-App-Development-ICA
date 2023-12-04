package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton

class FavoritesActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@FavoritesActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

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
                    }
                }
        }
    }
}
@Composable
fun FavoritesGreeting() {

    Text(
        text = "Favorites' List",
    )
}

@Preview(showBackground = true)


@Composable
fun FavoritesPreview() {
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
            }
        }
    }
}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}

