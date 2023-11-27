package uk.ac.tees.mad.t7199384.models.api

import kotlinx.serialization.Serializable

@Serializable
data class Update(
    val update: Map<String, Marketable> = mapOf<String,Marketable>()
)