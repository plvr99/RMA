package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Predmet

class PredmetRepository {
    companion object {
        var upisaniPredmeti : ArrayList<Predmet>
        var predmeti : ArrayList<Predmet> = arrayListOf(Predmet(1,"DM",2), Predmet(1,"IEK", 2), Predmet(1,"OBP", 2),
                Predmet(1,"RMA",2),Predmet(1,"ASP",2), Predmet(1,"OOAD",2),
                Predmet(1,"OE",1), Predmet(1,"IM1", 1))
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