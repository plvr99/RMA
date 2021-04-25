package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Odgovor

class OdgovoriRepository {
    companion object{
        var odgovori : ArrayList<Odgovor> = arrayListOf()

        fun dodajOdgovor(odgovor: Odgovor){
            odgovori.add(odgovor)
        }
        fun pronadjiOdgovorZaKviz(nazivKviza : String, nazivPredmeta : String, nazivGrupe : String): Odgovor?{
            return odgovori.find { odgovor -> odgovor.nazivKviza.equals(nazivKviza)
                    && odgovor.nazivPredmeta.equals(nazivPredmeta) && odgovor.nazivGrupe.equals(nazivGrupe) }
        }
    }
}