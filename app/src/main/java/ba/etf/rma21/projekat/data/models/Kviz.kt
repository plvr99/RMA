package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Kviz(
        @PrimaryKey val id : Int,
        @ColumnInfo (name = "naziv") val naziv: String,
        @ColumnInfo (name = "nazivPredmeta") val nazivPredmeta: String,
        @ColumnInfo (name = "datumPocetka") val datumPocetka: String,
        @ColumnInfo (name = "datumKraj") val datumKraj: String?,
        @ColumnInfo (name = "datumRada" ) val datumRada: String,
        @ColumnInfo (name = "trajanje" ) val trajanje: Int,
        @ColumnInfo (name = "nazivGrupe") val nazivGrupe: String,
        @ColumnInfo (name = "osvojeniBodovi") var osvojeniBodovi: Float?,
        @ColumnInfo (name = "predan") var predan : Boolean = false
) {
        fun odrediTipKviza() : Int{
                //Calendar.getInstance().time KAO CURRENT TIME
                //ima bodove i datum kraj je prije now - plava, kviz koji je urađen,
                if(predan) return 1
                if(osvojeniBodovi != null) return 1
                else{
                        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        val datumPocetka1 = format.parse(datumPocetka.plus("T09:00:00"))
                        val datumKraj1 = format.parse(datumKraj!!)
                        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + datumRada)
                        val datumRada1 = format.parse(datumRada)
                        //nema bodove i now je prije pocetka - zuta kviz koji tek treba biti aktivan,
                        if(Calendar.getInstance().time.before(datumPocetka1)) return 3
                        // nema bodove i now je poslije datum kraj - crvena  kviz koji je prošao, a nije urađen.
                        if(Calendar.getInstance().time.after(datumKraj1)) return 4
                        //nema bodove i datum rada + minute je prije datum kraja - zelena kviz koji je aktivan, ali nije urađen,
                        if(datumPocetka1.before(Calendar.getInstance().time) && datumKraj1.after(Calendar.getInstance().time)) return 2
                }
                return 0
        }

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Kviz

                if (naziv != other.naziv) return false
                if (nazivPredmeta != other.nazivPredmeta) return false
                if (nazivGrupe != other.nazivGrupe) return false

                return true
        }

}