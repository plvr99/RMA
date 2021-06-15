package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class PredmetIGrupaRepository {
    companion object {
        private lateinit var context:Context
        lateinit var db : AppDatabase
        fun setContext(_context: Context){
            context=_context
            db = AppDatabase.getInstance(context)
        }
        var predmeti : ArrayList<Predmet> = arrayListOf()
        var upisaniPredmeti : ArrayList<Predmet> = arrayListOf() // dobija se iz upisanih grupa
        var grupe : ArrayList<Grupa> = arrayListOf()
       // var upisaneGrupe : ArrayList<Predmet> = arrayListOf()
        suspend fun getPredmeti(): List<Predmet> {
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/predmet"
                try {
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        predmeti.clear()
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            val godina = nesto.getInt("godina")
                            predmeti.add(Predmet(id, naziv, godina))
                        }
                    }
                    return@withContext predmeti
                }catch (e: MalformedURLException) {
                    return@withContext emptyList<Predmet>()
                } catch (e: IOException) {
                    return@withContext emptyList<Predmet>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Predmet>()
                }
            }
        }
        suspend fun getPredmetiDB() : List<Predmet>{
            return withContext(Dispatchers.IO) {
                if(DBRepository.updateNow()){
                    AccountRepository.obnovaBaze(AccountRepository.getHash())
                }
                return@withContext db.predmetDao().getAllPredmet()
            }
        }
