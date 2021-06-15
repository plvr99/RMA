package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PitanjeKviz(@PrimaryKey(autoGenerate = true) val id : Int?,
                        @ColumnInfo (name = "KvizId") val kvizId: Int,
                        @ColumnInfo (name = "PitanjeId") val pitanjeId : Int)