package uk.ac.tees.mad.t7199384.models.api

import kotlinx.serialization.Serializable

sealed class Listing {
    @Serializable
    data class NormalListing(
        val lastReviewTime: Int,
        val pricePerUnit: Int,
        val quantity: Int,
        val stainID: Int,
        val creatorName: String,
        val creatorID: String?,
        val hq: Boolean,
        val isCrafted: Boolean,
        val listingID: String,
        val materia: List<Any>,
        val onMannequin: Boolean,
        val retainerCity: Int,
        val retainerID: String,
        val retainerName: String,
        val sellerID: String,
        val total: Int
    ) :Listing()

    @Serializable
    data class WorldListing(
        val lastReviewTime: Long,
        val pricePerUnit: Int,
        val quantity: Int,
        val stainID: Int,
        val worldName: String,
        val worldID: Int,
        val creatorName: String,
        val creatorID: String?,
        val hq: Boolean,
        val isCrafted: Boolean,
        val listingID: String,
        val materia: List<Any>,
        val onMannequin: Boolean,
        val retainerCity: Int,
        val retainerID: String,
        val retainerName: String,
        val sellerID: String,
        var total: Int
    ) :Listing()
}
