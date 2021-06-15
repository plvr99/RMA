package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma21.projekat.data.repositories.TakeKvizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PitanjeKvizViewModel {
    companion object{
        val scope = CoroutineScope(Job() + Dispatchers.Main)
    }

    fun getPitanja(idKviz : Int, funcSucces : (pitanja: List<Pitanje>) -> Unit, funcError: ()->Unit) {
        scope.launch { val lista =PitanjeKvizRepository.getPitanjaZaKviz(idKviz)
            if (lista.isNotEmpty()) funcSucces.invoke(lista)
            else funcError.invoke()
        }
    }
    fun postaviOdgovorKviz(idKvizTaken:Int,idPitanje:Int,odgovor:Int){
        scope.launch {
             OdgovorRepository.postaviOdgovorKviz(idKvizTaken, idPitanje, odgovor)
        }
    }
    fun predajOdgovore(idKviz: Int, bodovi : Int){
        scope.launch {
            OdgovorRepository.predajOdgovore(idKviz)
            KvizRepository.zavrsiKviz(idKviz, bodovi)
        }
    }
    fun zapocniKviz(kviz: Kviz, funcSucces : (pitanja: List<Pitanje>, kviz : Kviz, idKvizTaken: Int) -> Unit, funcError: (message : String)->Unit) {
        scope.launch {
            val kvizTaken = TakeKvizRepository.zapocniKviz(kviz.id)
            if (kvizTaken == null) funcError.invoke("Greska prilikom zapocinjanja kviza")
            val lista =PitanjeKvizRepository.getPitanjaZaKviz(kviz.id)
            if (lista.isNotEmpty()) funcSucces.invoke(lista, kviz, kvizTaken!!.id)
            else funcError.invoke("Nema pitanja za odabrani kviz")
        }
    }
    fun getOdgovoriKviz(idKvizTaken:Int, funcReturn: (odgovori : List<Odgovor>) ->Unit){
        scope.launch {
            val lista = OdgovorRepository.getOdgovoriKvizDB(idKvizTaken)
            funcReturn.invoke(lista)
        }
    }

}