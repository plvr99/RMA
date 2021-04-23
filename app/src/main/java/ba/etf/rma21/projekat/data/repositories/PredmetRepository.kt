package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Predmet

class PredmetRepository {
    companion object {
        var upisaniPredmeti : ArrayList<Predmet>
        var predmeti : ArrayList<Predmet> = arrayListOf(Predmet("DM",2), Predmet("IEK", 2), Predmet("OBP", 2),
                Predmet("RMA",2),Predmet("ASP",2), Predmet("OOAD",2),
                Predmet("OE",1), Predmet("IM1", 1))
        init {
            upisaniPredmeti = arrayListOf(predmeti.get(0), predmeti.get(1), predmeti.get(2), predmeti.get(3), predmeti.get(7))
        }
        fun getUpisani(): List<Predmet> {
            return upisaniPredmeti
        }

        fun getAll(): List<Predmet> {
            return predmeti

        }
        fun dajNeupisanePredmete(): List<Predmet> {
            return predmeti.filter { predmet -> !upisaniPredmeti.contains(predmet) }
        }
    }

}