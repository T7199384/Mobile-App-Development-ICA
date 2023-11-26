package uk.ac.tees.mad.t7199384.models.api

import kotlinx.serialization.Serializable

@Serializable
data class Marketable(
    val itemID: Int,
    val lastUploadTime: Long,
    val worldID: Int,
    val worldName: String
)