package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.t7199384.models.api.Item
import uk.ac.tees.mad.t7199384.models.api.ItemAPI
import uk.ac.tees.mad.t7199384.models.api.Listing
import uk.ac.tees.mad.t7199384.models.api.ListingTypeAdapter
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

import uk.ac.tees.mad.t7199384.models.api.fakeWorldData

class ItemActivity : ComponentActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val BASEURL: String = "https://universalis.app/api/v2/"
    private val TAG: String = "CHECK_RESPONSE_ITEM"

    var hqflag: Boolean by mutableStateOf(true)
        private set

    val fakeWorldData: Item = fakeWorldData()

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@ItemActivity.getSharedPreferences(
            getString(R.string.world_file_key),
            Context.MODE_PRIVATE
        )
        val world = sharedPref.getString("world", "Empty").toString()

        val itemId=intent.getLongExtra("itemId",-1L)
        val itemName=intent.getStringExtra("itemName") ?:"noValue"

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                val worldText by remember { mutableStateOf(world) }
                val hqState = rememberUpdatedState(newValue = (hqflag))

                                val coroutineScope = rememberCoroutineScope()

                                // Use remember to hold the state of your data
                                var data by remember { mutableStateOf<Item?>(null) }

                                // Trigger the item fetch when needed (e.g., on composition)
                                DisposableEffect(key1 = Unit) {
                                    coroutineScope.launch {
                                        val result = getItem(world, itemId)

                                        data = result
                                    }
                                    onDispose {}
                                }

                //var data = fakeWorldData
                if (data == null) {
                    CircularProgressIndicator()
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    Modifier
                                        .height(55.dp)
                                        .weight(1f),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    val cheapestListing = cheapestListing(data!!)
                                    ItemDesc(
                                        itemName,
                                        itemId,
                                        cheapestListing[0],
                                        cheapestListing[1],
                                        cheapestListing[2],
                                        cheapestListing[3]
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .weight(.15f)
                                ) {
                                    WorldChangeButton()
                                }
                            }
                            QualityButton(hqState.value)
                            Listing(worldText, data!!, hqState.value)
                        }
                    }
                }
            }
        }
    }
    override fun onBackPressed() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun cheapestListing(listingsData: Item): List<Int?> {
        var cheapHQ: Int? = null
        var quantityHQ: Int? = null
        var cheapNQ: Int? = null
        var quantityNQ: Int? = null

        for (listing in listingsData.listings) {
            when (listing) {
                is Listing.NormalListing -> {
                    val currentPrice = listing.pricePerUnit
                    if (cheapHQ == null || currentPrice * listing.quantity < cheapHQ * quantityHQ!! && listing.hq) {
                        cheapHQ = listing.pricePerUnit
                        quantityHQ = listing.quantity
                    }
                    if (cheapNQ == null || currentPrice * listing.quantity < cheapHQ * quantityHQ && !listing.hq) {
                        cheapNQ = listing.pricePerUnit
                        quantityNQ = listing.quantity
                    }
                }

                is Listing.WorldListing -> {

                    val currentPrice = listing.pricePerUnit
                    if (cheapHQ == null || currentPrice * listing.quantity < cheapHQ * quantityHQ!! && listing.hq) {
                        cheapHQ = listing.pricePerUnit
                        quantityHQ = listing.quantity
                    }
                    if (cheapNQ == null || currentPrice * listing.quantity < cheapHQ * quantityNQ!! && !listing.hq) {
                        cheapNQ = listing.pricePerUnit
                        quantityNQ = listing.quantity
                    }
                }
            }
        }
        return listOf(cheapHQ, quantityHQ, cheapNQ, quantityNQ)
    }

    private suspend fun getItem(world: String, itemID: Long): Item? {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        val url = BASEURL

        val gson = GsonBuilder()
            .registerTypeAdapter(Listing::class.java, ListingTypeAdapter())
            .create()
        val api = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ItemAPI::class.java)

        return suspendCoroutine { continuation ->
            api.getItem(world, itemID.toString()).enqueue(object : Callback<Item?> {
                override fun onResponse(call: Call<Item?>, response: Response<Item?>) {
                    if (response.isSuccessful) {
                        Log.i(TAG, "listings received")
                        Log.i(TAG, "onResponse:${response.body()}")

                        val item: Item? = response.body()

                        continuation.resume(item)
                    } else {
                        Log.i(TAG, "receive failed: ${response.code()} with $url")
                        Log.i(TAG, "Error response: ${response.errorBody()?.string()}")
                        continuation.resume(null)
                    }
                }

                override fun onFailure(call: Call<Item?>, t: Throwable) {
                    Log.i(TAG, "onFailure: ${t.message}")
                    continuation.resume(null)
                }
            })
        }
    }

    @Composable
    fun ItemDesc(
        name: String,
        id: Long,
        priceHQ: Int?,
        quantityHQ: Int?,
        priceNQ: Int?,
        quantityNQ: Int?
    ) {
        val totalHQ = priceHQ?.times(quantityHQ!!)
        val totalNQ = priceNQ?.times(quantityNQ!!)

        Column(Modifier.padding(start = 5.dp)) {
            Text(
                text = "$name       $id"
                //       "$priceHQ X $quantityHQ - $totalHQ        NQ: $priceNQ X $quantityNQ - $totalNQ",
            )
            Row {
                Column {
                    Text(text = "Cheapest HQ", style = TextStyle.Default.copy(fontSize = 10.sp))
                    Text(
                        text = "${priceHQ}G X $quantityHQ Total: ${totalHQ}G",
                        style = TextStyle.Default.copy(fontSize = 14.sp)
                    )
                }
                Spacer(modifier = Modifier.padding(start = 35.dp))
                Column {
                    Text(text = "Cheapest NQ", style = TextStyle.Default.copy(fontSize = 10.sp))
                    Text(
                        text = "${priceNQ}G X $quantityNQ Total: ${totalNQ}G",
                        style = TextStyle.Default.copy(fontSize = 14.sp)
                    )
                }
            }
        }
    }

    @Composable
    fun QualityButton(boolean: Boolean) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { qualityChange(true) },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (boolean) Color.Gray else Color.Transparent)
                )
                { Text("High Quality") }
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { qualityChange(false) },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (!boolean) Color.Gray else Color.Transparent)
                )
                { Text("Normal Quality") }
            }
        }
    }

    private fun qualityChange(boolean: Boolean) {
        hqflag = boolean
    }

    @Composable
    fun Listing(world: String, data: Item, hqflag: Boolean) {

        var averagePrice = data.averagePrice

        var listingsHQ: List<Listing> = emptyList()
        var listingsNQ: List<Listing> = emptyList()

        for (item in data.listings) {
            when (item) {
                is Listing.NormalListing -> {
                    if (item.hq) {
                        listingsHQ = listingsHQ.plus(item)
                    } else {
                        listingsNQ = listingsNQ.plus(item)
                    }
                }

                is Listing.WorldListing -> {
                    if (item.hq) {
                        listingsHQ = listingsHQ.plus(item)
                        averagePrice = data.averagePriceHQ
                    } else {
                        listingsNQ = listingsNQ.plus(item)
                        averagePrice = data.averagePriceNQ
                    }
                }
            }
        }

        var viewListings: List<Listing> = listingsNQ

        if (hqflag) {
            viewListings = listingsHQ
        }

        Row {
            LazyColumn {
                items(viewListings.size) { index ->
                    val listing = viewListings[index]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp)
                            .border(1.dp, Color.DarkGray)
                    )
                    {
                        Spacer(modifier = Modifier.padding(1.dp))
                        when (listing) {
                            is Listing.NormalListing -> {
                                val diff = ((listing.pricePerUnit - averagePrice) / averagePrice) * 100.0
                                val diffText = String.format(
                                    "%.2f",
                                    (listing.pricePerUnit - averagePrice) / averagePrice * 100.0
                                )
                                Row{
                                    Text(text = "$world")
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = "${listing.pricePerUnit}G X ${listing.quantity}")
                                    Spacer(modifier = Modifier.padding(5.dp))
                                }
                                Row{
                                    Text(text = "Total: ${listing.total}G")
                                }
                                Row {
                                    val textColor = if (diff > 0.0) Color.Red else Color.Green
                                    Text("$diffText%", color=textColor)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text="${listing.retainerName}")
                                }
                            }

                            is Listing.WorldListing -> {
                                val diff = ((listing.pricePerUnit - averagePrice) / averagePrice) * 100.0
                                val diffText = String.format(
                                    "%.2f",
                                    (listing.pricePerUnit - averagePrice) / averagePrice * 100.0
                                )
                                Row{
                                    Text(text = "${listing.worldName}")
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text = "${listing.pricePerUnit}G X ${listing.quantity}")
                                    Spacer(modifier = Modifier.padding(5.dp))
                                }
                                Row{
                                    Text(text = "Total: ${listing.total}G")
                                }
                                Row {
                                    val textColor = if (diff > 0.0) Color.Red else Color.Green
                                    Text("$diffText%", color=textColor)
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    Text(text="${listing.retainerName}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Preview(showBackground = true)


    @Composable
    fun ItemPreview() {
        ICATheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Box(
                            Modifier
                                .height(55.dp)
                                .weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            ItemDesc("Boiled Egg", 4650, 5, 80, 2, 40)
                        }
                        Box(
                            modifier = Modifier
                                .height(50.dp)
                                .weight(.15f)
                        ) {
                            WorldChangeButton()
                        }
                    }
                    QualityButton(false)
                    Listing("Jenova", fakeWorldData, false)
                }
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}



