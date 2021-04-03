package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa

class GrupaRepository {
    companion object {
        private var grupe : ArrayList<Grupa> = arrayListOf();
        init {
            // TODO: Implementirati
            val grupa : Grupa = Grupa("Grupa 1" , "IM1")
            grupe.add(grupa)
            grupe.add(Grupa("Grupa 2" , "IM1"))
            grupe.add(Grupa("Grupa 1" , "ASP"))
            grupe.add(Grupa("Grupa 2" , "ASP"))
            grupe.add(Grupa("Grupa 1" , "OOAD"))
            grupe.add(Grupa("Grupa 2" , "OOAD"))
            grupe.add(Grupa("Grupa 1" , "OE"))
            grupe.add(Grupa("Grupa 2" , "OE"))
        }

        fun getGroupsByPredmet(nazivPredmeta: String): List<Grupa> {
            val rez = grupe.filter { grupa -> grupa.nazivPredmeta.equals(nazivPredmeta) }
            return rez
        }
    }
}