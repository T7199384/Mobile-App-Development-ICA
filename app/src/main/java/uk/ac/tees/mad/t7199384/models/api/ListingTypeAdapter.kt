package uk.ac.tees.mad.t7199384.models.api

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class ListingTypeAdapter : TypeAdapter<Listing>() {
    override fun write(out: JsonWriter?, value: Listing?) {
        // Implement if needed
    }

    override fun read(`in`: JsonReader?): Listing? {
        val json = JsonParser.parseReader(`in`)
        val jsonObject = json.asJsonObject

        return if (jsonObject.has("worldName")) {
            Gson().fromJson(jsonObject, Listing.WorldListing::class.java)
        } else {
            Gson().fromJson(jsonObject, Listing.NormalListing::class.java)
        }
    }
}