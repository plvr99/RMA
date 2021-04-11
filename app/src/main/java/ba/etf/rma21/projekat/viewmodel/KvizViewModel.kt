package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository

class KvizViewModel() {
    companion object {
        var odabranaGodina: Int = 1
    }

    fun getAllPredmets() : List<Predmet>{
        return PredmetRepository.getAll()
    }
    fun getMyKvizes(): List<Kviz> {
        return KvizRepository.getMyKvizes()
    }

    fun getAll(): List<Kviz> {
        return KvizRepository.getAll()
    }

    fun getDone(): List<Kviz> {
        return KvizRepository.getDone()
    }

    fun getFuture(): List<Kviz> {
        return KvizRepository.getFuture()
    }
    fun dajNeupisanePredmete(): List<Predmet> {
        return PredmetRepository.dajNeupisanePredmete()
    }
    fun upisiNaPredmet(godina : Int, predmett: String, grupaa: String){

        GrupaRepository.upisanegrupe.add(
                GrupaRepository.grupe.find { grupa -> grupa.naziv.equals(grupaa) && grupa.nazivPredmeta.equals(predmett) }!!)

        println(GrupaRepository.upisanegrupe)
        PredmetRepository.upisaniPredmeti.add(
                PredmetRepository.predmeti.find { predmet -> predmet.naziv.equals(predmett) && predmet.godina.equals(godina) }!!)

        println(PredmetRepository.upisaniPredmeti)
        KvizRepository.upisiKvizove(predmett, grupaa)
    }
    fun dajNeupisaneGrupe(): List<Grupa> {
        return GrupaRepository.grupe.filter { grupa -> !GrupaRepository.upisanegrupe.contains(grupa) }
    }
    fun getNotTaken(): List<Kviz> {
        return KvizRepository.getNotTaken()
    }
    fun dajSveGrupeZaPredmet(predmet: String) : List<Grupa>{
        return GrupaRepository.getGroupsByPredmet(predmet)
    }

}