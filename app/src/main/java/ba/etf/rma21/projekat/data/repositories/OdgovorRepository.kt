package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class OdgovorRepository {
    companion object{
        var odgovori : ArrayList<Odgovor> = arrayListOf()
        private lateinit var context: Context
        lateinit var db : AppDatabase
        fun setContext(_context: Context){
            context=_context
            db = AppDatabase.getInstance(context)
        }

        suspend fun posaljiNaServis(idKvizTaken:Int,idPitanje:Int,odgovor:Int):Int{
            return withContext(Dispatchers.IO){
                try {
                    val hash = AccountRepository.getHash()
                    var bodovi = 0
                    val url1 = "https://rma21-etf.herokuapp.com/student/$hash/kviztaken/$idKvizTaken/odgovor"
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        this.requestMethod = "POST"
                        this.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
                        this.setRequestProperty("Accept", "application/json")
                        this.doOutput=true

                        val kvizTaken = TakeKvizRepository.getPocetiKvizovi()!!.find { kvizTaken -> kvizTaken.id == idKvizTaken }
                        val pitanja = PitanjeKvizRepository.getPitanja(kvizTaken!!.KvizId)
                        val bod = pitanja.find { pitanje -> pitanje.id == idPitanje }?.tacan == odgovor
                        bodovi = kvizTaken.osvojeniBodovi
                        if(bod) bodovi += ((1f / pitanja.size) * 100).toInt()
                        val json = JSONObject()
                        json.put("odgovor", odgovor).put("pitanje", idPitanje).put("bodovi", bodovi)
                        this.outputStream.write(json.toString().toByteArray(charset("UTF-8")))
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val rez = JSONObject(result)
                        if (rez.has("message")) return@withContext -1
                        else return@withContext bodovi
                    }
                        return@withContext bodovi
                }catch (e: MalformedURLException) {
                    return@withContext -1
                } catch (e: IOException) {
                    return@withContext -1
                } catch (e: JSONException) {
                    return@withContext -1
                }
            }
        }

        suspend fun getOdgovoriKviz(idKviza:Int):List<Odgovor>{
            return withContext(Dispatchers.IO){
                try {
                    val lista = arrayListOf<Odgovor>()
                    val kvizTaken = TakeKvizRepository.getKvizTaken(idKviza)
                    val idKviztaken = kvizTaken?.id ?: -1
                    if (idKviztaken != -1){
                        val hash = AccountRepository.getHash()
                        val url1 = "https://rma21-etf.herokuapp.com/student/$hash/kviztaken/$idKviztaken/odgovori"
                        (URL(url1).openConnection() as? HttpURLConnection)?.run {
                            val result = this.inputStream.bufferedReader().use { it.readText() }
                            val items = JSONArray(result)
                            for (i in 0 until items.length()){
                                val item = items.getJSONObject(i)
                                lista.add(Odgovor(null, item.getInt("PitanjeId"), idKviztaken, item.getInt("odgovoreno")))
                            }
                        }
                    }
                    return@withContext lista
                }catch (e: MalformedURLException) {
                    return@withContext emptyList<Odgovor>()
                } catch (e: IOException) {
                    return@withContext emptyList<Odgovor>()
                } catch (e: JSONException) {
                    return@withContext emptyList<Odgovor>()
                }
            }
        }
        //DB
        suspend fun postaviOdgovorKviz(idKvizTaken:Int,idPitanje:Int,odgovor:Int):Int{
            return withContext(Dispatchers.IO){

                val kvizTaken = TakeKvizRepository.getPocetiKvizovi()!!.first { kvizTaken -> kvizTaken.id == idKvizTaken }



                var bodovi = 0
                println("Count ODGOVORA : " +(db.odgovorDao().countOdgovor(idKvizTaken, idPitanje) == 0))
                if(db.odgovorDao().countOdgovor(idKvizTaken, idPitanje) == 0 ) {
                    db.odgovorDao().insertAllOdgovor(Odgovor(null, idPitanje, idKvizTaken, odgovor))
                }
                println("BODOVI" + db.odgovorDao().dajBrojTacnihNaKvizu(idKvizTaken).toFloat())
                bodovi =
                    ((db.odgovorDao().dajBrojTacnihNaKvizu(idKvizTaken).toFloat()
                            / db.pitanjeKvizDao().getAllPitanjaZakviz(kvizTaken.KvizId).size)*100).toInt()
                println("BODOVI bodovi " + bodovi)
                return@withContext bodovi
            }
        }
        suspend fun predajOdgovore(idKviz : Int){
            return withContext(Dispatchers.IO){
                val kvizTaken = TakeKvizRepository.getPocetiKvizovi()!!.find { kvizTaken -> kvizTaken.KvizId == idKviz }
                val odgovori = db.odgovorDao().getOdgovorForKvizTaken(kvizTaken!!.id)
                for (i in odgovori.indices){
                    posaljiNaServis(kvizTaken.id, odgovori[i].pitanjeId, odgovori[i].odgovoreno)
                }
            }
        }
        suspend fun getOdgovoriKvizDB(idKvizTaken: Int) : List<Odgovor>{
            return withContext(Dispatchers.IO){
                val lista = db.odgovorDao().getOdgovorForKvizTaken(idKvizTaken)
                return@withContext lista
            }
        }
    }
}