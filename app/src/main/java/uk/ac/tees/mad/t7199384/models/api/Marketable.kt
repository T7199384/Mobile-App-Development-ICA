package uk.ac.tees.mad.t7199384.models.api

import kotlinx.serialization.Serializable

@Serializable
data class Marketable(
    val itemID: Int,
    val lastUploadTime: Long,
    val worldID: Int,
    val worldName: String

) : Map<String, Marketable?> {
    override val entries: Set<Map.Entry<String, Marketable?>>
        get() = TODO("Not yet implemented")
    override val keys: Set<String>
        get() = TODO("Not yet implemented")
    override val size: Int
        get() = TODO("Not yet implemented")
    override val values: Collection<Marketable?>
        get() = TODO("Not yet implemented")

    override fun containsKey(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsValue(value: Marketable?): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(key: String): Marketable? {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
}