package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class KvizRepository {

    companion object {

        suspend fun getUpisani():List<Kviz>{
            return withContext(Dispatchers.IO){
                try {
                    return@withContext getMyKvizes()

                } catch (e: MalformedURLException) {
                    return@withContext emptyList<Kviz>()
                } catch (e: IOException) {
                    return@withContext emptyList<Kviz>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Kviz>()
                }
            }
        }
        suspend fun getMyKvizes(): List<Kviz> {
            return withContext(Dispatchers.IO) {
                val mojiKvizovi = arrayListOf<Kviz>()
                try {
                    val upisaneGrupe = PredmetIGrupaRepository.getUpisaneGrupe()
                    println("upisane grupe size = " + upisaneGrupe.size)
                    for (i in 0 until upisaneGrupe.size){
                        val url1 = "https://rma21-etf.herokuapp.com/grupa/${upisaneGrupe[i].id}/kvizovi"
                        val url = URL(url1)
                        (url.openConnection() as? HttpURLConnection)?.run {
                            val result = this.inputStream.bufferedReader().use { it.readText() }
                            val items = JSONArray(result)
                            val kvizovi = getAll()
                            for (j in 0 until items.length()){
                                val nesto = items.getJSONObject(j)
                                val id = nesto.getInt("id")
                                mojiKvizovi.add(kvizovi.find { kviz -> kviz.id == id }!!)
                            }
                        }
                    }
                    return@withContext mojiKvizovi
                }catch (e: MalformedURLException) {
                    return@withContext emptyList<Kviz>()
                } catch (e: IOException) {
                    return@withContext emptyList<Kviz>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Kviz>()
                }
            }
        }

        suspend fun getAll(): List<Kviz> {
            return withContext(Dispatchers.IO) {
                val kvizovi = arrayListOf<Kviz>()
                val url1 = "https://rma21-etf.herokuapp.com/kviz"
                try {
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        println(result)
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            val datum = nesto.getString("datumPocetak").split("-")
                            val datumPocetka =
                                createDate(datum[0].toInt(), datum[1].toInt(), datum[2].toInt())
//                            val datumKraj = null
                            val datumKraj = createDate(2022, 5,5)
                            val trajanje = nesto.getInt("trajanje")
                            val idKviza = nesto.getInt("id")
                            val grupe = PredmetIGrupaRepository.dajGrupeSaIdem(idKviza)
                            println("Broj pronadjenih grupa sa idem $idKviza je " + grupe.size)
                            for (j in grupe.indices){
                                kvizovi.add( Kviz(id, naziv, grupe[j].nazivPredmeta, datumPocetka, datumKraj, Calendar.getInstance().time, trajanje, grupe[j].naziv, null))
                                // TODO: 2.6.2021 popraviti brek
                                break
                            }
                        }
                    }
                    return@withContext kvizovi
                }catch (e: MalformedURLException) {
                    return@withContext emptyList<Kviz>()
                } catch (e: IOException) {
                    return@withContext emptyList<Kviz>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Kviz>()
                }
            }
        }
        suspend fun getById(id:Int): Kviz? {
            return withContext(Dispatchers.IO) {
                var kviz: Kviz? = null
                try {
                    val url1 = "https://rma21-etf.herokuapp.com/kviz/$id"
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val nesto = JSONObject(result)
                        val id1 = nesto.getInt("id")
                        val naziv = nesto.getString("naziv")
                        val datum = nesto.getString("datumPocetka").split("-")
                        val datumPocetka =
                            createDate(datum[0].toInt(), datum[1].toInt(), datum[2].toInt())
                        val datumKraj = null
                        val trajanje = nesto.getInt("trajanje")
                        val idKviza = nesto.getInt("id")
                        val grupe = PredmetIGrupaRepository.dajGrupeSaIdem(idKviza)
                        for (i in grupe.indices){
                            kviz = Kviz(id1,naziv, grupe[i].nazivPredmeta, datumPocetka, datumKraj, Calendar.getInstance().time, trajanje, grupe[i].naziv, null)
                            if (i == 0 ) break // da vrati prvi kviz sa prvom grupom
                        }
                    }
                    return@withContext kviz
                }catch (e: MalformedURLException) {
                    return@withContext null
                } catch (e: IOException) {
                    return@withContext null
                } catch (e: JSONException) {
                    return@withContext null
                }
            }
        }

        suspend fun getDone(): List<Kviz> {
            return getMyKvizes().filter { kviz: Kviz -> kviz.odrediTipKviza()==1 }
        }

        suspend fun getFuture(): List<Kviz> {
            return getMyKvizes().filter { kviz: Kviz -> kviz.odrediTipKviza() == 3 }
        }

        suspend fun getNotTaken(): List<Kviz> {
            return getMyKvizes().filter { kviz: Kviz -> kviz.odrediTipKviza()== 4 }
        }
        private fun createDate(year : Int, month: Int, day : Int) : Date{
            return GregorianCalendar(year, month-1, day).time
        }
    }
}