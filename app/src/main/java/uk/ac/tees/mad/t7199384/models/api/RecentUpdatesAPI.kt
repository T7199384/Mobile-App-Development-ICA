package uk.ac.tees.mad.t7199384.models.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.t7199384.utils.data.classes.Update

interface RecentUpdatesAPI{

    @GET("most-recently-updated")
    fun getMostRecentUpdates(@Query("world") world:String): Call<Update>

    @GET("least-recently-updated")
    fun getLeastRecentUpdates(@Query("world") world:String): Call<Update>

}