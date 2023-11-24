package uk.ac.tees.mad.t7199384.models.api

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val itemID: Int,
    val worldID: Int? = null,
    val lastUploadTime: Long,
    val listings: List<Listing>,
    val averagePrice: Double,
    val averagePriceNQ: Double,
    val averagePriceHQ: Double
) {}