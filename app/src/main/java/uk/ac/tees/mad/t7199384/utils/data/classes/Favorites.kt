package uk.ac.tees.mad.t7199384.utils.data.classes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites (
    @PrimaryKey(autoGenerate = true) val uid: Long= 0,
    @ColumnInfo(name = "item_name") val itemName: String?,
    @ColumnInfo(name = "item_id") val itemID: Long?
)