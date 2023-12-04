package uk.ac.tees.mad.t7199384.models.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import uk.ac.tees.mad.t7199384.utils.data.classes.Item

interface ItemAPI{

    @GET("{world}/{itemID}")
    fun getItem(@Path("world") world:String, @Path("itemID") itemId: String): Call<Item?>

}