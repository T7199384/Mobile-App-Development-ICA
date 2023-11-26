package uk.ac.tees.mad.t7199384.models.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecentUpdatesAPI{

    @GET("extra/stats/most-recently-updated?world={world}")
    fun getMostRecentUpdates(@Path("world") world:String): Call<Marketable>

    @GET("extra/stats/least-recently-updated?world={world}")
    fun getLeastRecentUpdates(@Path("world") world:String): Call<Marketable>

}