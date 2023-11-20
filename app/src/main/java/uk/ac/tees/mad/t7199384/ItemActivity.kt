package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.t7199384.models.api.Item
import uk.ac.tees.mad.t7199384.models.api.ItemAPI
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton

class ItemActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    private val BASEURL = "https://universalis.app/api/v2/"
    private val TAG: String = "CHECK_RESPONSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@ItemActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                val worldText by remember{mutableStateOf(world)}

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Box(Modifier.fillMaxSize().weight(1f)){
                            Greeting2(world)
                            //WorldChangeButton(world = "Crystal")
                        }
                        Box(modifier = Modifier.fillMaxSize().weight(0.15f)){
                            WorldChangeButton(world = worldText)
                        }
                    }
                    Row(){
                        getItem(world,4650)
                    }

            }
        }
    }
}

    private fun getItem(world: String, itemID: Int) {
        val url = BASEURL
        val api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemAPI::class.java)

        api.getItem(world,itemID.toString()).enqueue(object: Callback<Item> {
            override fun onResponse(
                call: Call<Item>,
                response: Response<Item>
            ) {
                if (response.isSuccessful){
                    Log.i(TAG, "listings received")
                    Log.i(TAG, "onResponse:${response.body()}")
                }
                else{
                    Log.i(TAG, "receive failed: ${response.code()} with $url")
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                Log.i(TAG,"onFailure: ${t.message}")
            }

        })
    }

@Composable
fun Greeting2(world: String) {
    val worldGreeting by remember{mutableStateOf(world)}

    Text(
        text = "Welcome to $worldGreeting's market!",
    )
}

@Preview(showBackground = true)


@Composable
fun GreetingPreview2() {
    val world = "Crystal"
    ICATheme {
        Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Box(Modifier.fillMaxSize().weight(1f)){
                    Greeting2(world)
                    //WorldChangeButton(world = "Crystal")
                    }
                Box(modifier = Modifier.fillMaxSize().weight(0.15f)){
                    WorldChangeButton(world = "Crystal")
                }
            }
        }
    }
}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}

