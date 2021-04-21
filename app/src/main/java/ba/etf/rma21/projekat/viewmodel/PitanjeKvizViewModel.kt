package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.OdgovoriRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository

class PitanjeKvizViewModel {

    fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje> {
        return PitanjeKvizRepository.getPitanja(nazivKviza, nazivPredmeta)
    }

    fun dodajOdgovor(odgovor: Odgovor){
        OdgovoriRepository.dodajOdgovor(odgovor)
    }
    fun pronadjiOdgovorZaKviz(nazivKviza : String, nazivPredmeta : String, nazivGrupe : String): Odgovor?{
        return OdgovoriRepository.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe)
    }
}