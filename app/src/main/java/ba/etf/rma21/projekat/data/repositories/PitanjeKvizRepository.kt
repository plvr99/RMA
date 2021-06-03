package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Pitanje
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
        suspend fun getPitanja(idKviza:Int):List<Pitanje>{
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/kviz/$idKviza/pitanja"
                try {
                    val listaPitanja = mutableListOf<Pitanje>()
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val items = JSONArray(result)
                        for (i in 0 until items.length()) {
                            val nesto = items.getJSONObject(i)
                            val id = nesto.getInt("id")
                            val naziv = nesto.getString("naziv")
                            val tacan = nesto.getInt("tacan")
                            val tekst = nesto.getString("tekstPitanja")
                            val opcije = nesto.getJSONArray("opcije")
                            val opcijePitanje : ArrayList<String> = ArrayList()
                            for (j in 0 until opcije.length()){
                                opcije.getString(j)
                                opcijePitanje.add(opcije.getString(j))
                            }
                            listaPitanja.add(Pitanje(id, naziv,tekst,opcijePitanje,tacan))
                        }
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
    }
}