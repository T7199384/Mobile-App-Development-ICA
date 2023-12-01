package uk.ac.tees.mad.t7199384.models.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchAPI {
    @GET("item/{itemID}")
    fun getNameFromID(@Path("itemID") itemID: Long): Call<ResponseBody>

    @GET("search")
    fun getIDFromName(@Query("string") itemName:String): Call<ResponseBody>


}