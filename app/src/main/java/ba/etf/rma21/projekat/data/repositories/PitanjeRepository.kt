package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Pitanje

class PitanjeRepository {
    companion object{
        var pitanja : ArrayList<Pitanje>
        init {
            pitanja = arrayListOf(
                Pitanje("IM1 P1","2+2=", arrayListOf("4","3","8"), 0),
                Pitanje("IM1 P2","3+3=", arrayListOf("4","6","8"), 1)
            )
        }
    }
}