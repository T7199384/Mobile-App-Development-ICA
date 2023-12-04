package uk.ac.tees.mad.t7199384.utils.data.classes

import kotlinx.serialization.Serializable

@Serializable
data class Marketable(
    val itemID: Long,
    val lastUploadTime: Long,
    val worldID: Int,
    val worldName: String
)