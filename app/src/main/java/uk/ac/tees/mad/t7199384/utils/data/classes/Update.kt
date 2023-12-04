package uk.ac.tees.mad.t7199384.utils.data.classes

import kotlinx.serialization.Serializable
import uk.ac.tees.mad.t7199384.utils.data.classes.Marketable

@Serializable
data class Update(
    val items: List<Marketable> = emptyList()
) {
}