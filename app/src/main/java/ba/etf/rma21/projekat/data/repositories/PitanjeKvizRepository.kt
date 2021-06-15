package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.PitanjeKviz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class PitanjeKvizRepository {
    companion object{
        private lateinit var context: Context
        lateinit var db : AppDatabase
        fun setContext(_context:Context){
            context=_context
            db = AppDatabase.getInstance(context)
        }

        suspend fun getPitanja(idKviza:Int):List<Pitanje>{
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/kviz/$idKviza/pitanja"
                try {
                    val listaPitanja = mutableListOf<Pitanje>()
                    val listaPitanjaKviz = mutableListOf<PitanjeKviz>()
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            if(db.pitanjeKvizDao().provjeraInsert(idKviza, id) == 0 ){
                                db.pitanjeKvizDao().insertAll(PitanjeKviz(null, idKviza, id))
                            }
                            listaPitanjaKviz.add(PitanjeKviz(null, idKviza, id))
                            val naziv = nesto.getString("naziv")
                            val tacan = nesto.getInt("tacan")
                            val tekst = nesto.getString("tekstPitanja")
                            val opcije = nesto.getJSONArray("opcije")
                            var opcijePitanje  = ""
                            for (j in 0 until opcije.length()-1){
                                opcijePitanje = opcijePitanje.plus(opcije.getString(j)).plus(",")
                            }
                            if (opcije.length()!=0) opcijePitanje = opcijePitanje.plus(opcije.getString(opcije.length()-1))
                            listaPitanja.add(Pitanje(id, naziv,tekst,opcijePitanje,tacan))
                        }
//                        println("SIZE PITANJAKVIZ JE " + listaPitanjaKviz.size)
//                        db.pitanjeKvizDao().insertAll(*listaPitanjaKviz.toTypedArray())
                    }
                    return@withContext listaPitanja
                }
                catch (e: MalformedURLException) {
                    return@withContext emptyList<Pitanje>()
                } catch (e: IOException) {
                    return@withContext emptyList<Pitanje>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Pitanje>()
                }
            }
        }
        suspend fun getPitanjaZaKviz(idKviz : Int ): List<Pitanje> {
            return withContext(Dispatchers.IO){
                val pitanjeKvizList = db.pitanjeKvizDao().getAllPitanjaZakviz(idKviz)
                val pitanja = arrayListOf<Pitanje>()
                for (i in pitanjeKvizList.indices){
                    pitanja.add(db.pitanjeDao().getPitanjeById(pitanjeKvizList[i].pitanjeId))
                }
                return@withContext pitanja
            }
        }
    }
}