//        fun dajNeupisanePredmete(): List<Predmet> {
//            return PredmetRepository.predmeti.filter { predmet -> !PredmetRepository.upisaniPredmeti.contains(predmet) }
//        }
//        fun getUpisani(): List<Predmet> {
//            return PredmetRepository.upisaniPredmeti
//        }
//        fun dajNeupisaneGrupe(): List<Grupa> {
//            return GrupaRepository.grupe.filter { grupa -> !GrupaRepository.upisanegrupe.contains(grupa) }
//        }
        suspend fun getGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/grupa"
                try {
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        grupe.clear()
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            val predmetId = nesto.getInt("PredmetId")
                            val nazivPredmet = getPredmetById(predmetId)!!.naziv
                            grupe.add(Grupa(id, naziv, nazivPredmet, predmetId))
                        }
                    }
                    return@withContext grupe
                }catch (e: MalformedURLException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: IOException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Grupa>()
                }
            }
        }
        suspend fun getGrupeDB(): List<Grupa>{
            return withContext(Dispatchers.IO){
                if(DBRepository.updateNow()){
                    AccountRepository.obnovaBaze(AccountRepository.getHash())
                }
                return@withContext db.grupaDao().getAllGrupe()
            }
        }
        suspend fun getPredmetById(id: Int): Predmet?{
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/predmet/$id"
                try {
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val item = JSONObject(result)
                            val id1 = item.getInt("id")
                            val naziv = item.getString("naziv")
                            val godina = item.getInt("godina")
                        return@withContext Predmet(id1, naziv, godina)
                    }
                }catch (e: MalformedURLException) {
                    return@withContext null
                } catch (e: IOException) {
                    return@withContext null
                } catch (e: JSONException) {
                    return@withContext null
                }
            }
        }
        suspend fun getPredmetByIdDB(id : Int) : Predmet{
            return withContext(Dispatchers.IO) {
                if (DBRepository.updateNow()) {
                    AccountRepository.obnovaBaze(AccountRepository.getHash())
                }
                return@withContext db.predmetDao().getPredmetById(id)
            }
        }
        suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa> {
            return withContext(Dispatchers.IO) {
                val grupeZaPredmet = arrayListOf<Grupa>()
                val nazivPredmet = getPredmetById(idPredmeta)!!.naziv
                val url1 = "https://rma21-etf.herokuapp.com/predmet/$idPredmeta/grupa"
                try {
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            grupeZaPredmet.add(Grupa(id, naziv, nazivPredmet, idPredmeta))
                        }
                    }
                    return@withContext grupeZaPredmet
                } catch (e: MalformedURLException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: IOException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Grupa>()
                }
            }
        }
        suspend fun getGrupeZaPredmetDB(idPredmeta: Int) : List<Grupa>{
            return withContext(Dispatchers.IO) {
                if (DBRepository.updateNow()) {
                    AccountRepository.obnovaBaze(AccountRepository.getHash())
                }
                return@withContext db.grupaDao().getgrupeByPredmetId(idPredmeta)
            }
        }

        suspend fun upisiUGrupu(idGrupa: Int): Boolean {
            return withContext(Dispatchers.IO) {
                val hash = AccountRepository.getHash()
                println("HASSSSSSSSSSSHSHSHSHSHSH" + hash)
                val url1 = "https://rma21-etf.herokuapp.com/grupa/$idGrupa/student/$hash"
                try {
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        this.requestMethod = "POST"
                        this.doOutput = true
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val rezultat = JSONObject(result)
                        val poruka = rezultat.getString("message")
                        if (poruka.contains("je dodan u grupu")) {
                            AccountRepository.obnovaBaze(AccountRepository.getHash())
                            return@withContext true
                        }
                    }
                    return@withContext false
                } catch (e: MalformedURLException) {
                    return@withContext false
                } catch (e: IOException) {
                    return@withContext false
                } catch (e: JSONException) {
                    return@withContext false
                }
            }
        }
        suspend fun dajGrupeSaIdem(id : Int) : List<Grupa> {
            val grupe = arrayListOf<Grupa>()
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/kviz/$id/grupa"
                try {
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id1 = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            val predmetId = nesto.getInt("PredmetId")
                            val predmet = getPredmetById(predmetId)
                            grupe.add(Grupa(id1, naziv, predmet!!.naziv, predmetId))
                        }
                    }
                    return@withContext grupe
                } catch (e: MalformedURLException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: IOException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Grupa>()
                }
            }
        }
        suspend fun getUpisaneGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                val hash = AccountRepository.getHash()
                println("HASHE "+ hash)
                val url1 = "https://rma21-etf.herokuapp.com/student/$hash/grupa"
                val upisaneGrupe = arrayListOf<Grupa>()
                try {
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val items = JSONArray(result)
                        upisaniPredmeti.clear()
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            val predmetId = nesto.getInt("PredmetId")
                            var nazivPredmet = ""
                            //todo mozda treba clearat upisane predemtetr
                            getPredmetById(predmetId)?.let {
                                upisaniPredmeti.add(it)
                                nazivPredmet = it.naziv
                            }
                            upisaneGrupe.add(Grupa(id, naziv, nazivPredmet, predmetId))
                        }
                    }
                    return@withContext upisaneGrupe
                } catch (e: MalformedURLException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: IOException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Grupa>()
                }
            }
        }
        suspend fun getUpisaneGrupeDB() : List<Grupa>{
            return withContext(Dispatchers.IO){
                if (DBRepository.updateNow()) {
                    AccountRepository.obnovaBaze(AccountRepository.getHash())
                }
                // U BAZI SE SAMO NALAZE UPISANE GRUPE
                return@withContext db.grupaDao().getAllGrupe()
            }
        }
        suspend fun getNeupisaneGrupe(): List<Grupa>{
            return withContext(Dispatchers.IO) {
                try {
                    if (DBRepository.updateNow()) {
                        AccountRepository.obnovaBaze(AccountRepository.getHash())
                    }
                    val lista = getGrupe() - getUpisaneGrupeDB()
                    println("GET NEUPISANE GRUPE POZVAN: " + lista)
                    return@withContext lista
                } catch (e: MalformedURLException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: IOException) {
                    return@withContext emptyList<Grupa>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Grupa>()
                }
            }
        }

        suspend fun getNeupisanePredmete(): List<Predmet>{
            return withContext(Dispatchers.IO) {
                try {
                    val predmeti = arrayListOf<Predmet>()
                    val grupe = getNeupisaneGrupe()
                    for (i in grupe.indices) {
                        val predmet = getPredmetByIdDB(grupe[i].predmetId)
                        if(!predmeti.contains(predmet)) predmeti.add(predmet)
                    }
                    println("GET NEUPISANI PREDMETI POZVANI: " +predmeti)
                    return@withContext predmeti
                } catch (e: MalformedURLException) {
                    return@withContext emptyList<Predmet>()
                } catch (e: IOException) {
                    return@withContext emptyList<Predmet>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Predmet>()
                }
            }
        }
    }
}