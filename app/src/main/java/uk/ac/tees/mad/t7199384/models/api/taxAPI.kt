package uk.ac.tees.mad.t7199384.models.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface taxAPI {
    @GET("tax-rates")
    fun getTaxes(@Query("world") world:String): Call<ResponseBody>

}