package uk.ac.tees.mad.t7199384

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.t7199384.models.api.Item
import uk.ac.tees.mad.t7199384.models.api.ItemAPI
import uk.ac.tees.mad.t7199384.models.api.Listing
import uk.ac.tees.mad.t7199384.ui.theme.ICATheme
import uk.ac.tees.mad.t7199384.utils.data.WorldChangeButton
import kotlin.coroutines.suspendCoroutine

class ItemActivity : ComponentActivity(),SharedPreferences.OnSharedPreferenceChangeListener {

    private val BASEURL = "https://universalis.app/api/v2/"
    private val TAG: String = "CHECK_RESPONSE"

    val fakeData = Item(
        itemID = 4650,
        worldID = 40,
        lastUploadTime = 1700361722705,
        averagePrice=469.79114,
        averagePriceNQ=444.14285,
        averagePriceHQ=312.1,
        listings = listOf(
            Listing.NormalListing(
                lastReviewTime=1700352444,
                pricePerUnit=105,
                quantity=99,
                stainID=0,
                creatorName="",
                creatorID=null,
                hq=false,
                isCrafted=false,
                listingID="ddcb00c174615b0615ae8efdce3ee8b589945aa7fccaeb9e3864be65be125295",
                materia=listOf(),
                onMannequin=false,
                retainerCity=4,
                retainerID="b0022597232ed037aa0bad921d0ef91cc8933fdd04d2df6879777610b2e58dbc",
                retainerName="ShrekOfCabbages",
                sellerID="1a5859104a70752007eeebeff93c502dfb639ff5f9edc158b4305ee3c16b2009",
                total=10395),
            Listing.NormalListing(
                lastReviewTime=1700352444,
                pricePerUnit=151,
                quantity=10,
                stainID=0,
                creatorName="",
                creatorID=null,
                hq=true,
                isCrafted=false,
                listingID="ddcb00c174615b0615ae8efdce3ee8b589945aa7fccaeb9e3864be65be125295",
                materia=listOf(),
                onMannequin=false,
                retainerCity=4,
                retainerID="b0022597232ed037aa0bad921d0ef91cc8933fdd04d2df6879777610b2e58dbc",
                retainerName="SoupSaiyan",
                sellerID="1a5859104a70752007eeebeff93c502dfb639ff5f9edc158b4305ee3c16b2009",
                total=1510),
            Listing.NormalListing(
                lastReviewTime=1700352444,
                pricePerUnit=200,
                quantity=99,
                stainID=0,
                creatorName="",
                creatorID=null,
                hq=false,
                isCrafted=false,
                listingID="ddcb00c174615b0615ae8efdce3ee8b589945aa7fccaeb9e3864be65be125295",
                materia=listOf(),
                onMannequin=false,
                retainerCity=4,
                retainerID="b0022597232ed037aa0bad921d0ef91cc8933fdd04d2df6879777610b2e58dbc",
                retainerName="AFrixFuzzer",
                sellerID="1a5859104a70752007eeebeff93c502dfb639ff5f9edc158b4305ee3c16b2009",
                total=19800)
        )
    )

    val fakeData2 = Item(
        itemID = 4650,
        lastUploadTime = 1700361722705,
        averagePrice=469.79114,
        averagePriceNQ=444.14285,
        averagePriceHQ=312.1,
        listings = listOf(
            Listing.WorldListing(
                lastReviewTime = 1700693363,
                pricePerUnit = 2,
                quantity = 1,
                stainID = 0,
                worldName = "Brynhildr",
                worldID = 34,
                creatorName = "",
                creatorID = null,
                hq = false,
                isCrafted = false,
                listingID = "a78696485b716926e8d191f1ca0175548da397cb19f9ea4bc9c03c79b90b54e6",
                materia = emptyList(),
                onMannequin = false,
                retainerCity = 1,
                retainerID = "2e31d9b5281db024fa996174bd8b76f498ddabdd564a17a54ac1b63ab36d7a73",
                retainerName = "M'liko",
                sellerID = "d0315724e23275dabaa3a2efb0e208ff37dbf07df83f24c07e601d5ce5f9a60b",
                total = 2
            ),
            Listing.WorldListing(
                lastReviewTime = 1700677096,
                pricePerUnit = 2,
                quantity = 1,
                stainID = 0,
                worldName = "Brynhildr",
                worldID = 34,
                creatorName = "",
                creatorID = "12d9d40d0de04c84ba299c67846e838d6d2b4c8baa5e761aa0c6c45188343c00",
                hq = false,
                isCrafted = true,
                listingID = "1ea6ba3a7db77d7b03c3c1562d9e27867993c6ed3f387bb4264a6e99ffdeb812",
                materia = emptyList(),
                onMannequin = false,
                retainerCity = 10,
                retainerID = "d8d88dc1fcee2379fa4d6f2057496cd18ed9e3d7d5907f6815499e8921db456b",
                retainerName = "Jay'zhava",
                sellerID = "107a78dbbec14a484b9e427fe2dd67c22fab8d0dfb7c714efbe0430abfe9ef6c",
                total = 2
            ),
            Listing.WorldListing(
                lastReviewTime = 1700663662,
                pricePerUnit = 2,
                quantity = 1,
                stainID = 0,
                worldName = "Brynhildr",
                worldID = 34,
                creatorName = "",
                creatorID = null,
                hq = false,
                isCrafted = false,
                listingID = "c73338f7deda5c3d1c3dc7f717aaeb023f7566d92354e46470d93e4d36f83503",
                materia = emptyList(),
                onMannequin = false,
                retainerCity = 4,
                retainerID = "947e2006be17929ed6144a9fe613c1d286bd424f16576aa47c74f89aa2134f69",
                retainerName = "Solanna",
                sellerID = "e230acfb1e6c8fa1b56e03d607081c0d408585fdf7e4703e6baf6870524216e2",
                total = 2
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPref = this@ItemActivity.getSharedPreferences(getString(R.string.world_file_key), Context.MODE_PRIVATE)
        val world = sharedPref.getString("world", "Empty").toString()

        sharedPref.registerOnSharedPreferenceChangeListener(this)

        val data: Item? = runBlocking {
            getItem("world", 4650)
        }

        super.onCreate(savedInstanceState)
        setContent {
            ICATheme {
                val worldText by remember{mutableStateOf(world)}

                Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Box(
                            Modifier
                                .fillMaxSize()
                                .weight(1f)){

                            val cheapestListing= cheapestListing(fakeData2)
                            ItemDesc("Boiled Egg",4650,cheapestListing[1],cheapestListing[2],cheapestListing[3],cheapestListing[4])
                            //WorldChangeButton(world = "Crystal")
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .weight(0.15f)){
                            WorldChangeButton(world = worldText)
                        }
                    }
                    Row(){

                    }

            }
        }
    }
}

