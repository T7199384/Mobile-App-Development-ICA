package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.t7199384.models.api.Comments
import uk.ac.tees.mad.t7199384.models.api.ItemAPI
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton

class ItemActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    private val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val TAG: String = "CHECK_RESPONSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        var sharedPref = this@ItemActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        var world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                var worldText by remember{mutableStateOf(world)}

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Row(Modifier.fillMaxWidth()){
                        Greeting2(worldText)
                        Spacer(Modifier.weight(1f,true).fillMaxWidth().background(MaterialTheme.colorScheme.background))
                        WorldChangeButton(world = worldText)
                    }
                    Row(){
                        getAllComments()
                    }

            }
        }
    }
}

    private fun getAllComments() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemAPI::class.java)

        api.getComments().enqueue(object: Callback<List<Comments>> {
            override fun onResponse(
                call: Call<List<Comments>>,
                response: Response<List<Comments>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let{
                        for (comment in it){
                            Log.i(TAG, "onResponse:${comment.body}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Log.i(TAG,"onFailure: ${t.message}")
            }

        })
    }

@Composable
fun Greeting2(world: String) {
    var worldGreeting by remember{mutableStateOf(world)}

    Text(
        text = "Welcome to $worldGreeting's market!",
    )
}

@Preview(showBackground = true)


@Composable
fun GreetingPreview2() {
    var world = "Crystal"
    ICATheme {
        Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
            Row(Modifier.fillMaxWidth()){
                Greeting2(world)
                Spacer(Modifier.weight(1f,true).fillMaxWidth().background(MaterialTheme.colorScheme.background))
                WorldChangeButton(world = "Crystal")
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}

