package uk.ac.tees.mad.t7199384.utils.data.classes


import kotlinx.serialization.Serializable
import uk.ac.tees.mad.t7199384.utils.data.classes.Listing

@Serializable
data class Item(
    val itemID: Int,
    val lastUploadTime: Long,
    val listings: List<Listing>,
    val averagePrice: Double,
    val averagePriceNQ: Double,
    val averagePriceHQ: Double
) {
}


