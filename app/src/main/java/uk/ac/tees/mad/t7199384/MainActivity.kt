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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.t7199384.models.api.Marketable
import uk.ac.tees.mad.t7199384.models.api.fakeUpdates
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton

class MainActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@MainActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                val worldText by remember{mutableStateOf(world)}

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Column (Modifier.padding(start = 5.dp)){
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            Box(
                                Modifier
                                    .height(55.dp)
                                    .weight(1f),
                                contentAlignment= Alignment.CenterStart
                            ) {
                                Greeting(worldText)
                            }
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(.15f)
                            ) {
                                WorldChangeButton(worldText)
                            }
                        }
                        //QualityButton(false)
                        //Listing("Jenova", fakeData2, false)
                    }
                }
        }
    }
}

@Composable
fun Greeting(world: String) {
    val worldGreeting by remember{mutableStateOf(world)}

    Text(
        text = "Welcome to $worldGreeting's market!",
        style = TextStyle.Default.copy(fontSize=18.sp, fontWeight = FontWeight.ExtraBold)
    )
}

@Composable
fun UpdateView(mostUpdate: List<Marketable>, leastUpdate: List<Marketable>){
        Column (){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Most Recent Updates",
                    style = TextStyle.Default.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        LazyColumn(Modifier.fillMaxHeight(.5f)) {
            items(mostUpdate.size) { index ->
                val mPost = mostUpdate[index]

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text(text = "${mPost.itemID}")
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text("Item Name")
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text("Item Type")
                    }
                }
            }
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Least Recent Updates",
                    style = TextStyle.Default.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        LazyColumn(Modifier.fillMaxHeight(1f)) {
            items(leastUpdate.size) { index ->
                val mPost = leastUpdate[index]

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text(text = "${mPost.itemID}")
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text("Item Name")
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text("Item Type")
                    }
                }
            }
        }

}
/*
fun mostRecent (world: String): List<Marketable>{
    val mostRecentPosts = List<Marketable> = null
    return mostRecentPosts
}*/
@Preview(showBackground = true)


@Composable
fun GreetingPreview() {
    val mostUpdatedPosts=UpdatePreview()
    val leastUpdatedPosts =UpdatePreview()

    ICATheme {
        Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
            Column (Modifier.padding(start = 5.dp).fillMaxHeight()){
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        Modifier
                            .height(55.dp)
                            .weight(1f),
                        contentAlignment= Alignment.CenterStart
                    ) {
                        Greeting("Crystal")
                    }
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(.15f)
                    ) {
                        WorldChangeButton(world = "Crystal")
                    }
                }
                UpdateView(mostUpdatedPosts,leastUpdatedPosts)
            }
        }
    }
}

    fun UpdatePreview(): List<Marketable>{
        return fakeUpdates()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}


