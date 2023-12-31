package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
import uk.ac.tees.mad.t7199384.utils.data.classes.Marketable
import uk.ac.tees.mad.t7199384.models.api.RateLimiter
import uk.ac.tees.mad.t7199384.models.api.RecentUpdatesAPI
import uk.ac.tees.mad.t7199384.models.api.SearchAPI
import uk.ac.tees.mad.t7199384.models.api.taxAPI
import uk.ac.tees.mad.t7199384.utils.data.classes.Update
import uk.ac.tees.mad.t7199384.utils.data.classes.fakeUpdates
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    private val baseURL = "https://universalis.app/api/v2/extra/stats/"
    private val searchURL = "https://xivapi.com/"
    private val taxURL = "https://universalis.app/api/v2/"
    private val tag = "CHECK_RESPONSE_UPDATE"
    private val searchTag = "CHECK_SEARCH_RESPONSE_UPDATE"
    private val taxTag = "CHECK_TAX_RESPONSE_UPDATE"

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

                var taxes:Map<String,Any> by remember { mutableStateOf(emptyMap()) }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val taxesResult = getTaxeList(worldText)

                        taxes = taxesResult
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
                                    .height(85.dp)
                                    .weight(1f),
                                contentAlignment= Alignment.CenterStart
                            ) {
                                Greeting(worldText,taxes)
                            }
                            Box(
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(.15f)
                            ) {
                                WorldChangeButton()
                            }
                        }
                        TextButton(
                            onClick = {
                                val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
                                startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Go to Favorites")
                        }
                        UpdateView(mostUpdatedPosts,leastUpdatedPosts)
                    }
                }
            }
        }
    }

    private suspend fun getTaxeList(world: String) :Map<String, Any>{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(RateLimiter())
            .build()

        val url = taxURL
        val api = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(taxAPI::class.java)

        return suspendCoroutine { continuation ->
                api.getTaxes(world).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Log.i(tag, "listings received")
                            Log.i(tag, "onResponse:${response.body()}")

                            val body = response.body()
                            val taxMap: Map<String, Any> = Gson().fromJson(
                                body?.charStream(),
                                object : TypeToken<Map<String, Any>>() {}.type
                            )

                            continuation.resume(taxMap)
                        } else {
                            Log.i(taxTag, "receive failed: ${response.code()} with $url")
                            Log.i(taxTag, "Error response: ${response.errorBody()?.string()}")
                            continuation.resume(emptyMap())
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.i(taxTag, "onFailure: ${t.message}")
                        continuation.resume(emptyMap())
                    }
                })
            }
    }

    @Composable
    fun Greeting(world: String, taxes: Map<String, Any>) {
        val worldGreeting by remember { mutableStateOf(world) }

        Column {
            Text(
                text = "Welcome to $worldGreeting's market!",
                style = TextStyle.Default.copy(fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            )

            if (taxes.isNotEmpty()) {
                val limsaValue = (taxes["Limsa Lominsa"] as? Number)?.toInt()
                val gridaniaValue = (taxes["Gridania"] as? Number)?.toInt()
                val uldahValue = (taxes["Ul'dah"] as? Number)?.toInt()
                val ishgardValue = (taxes["Ishgard"] as? Number)?.toInt()
                val kuganeValue = (taxes["Kugane"] as? Number)?.toInt()
                val crystariumValue = (taxes["Crystarium"] as? Number)?.toInt()
                val sharlayanValue = (taxes["Old Sharlayan"] as? Number)?.toInt()

                Text(text = "Limsa: ${limsaValue}%  Gridania: ${gridaniaValue}%  Ul'Dah: ${uldahValue}%",
                    style = TextStyle.Default.copy(fontSize=15.sp))
                Text(text = "Ishgard: ${ishgardValue}% Kugane: ${kuganeValue}%",
                    style = TextStyle.Default.copy(fontSize=15.sp))
                Text(text = "Crystarium: ${crystariumValue}%  Sharlayan: ${sharlayanValue}%",
                    style = TextStyle.Default.copy(fontSize=15.sp))
            }
        }
    }

    @Composable
    fun UpdateView(mostUpdate: List<Marketable>, leastUpdate: List<Marketable>) {
        val context = LocalContext.current

        Column {
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
        LazyColumn(
            Modifier.fillMaxHeight(.5f)
        ) {
            items(mostUpdate.size) { index ->
                val mPost = mostUpdate[index]

                val coroutineScope = rememberCoroutineScope()

                var itemNameType: List<String> by remember { mutableStateOf(emptyList()) }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val result = searchID(mPost.itemID)

                        itemNameType = result
                    }
                    onDispose {}
                }

                if (itemNameType.isEmpty()) {
                    itemNameType = listOf("LOADING", "")
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.DarkGray)
                    .clickable {
                        val intent = Intent(context, ItemActivity::class.java)
                        intent.putExtra("itemId", mPost.itemID)
                        intent.putExtra("itemName", itemNameType[0])
                        context.startActivity(intent)
                    }

                ) {
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
        LazyColumn(Modifier.fillMaxHeight(.83f)) {
            items(leastUpdate.size) { index ->
                val lPost = leastUpdate[index]

                val coroutineScope = rememberCoroutineScope()

                var itemNameType: List<String> by remember { mutableStateOf(emptyList()) }

                DisposableEffect(key1 = Unit) {
                    coroutineScope.launch {
                        val result = searchID(lPost.itemID)

                        itemNameType = result
                    }
                    onDispose {}
                }

                if (itemNameType.isEmpty()) {
                    itemNameType = listOf("LOADING", "")
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.DarkGray)
                    .clickable {
                        val intent = Intent(context, ItemActivity::class.java)
                        intent.putExtra("itemId", lPost.itemID)
                        intent.putExtra("itemName", itemNameType[0])
                        context.startActivity(intent)
                    }
                ) {
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
        SearchBar()
    }

    private suspend fun getUpdate(world: String, s: String): List<Marketable> {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(RateLimiter())
            .build()

        val url = baseURL
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
                            Log.i(tag, "listings received")
                            Log.i(tag, "onResponse:${response.body()}")

                            val body = response.body()
                            val marketables: List<Marketable> = body?.items ?: emptyList()

                            continuation.resume(marketables)
                        } else {
                            Log.i(tag, "receive failed: ${response.code()} with $url")
                            Log.i(tag, "Error response: ${response.errorBody()?.string()}")
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<Update>, t: Throwable) {
                        Log.i(tag, "onFailure: ${t.message}")
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
                            Log.i(tag, "listings received")
                            Log.i(tag, "onResponse:${response.body()}")

                            val body = response.body()
                            val marketables: List<Marketable> = body?.items ?: emptyList()

                            continuation.resume(marketables)
                        } else {
                            Log.i(tag, "receive failed: ${response.code()} with $url")
                            Log.i(tag, "Error response: ${response.errorBody()?.string()}")
                            continuation.resume(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<Update>, t: Throwable) {
                        Log.i(tag, "onFailure: ${t.message}")
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

        val url = searchURL
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
                        Log.i(searchTag, "search received")
                        Log.i(searchTag, "onResponse:${response.body()}")

                        val name:String
                        val type:String

                        val jsonObj = JSONObject(response.body()!!.string())

                        name = jsonObj.opt("Name")!!.toString()
                        val itemKindObject = jsonObj.optJSONObject("ItemKind")
                        type = itemKindObject?.opt("Name").toString()

                        val resultList = listOf(name,type)

                        continuation.resume(resultList)
                    } else {
                        Log.i(searchTag, "receive failed: ${response.code()} with $url")
                        Log.i(searchTag, "Error response: ${response.errorBody()?.string()}")
                        continuation.resume(emptyList())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.i(searchTag, "onFailure: ${t.message}")
                    continuation.resume(emptyList())
                }
            })
        }
    }

    private fun searchName(item: String, pageNumber: Int=1, resultsPerPage: Int=100): List<String> {
        var searchedItem=item.trim()
        searchedItem = searchedItem.split(" ").joinToString(" ") { it -> it.replaceFirstChar { it.uppercase() } }
        val resultsList = mutableListOf<String>()
        var currentPage = pageNumber
        var totalPages = 0

        try {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            val url = searchURL
            val api = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SearchAPI::class.java)

            do {
                val call = api.getIDFromName(searchedItem, currentPage, resultsPerPage)
                val response = call.execute()
                if (response.isSuccessful) {

                    val jsonObj = JSONObject(response.body()!!.string())

                    val resultsArray = jsonObj.getJSONArray("Results")
                    if (resultsArray.length() > 0) {
                        for (index in 0 until resultsArray.length()) {
                            val itemResult = resultsArray.getJSONObject(index)
                            val resultName = itemResult.getString("Name")


                            if (resultName == searchedItem) {
                                val id = itemResult.getString("ID")
                                resultsList.add(id)
                                resultsList.add(resultName)
                            }
                        }
                    }

                    totalPages = jsonObj.getJSONObject("Pagination").getInt("PageTotal")
                    currentPage++

                } else {
                    Log.i(searchTag, "receive failed: ${response.code()} with $url")
                    Log.i(searchTag, "Error response: ${response.errorBody()?.string()}")
                }
            } while (currentPage <= totalPages && resultsList.isEmpty())

        } catch (e: Exception) {
            Log.i(searchTag, "Exception: ${e.message}")
        }

        return resultsList
    }

    private fun searchFunction(searchText: String, context: Context){
        val coroutineSearchScope = CoroutineScope(Dispatchers.Default)
        var itemList:List<String>

        coroutineSearchScope.launch {
            itemList = searchName(searchText)

            var itemId = 0L

            try {
                if (itemList[0] != " ") {
                    itemId = itemList[0].toLong()
                }
                val itemName = itemList[1]

                if (itemList.isNotEmpty()) {
                    val intent = Intent(context, ItemActivity::class.java)
                    intent.putExtra("itemId", itemId)
                    intent.putExtra("itemName", itemName)
                    context.startActivity(intent)
                } else {
                    runOnUiThread {
                        Toast.makeText(context, "item not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IndexOutOfBoundsException) {
                runOnUiThread {
                    Toast.makeText(context, "item not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar()
    {
        var searchText by remember { mutableStateOf("Search") }
        var isFocused by remember { mutableStateOf(false) }

        Column(modifier=Modifier.fillMaxWidth()){
            Box(modifier= Modifier
                .weight(.7f)
                .fillMaxWidth())
            {
                TextField(value = searchText,
                    onValueChange = {searchText=it},
                    modifier= Modifier
                        .background(Color.Black)
                        .border(1.dp, Color.Yellow)
                        .fillMaxWidth()
                        .height(50.dp)
                        .onFocusChanged {
                            isFocused = it.isFocused
                            if (isFocused) {
                                searchText = ""
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone={
                            searchFunction(searchText, this@MainActivity)
                        }
                    )
                )
            }
        }
    }

    @Preview(showBackground = true)

    @Composable
    fun GreetingPreview() {
        val mostUpdatedPosts=updatePreview()
        val leastUpdatedPosts =updatePreview()

        val taxMap: Map<String, Any> = mapOf(
            "Limsa Lominsa" to 5,
            "Gridania" to 5,
            "Ul'dah" to 5,
            "Ishgard" to 3,
            "Kugane" to 3,
            "Crystarium" to 3,
            "Old Sharlayan" to 3
        )

        ICATheme {
            Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                Column (
                    Modifier
                        .padding(start = 5.dp)
                        .fillMaxHeight()){
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .height(85.dp)
                                .weight(1f),
                            contentAlignment= Alignment.CenterStart
                        ) {
                            Greeting("Crystal",taxMap)
                        }
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .weight(.15f)
                        ) {
                            WorldChangeButton()
                        }
                    }
                    TextButton(
                        onClick = {
                            val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                    ) {
                        Text("Go to Favorites",style = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                    }
                    UpdateViewPreview(mostUpdatedPosts,leastUpdatedPosts)
                }
            }
        }
    }

    @Composable
    fun UpdateViewPreview(mostUpdate: List<Marketable>, leastUpdate: List<Marketable>){
        Column {
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

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.DarkGray)) {
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
        LazyColumn(Modifier.fillMaxHeight(.83f)) {
            items(leastUpdate.size) { index ->
                val mPost = leastUpdate[index]

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.DarkGray)) {
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
        SearchBar()
    }

    private fun updatePreview(): List<Marketable>{
        return fakeUpdates()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}
