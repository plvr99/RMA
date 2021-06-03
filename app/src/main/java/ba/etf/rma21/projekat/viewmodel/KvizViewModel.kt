package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PredmetIGrupaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class KvizViewModel() {
    companion object {
        var odabranaGodina: Int = 0
        var odabraniPredmet: Int = -1
        var odabranaGrupa: Int = -1
        var odabraniKvizovi: Int = 0
        val scope = CoroutineScope(Job() + Dispatchers.Main)
    }
    fun init(initFunc : (kvizovi : List<Kviz>) ->Unit){
        scope.launch {
            initFunc.invoke( KvizRepository.getAll())
        }
    }

    fun upisiNaPredmet(godina : Int, predmett: String, grupaa: String){
        println("upis  " + predmett + " " + grupaa)
        scope.launch {
            val grupa = PredmetIGrupaRepository.getGrupe().find { grupa -> grupa.naziv.equals(grupaa) &&
                    grupa.nazivPredmeta.equals(predmett)}
            PredmetIGrupaRepository.upisiUGrupu(grupa!!.id)
        }

    }

    fun getAllPredmets(populateWithPredmet : (predmeti: List<Predmet>) -> Unit){
        scope.launch {  populateWithPredmet.invoke(PredmetIGrupaRepository.getPredmeti())}
    }
    fun dajNeupisanePredmete(populateWithPredmet : (predmeti: List<Predmet>) -> Unit) {
        scope.launch {  populateWithPredmet.invoke(PredmetIGrupaRepository.getNeupisanePredmete())}
    }

    fun dajNeupisaneGrupe(populateWithGrupe : (grupe: List<Grupa>) -> Unit){
        scope.launch {populateWithGrupe.invoke(PredmetIGrupaRepository.getNeupisaneGrupe() ) }
    }

    fun dajSveGrupeZaPredmet(predmet: String, populateWithGrupe : (grupe: List<Grupa>) -> Unit){
        scope.launch {
            println("PREDMET KOJI SE NASAO JE " +PredmetIGrupaRepository.predmeti.find
            { predmet1 -> predmet1.naziv.equals(predmet) }!!.naziv)

            populateWithGrupe.invoke(PredmetIGrupaRepository.getGrupeZaPredmet(PredmetIGrupaRepository.predmeti.find
            { predmet1 -> predmet1.naziv.equals(predmet) }!!.id))
        }

    }
    fun getMyKvizes(showKvizovi: (kvizovi: List<Kviz>) -> Unit){
        scope.launch { val lista = KvizRepository.getMyKvizes()
            println("Get My kvisez called lista size = "  + lista.size)
            showKvizovi.invoke(lista)
        }
    }

    fun getAll(showKvizovi: (kvizovi: List<Kviz>) -> Unit) {
        scope.launch { val lista = KvizRepository.getAll()
            showKvizovi.invoke(lista)
        }
    }

    fun getDone(showKvizovi: (kvizovi: List<Kviz>) -> Unit) {
        scope.launch { val lista = KvizRepository.getDone()
            showKvizovi.invoke(lista)
        }
    }

    fun getFuture(showKvizovi: (kvizovi: List<Kviz>) -> Unit) {
        scope.launch { val lista = KvizRepository.getFuture()
            showKvizovi.invoke(lista)
        }
    }

    fun getNotTaken(showKvizovi: (kvizovi: List<Kviz>) -> Unit){
        scope.launch { val lista = KvizRepository.getNotTaken()
            showKvizovi.invoke(lista)
        }
    }

}