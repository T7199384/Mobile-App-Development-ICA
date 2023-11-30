package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.t7199384.models.api.Marketable
import uk.ac.tees.mad.t7199384.models.api.RateLimiter
import uk.ac.tees.mad.t7199384.models.api.RecentUpdatesAPI
import uk.ac.tees.mad.t7199384.models.api.SearchAPI
import uk.ac.tees.mad.t7199384.models.api.Update
import uk.ac.tees.mad.t7199384.models.api.fakeUpdates
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    private val BASEURL = "https://universalis.app/api/v2/extra/stats/"
    private val SEARCHURL = "https://xivapi.com/"
    private val TAG = "CHECK_RESPONSE_UPDATE"
    private val SEARCHTAG = "CHECK_SEARCH_RESPONSE_UPDATE"

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@MainActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                val worldText by remember{mutableStateOf(world)}

                val coroutineScope = rememberCoroutineScope()

                var mostUpdatedPosts:List<Marketable> by remember { mutableStateOf(emptyList()) }
                var leastUpdatedPosts:List<Marketable> by remember { mutableStateOf(emptyList()) }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val result = getUpdate(worldText,"MOST")

                        mostUpdatedPosts = result
                    }
                    onDispose {}
                }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val result = getUpdate(worldText,"LEAST")

                        leastUpdatedPosts = result
                    }
                    onDispose {}
                }

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Column (
                        Modifier
                            .padding(start = 5.dp)
                            .fillMaxHeight()){
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
                                WorldChangeButton(world = worldText)
                            }
                        }
                        UpdateView(mostUpdatedPosts,leastUpdatedPosts)
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
                    .border(3.dp, Color.DarkGray)
            ) {
                Text(
                    text = "Most Recent Updates",
                    style = TextStyle.Default.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        LazyColumn(Modifier.fillMaxHeight(.5f)) {
            items(mostUpdate.size) { index ->
                val mPost = mostUpdate[index]

                val coroutineScope = rememberCoroutineScope()

                var itemNameType:List<String> by remember { mutableStateOf(emptyList()) }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val result = searchID(mPost.itemID)

                        itemNameType = result
                    }
                    onDispose {}
                }

                if(itemNameType.isEmpty()){
                    itemNameType= listOf("If you still see this you scrolled too fast!","")
                }

                Column(modifier = Modifier.fillMaxWidth().border(1.dp, Color.DarkGray)) {
                    Column {
                        Row {
                            Text(text = "${mPost.itemID}")
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text(itemNameType[0])
                        }
                        Column {
                            Text(itemNameType[1])
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
                    }
                }
            }
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(3.dp, Color.DarkGray)
            ) {
                Text(
                    text = "Least Recent Updates",
                    style = TextStyle.Default.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        LazyColumn(Modifier.fillMaxHeight(1f)) {
            items(leastUpdate.size) { index ->
                val lPost = leastUpdate[index]

                val coroutineScope = rememberCoroutineScope()

                var itemNameType:List<String> by remember { mutableStateOf(emptyList()) }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val result = searchID(lPost.itemID)

                        itemNameType = result
                    }
                    onDispose {}
                }

                if(itemNameType.isEmpty()){
                    itemNameType= listOf("If you still see this you scrolled too fast!","")
                }

                Column(modifier = Modifier.fillMaxWidth().border(1.dp, Color.DarkGray)) {
                    Column {
                        Row {
                            Text(text = "${lPost.itemID}")
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text(itemNameType[0])
                        }
                        Column {
                            Text(itemNameType[1])
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
                    }
                }
            }
        }

    }
    private suspend fun getUpdate(world: String, s: String): List<Marketable> {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(RateLimiter())
            .build()

        val url = BASEURL
        val api = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecentUpdatesAPI::class.java)

        if(s == "MOST") {
            return suspendCoroutine { continuation ->
                api.getMostRecentUpdates(world).enqueue(object : Callback<Update> {
                    override fun onResponse(call: Call<Update>, response: Response<Update>) {
                        if (response.isSuccessful) {
                            Log.i(TAG, "listings received")
                            Log.i(TAG, "onResponse:${response.body()}")

                            val body = response.body()
                            val marketables: List<Marketable> = body?.items ?: emptyList()

                            continuation.resume(marketables)
                        } else {
                            Log.i(TAG, "receive failed: ${response.code()} with $url")
                            Log.i(TAG, "Error response: ${response.errorBody()?.string()}")
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<Update>, t: Throwable) {
                        Log.i(TAG, "onFailure: ${t.message}")
                        continuation.resume(emptyList())
                    }
                })
            }
        }
        else{
            return suspendCoroutine { continuation ->
                api.getLeastRecentUpdates(world).enqueue(object : Callback<Update> {
                    override fun onResponse(call: Call<Update>, response: Response<Update>) {
                        if (response.isSuccessful) {
                            Log.i(TAG, "listings received")
                            Log.i(TAG, "onResponse:${response.body()}")

                            val body = response.body()
                            val marketables: List<Marketable> = body?.items ?: emptyList()

                            continuation.resume(marketables)
                        } else {
                            Log.i(TAG, "receive failed: ${response.code()} with $url")
                            Log.i(TAG, "Error response: ${response.errorBody()?.string()}")
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<Update>, t: Throwable) {
                        Log.i(TAG, "onFailure: ${t.message}")
                        continuation.resume(emptyList())
                    }
                })
            }
        }
    }

    private suspend fun searchID(itemId: Long): List<String>{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val url = SEARCHURL
        val api = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchAPI::class.java)

        return suspendCoroutine { continuation ->
            api.getNameFromID(itemId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.i(SEARCHTAG, "search received")
                        Log.i(SEARCHTAG, "onResponse:${response.body()}")

                        val name:String
                        val type:String

                        val jsonObj = JSONObject(response.body()!!.string())

                        name = jsonObj.opt("Name")!!.toString()
                        val itemKindObject = jsonObj.optJSONObject("ItemKind")
                        type = itemKindObject?.opt("Name").toString()

                        val resultList = listOf<String>(name,type)

                        continuation.resume(resultList)
                    } else {
                        Log.i(SEARCHTAG, "receive failed: ${response.code()} with $url")
                        Log.i(SEARCHTAG, "Error response: ${response.errorBody()?.string()}")
                        continuation.resume(emptyList())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i(SEARCHTAG, "onFailure: ${t.message}")
                    continuation.resume(emptyList())
                }
            })
        }
    }
    @Preview(showBackground = true)


    @Composable
    fun GreetingPreview() {
        val mostUpdatedPosts=UpdatePreview()
        val leastUpdatedPosts =UpdatePreview()

        ICATheme {
            Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                Column (
                    Modifier
                        .padding(start = 5.dp)
                        .fillMaxHeight()){
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
                    UpdateViewPreview(mostUpdatedPosts,leastUpdatedPosts)
                }
            }
        }
    }

    @Composable
    fun UpdateViewPreview(mostUpdate: List<Marketable>, leastUpdate: List<Marketable>){
        Column (){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                Text(
                    text = "Most Recent Updates",
                    style = TextStyle.Default.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        LazyColumn(Modifier.fillMaxHeight(.5f)) {
            items(mostUpdate.size) { index ->
                val mPost = mostUpdate[index]

                Column(modifier = Modifier.fillMaxWidth().border(1.dp, Color.DarkGray)) {
                    Column {
                        Row {
                            Text(text = "${mPost.itemID}")
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text("Boiled Egg")
                        }
                    }
                    Column {
                        Text("Medicine & Meal")
                        Spacer(modifier = Modifier.padding(2.dp))
                    }
                }
            }
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                Text(
                    text = "Least Recent Updates",
                    style = TextStyle.Default.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
        LazyColumn(Modifier.fillMaxHeight(1f)) {
            items(leastUpdate.size) { index ->
                val mPost = leastUpdate[index]

                Column(modifier = Modifier.fillMaxWidth().border(1.dp, Color.DarkGray)) {
                    Column {
                        Row {
                            Text(text = "${mPost.itemID}")
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text("Boiled Egg")
                        }
                    }
                    Column {
                        Text("Medicine & Meal")
                        Spacer(modifier = Modifier.padding(2.dp))
                    }
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
