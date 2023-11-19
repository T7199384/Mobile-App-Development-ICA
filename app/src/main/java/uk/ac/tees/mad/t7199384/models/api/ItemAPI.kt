package uk.ac.tees.mad.t7199384.models.api

import retrofit2.Call
import retrofit2.http.GET

interface ItemAPI{

    @GET("comments")
    fun getItem(): Call<List<Item>>

}