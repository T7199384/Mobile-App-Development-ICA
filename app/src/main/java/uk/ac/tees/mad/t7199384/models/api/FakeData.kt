package uk.ac.tees.mad.t7199384.models.api

import java.util.Random
import java.util.UUID

fun fakeWorldData(): Item {
    val listings = (1..30).map {
        Listing.WorldListing(
            lastReviewTime = 1700693363,
            listingID = UUID.randomUUID().toString(),
            pricePerUnit = Random().nextInt(100),
            quantity = Random().nextInt(100),
            stainID = 0,
            creatorName = "",
            creatorID = null,
            hq = Random().nextBoolean(),
            isCrafted = Random().nextBoolean(),
            materia = emptyList(),
            onMannequin = Random().nextBoolean(),
            retainerCity = Random().nextInt(3),
            retainerID = UUID.randomUUID().toString(),
            retainerName = "Retainer $it",
            sellerID = "Seller $it",
            worldID = Random().nextInt(100),
            worldName = "Crystal",
            total = 0
        )
    }

    listings.forEach { it.total = it.pricePerUnit * it.quantity }

    val item = Item(
        itemID = 1,
        lastUploadTime = System.currentTimeMillis(),
        listings = listings,
        averagePrice = calculateAveragePrice(listings),
        averagePriceNQ = calculateAveragePrice(listings.filter { it.pricePerUnit < 50 }),
        averagePriceHQ = calculateAveragePrice(listings.filter { it.pricePerUnit >= 50 })
    )

    return(item)
}

fun calculateAveragePrice(listings: List<Listing.WorldListing>): Double {
    val total = listings.sumByDouble { it.pricePerUnit.toDouble() }
    return if (listings.isNotEmpty()) total / listings.size else 0.0
}

fun fakeUpdates(): List<Marketable>{
    val mostUpdatedPosts = (1..30).map {
        Marketable(
            itemID= it,
            lastUploadTime =1700693363,
            worldID = 40,
            worldName = "Crystal",
        )
    }
    return mostUpdatedPosts
}