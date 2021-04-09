package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa

class GrupaRepository {
    companion object {
        var grupe : ArrayList<Grupa> = arrayListOf()
        var upisanegrupe : ArrayList<Grupa>
        init {
            grupe.add(Grupa("Grupa 1" , "IM1"))
            grupe.add(Grupa("Grupa 2" , "IM1"))
            grupe.add(Grupa("Grupa 1" , "ASP"))
            grupe.add(Grupa("Grupa 2" , "ASP"))
            grupe.add(Grupa("Grupa 1" , "OOAD"))
            grupe.add(Grupa("Grupa 2" , "OOAD"))
            grupe.add(Grupa("Grupa 1" , "OE"))
            grupe.add(Grupa("Grupa 2" , "OE"))
            grupe.add(Grupa("DM grupa 1", "DM"))
            grupe.add(Grupa("OBP grupa 1", "OBP"))
            grupe.add(Grupa("RMA grupa 1",  "RMA"))
            grupe.add(Grupa("IEK grupa 1", "IEK"))

            upisanegrupe = arrayListOf(grupe.get(8), grupe.get(9), grupe.get(10),grupe.get(11))
        }

        fun getGroupsByPredmet(nazivPredmeta: String): List<Grupa> {
            val rez = grupe.filter { grupa -> grupa.nazivPredmeta.equals( nazivPredmeta )}
            return rez
        }
        fun dajNeupisaneGrupe(): List<Grupa> {
            return grupe.filter { grupa -> !upisanegrupe.contains(grupa) }
        }
    }
}