package uk.ac.tees.mad.t7199384.models.api


data class Listing (
    val listReviewTime: Int,
    val pricePerUnit: Int,
    val quantity: Int,
    val stainID: Int,
    val worldName: String,
    val worldID: Int,
    val creatorName: String,
    val creatorID: String,
    val hq: Boolean,
    val isCrafted: Boolean,
    val listingID: String,
    val materia: List<Object>,
    val onMannequin: Boolean,
    val retainerCity: Int,
    val retainerID: String,
    val retainerName: String,
    val sellerID: String,
    val total: Int
    )