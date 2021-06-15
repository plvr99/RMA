package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Grupa(@PrimaryKey val id: Int, @ColumnInfo (name = "naziv")val naziv: String,
                 @ColumnInfo (name = "nazivPredmeta") val nazivPredmeta: String,
                 @ColumnInfo val predmetId : Int) {
    override fun toString(): String {
        return naziv
    }
}