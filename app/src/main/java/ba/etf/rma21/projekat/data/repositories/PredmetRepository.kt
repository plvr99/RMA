package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet

class PredmetRepository {
    companion object {
        var upisaniPredmeti : List<Predmet>
        fun create() : PredmetRepository = PredmetRepository()
        var predmeti : List<Predmet>
        init {
            upisaniPredmeti = arrayListOf(Predmet("DM",2), Predmet("IEK", 2), Predmet("OBP", 2), Predmet("RMA",2))
            predmeti = arrayListOf(Predmet("ASP",2), Predmet("OOAD",2), Predmet("OE",1), Predmet("IM1", 1))
        }
        fun getUpisani(): List<Predmet> {
            return upisaniPredmeti;
        }

        fun getAll(): List<Predmet> {
            return predmeti + upisaniPredmeti
        }
        // TODO: Implementirati i ostale potrebne metode
    }

}