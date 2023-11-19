package uk.ac.tees.mad.t7199384.models.api

data class Item (
    val itemID: Int,
    val lastUploadTime: Long,
    val listings: Array<Listing>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (itemID != other.itemID) return false
        if (lastUploadTime != other.lastUploadTime) return false
        if (!listings.contentEquals(other.listings)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemID
        result = 31 * result + lastUploadTime.hashCode()
        result = 31 * result + listings.contentHashCode()
        return result
    }
}