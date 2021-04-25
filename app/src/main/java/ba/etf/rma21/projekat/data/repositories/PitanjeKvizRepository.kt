package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.PitanjeKviz

class PitanjeKvizRepository {
    companion object{
        var lista : ArrayList<PitanjeKviz>
        var pitanja : ArrayList<Pitanje>
        init {
            pitanja = arrayListOf(
                Pitanje("P1","2+2=", arrayListOf("4","3","8"), 0),
                Pitanje("P2","3+3=", arrayListOf("4","6","8"), 1),
                Pitanje("P3","Formula za otpor R glasi:", arrayListOf("R = U * I","R = I / U","R = U / I"), 2),
                Pitanje("P4","I KZ: ", arrayListOf("Zbir struja koje ulaze u cvor jednak je nuli",
                    "Zbir struja koje ulaze u cvor jednak je zbiru struja koje izlaze iz cvora","Suma svih napona u konturi jednaa je nuli"), 1)
            )
            lista = arrayListOf(
                PitanjeKviz("P1","Kviz 2", "IM1"), PitanjeKviz("P2","Kviz 2", "IM1"),
                PitanjeKviz("P1","Kviz 1", "IM1"), PitanjeKviz("P2","Kviz 1", "IM1"),
                PitanjeKviz("P3","Kviz 2", "OE"), PitanjeKviz("P4","Kviz 2", "OE")
            )
        }
        fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje> {
            return pitanja.filter { pitanje -> getNazivPitanjaKviz(nazivKviza, nazivPredmeta).contains(pitanje.naziv) }
        }
        private fun getNazivPitanjaKviz(nazivKviza: String, nazivPredmeta: String): List<String> {
            return lista.filter { pitanjeKviz -> pitanjeKviz.kviz.equals(nazivKviza)
                    && pitanjeKviz.nazivPredmeta.equals(nazivPredmeta)}.map { pitanjeKviz -> pitanjeKviz.naziv }
        }
    }
}