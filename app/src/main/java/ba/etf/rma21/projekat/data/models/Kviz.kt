package ba.etf.rma21.projekat.data.models

import java.util.*

data class Kviz(
        val id : Int, val naziv: String, val nazivPredmeta: String, val datumPocetka: Date, val datumKraj: Date?,
        val datumRada: Date, val trajanje: Int, val nazivGrupe: String, val osvojeniBodovi: Float?
) {
        fun odrediTipKviza() : Int{
                //Calendar.getInstance().time KAO CURRENT TIME
                //ima bodove i datum kraj je prije now - plava, kviz koji je urađen,
                if(osvojeniBodovi != null) return 1
                else{
                        //nema bodove i now je prije pocetka - zuta kviz koji tek treba biti aktivan,
                        if(Calendar.getInstance().time.before(datumPocetka)) return 3
                        // nema bodove i now je poslije datum kraj - crvena  kviz koji je prošao, a nije urađen.
                        if(Calendar.getInstance().time.after(datumKraj)) return 4
                        //nema bodove i datum rada + minute je prije datum kraja - zelena kviz koji je aktivan, ali nije urađen,
                        if(datumPocetka.before(Calendar.getInstance().time) && datumKraj!!.after(Calendar.getInstance().time)) return 2
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