    fun cheapestListing(listingsData: Item): List<Int?> {
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
                    if (cheapNQ == null || currentPrice * listing.quantity < cheapHQ * quantityHQ!! && !listing.hq) {
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
    private suspend fun getItem(world: String, itemID: Int): Item? {
        return suspendCoroutine { continuation -> val url = BASEURL
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

            }) }

    }

@Composable
fun ItemDesc(name: String, id: Int, priceHQ: Int?, quantityHQ: Int?, priceNQ: Int?, quantityNQ: Int?) {
    val totalHQ= priceHQ?.times(quantityHQ!!)
    val totalNQ= priceNQ?.times(quantityNQ!!)

    Column(Modifier.padding(start = 5.dp)){
        Text(
            text = "$name       $id"
             //       "$priceHQ X $quantityHQ - $totalHQ        NQ: $priceNQ X $quantityNQ - $totalNQ",
        )
        Row(){
            Column() {
                Text(text = "Cheapest HQ", style = TextStyle.Default.copy(fontSize = 10.sp))
                Text(
                    text = "$priceHQ X $quantityHQ Total: $totalHQ",
                    style = TextStyle.Default.copy(fontSize = 14.sp)
                )
            }
            Spacer(modifier = Modifier.padding(start=35.dp))
            Column() {
                Text(text = "Cheapest NQ", style = TextStyle.Default.copy(fontSize = 10.sp))
                Text(
                    text = "$priceNQ X $quantityNQ Total: $totalNQ",
                    style = TextStyle.Default.copy(fontSize = 14.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)


@Composable
fun ItemPreview() {
    ICATheme {
        Surface( modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
            Column {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        Modifier
                            .height(55.dp)
                            .weight(1f),
                    contentAlignment= Alignment.CenterStart
                    ) {
                        val cheapestListing= cheapestListing(fakeData2)
                        ItemDesc("Boiled Egg",4650,cheapestListing[1],cheapestListing[2],cheapestListing[3],cheapestListing[4])
                    }
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(.15f)
                    ) {
                        WorldChangeButton(world = "Crystal")
                    }
                }
                ListingPreview("Jenova",fakeData2)
            }
        }
    }
}

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        recreate()
    }
}
@Composable
fun ListingPreview(world: String, data: Item) {

    var serverWorld = world



    var averagePrice = data.averagePrice

    var listingsHQ: List<Listing> = emptyList()
    var listingsNQ: List<Listing> = emptyList()

    for(item in data.listings){
        when (item){ is Listing.NormalListing ->{
            if(item.hq){
                listingsHQ=listingsHQ.plus(item)
            }
            else{
                listingsNQ=listingsNQ.plus(item)
            }
        }

            is Listing.WorldListing -> {
                if(item.hq){
                    listingsHQ=listingsHQ.plus(item)
                    averagePrice=data.averagePriceHQ
                }
                else{
                    listingsNQ=listingsNQ.plus(item)
                    averagePrice=data.averagePriceNQ
                }
            }
        }
    }

    var viewListings: List<Listing> = listingsNQ

    val hqflag=false
    if(hqflag){
        viewListings=listingsHQ
    }

    Row() {
        Column(modifier = Modifier
            .padding(4.dp)
            .weight(1f),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "High Quality")
        }
        Column(modifier = Modifier
            .padding(4.dp)
            .weight(1f),horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Normal Quality")
        }
    }
    Row() {
        LazyColumn() {
            items(viewListings.size) { index ->
                val listing=viewListings[index]

                Column(modifier= Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp))
                {
                    Spacer(modifier = Modifier.padding(1.dp))
                    when(listing) {
                        is Listing.NormalListing -> {
                            val diff = String.format("%.2f",(listing.pricePerUnit-averagePrice)/averagePrice*100.0)
                            Text(textAlign= TextAlign.Center,
                                text=
                                "$serverWorld ${listing.pricePerUnit}G " +
                                        "X ${listing.quantity} Total: ${listing.total} $diff% " +
                                        "  ${listing.retainerName}")
                        }

                        is Listing.WorldListing -> {
                            val diff = String.format("%.2f",(listing.pricePerUnit-averagePrice)/averagePrice*100.0)
                            Text(textAlign= TextAlign.Center,
                                text=
                                "${listing.worldName} ${listing.pricePerUnit}G " +
                                        "X ${listing.quantity} Total: ${listing.total} $diff% " +
                                        "  ${listing.retainerName}")
                        }
                    }
            } } }
    }
}

