package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class AccountRepository {
    companion object {
        private lateinit var context:Context
        lateinit var db : AppDatabase
        var acHash = "ebad9b23-f0d6-4574-ac0b-3a6f320b20bd"
        fun setContext(_context: Context){
            context=_context
            KvizRepository.setContext(_context)
            PitanjeKvizRepository.setContext(_context)

            db = AppDatabase.getInstance(context)
        }


        suspend fun postaviHash(accountHash : String) : Boolean{
            return withContext(Dispatchers.IO){
                if (accountHash != acHash){
                    acHash = accountHash
                    obnovaBaze(accountHash)
                }
                else {
                    //promijeniti
                    acHash = accountHash
                    obnovaBaze(accountHash)
                }
                println("POSTAVLJENI HASH JE " + acHash)
                return@withContext true
            }
        }
        suspend fun obnovaBaze(accountHash: String){
            return withContext(Dispatchers.IO) {
                //brisi
                db.accountDao().deleteAllAccounts()
                db.grupaDao().deleteAllGrupa()
                db.kvizDao().deleteAllKviz()
                db.pitanjeDao().deleteAllPitanja()
                db.predmetDao().deleteAllPredmet()
                db.pitanjeKvizDao().deleteAllPitanjeKviz()
                db.odgovorDao().deleteAllOdgovor()
                //pisi
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                db.accountDao()
                    .insertAccount(Account(accountHash, format.format(Calendar.getInstance().time)))
                db.grupaDao()
                    .insertAllGrupa(*PredmetIGrupaRepository.getUpisaneGrupe().toTypedArray())
                db.predmetDao().insertAll(*PredmetIGrupaRepository.getPredmeti().toTypedArray())
                val kvizovi = KvizRepository.getMyKvizes()
                db.kvizDao().insertAllKviz(*kvizovi.toTypedArray())
                for (i in kvizovi.indices) {
                    //sljedeca linija ce popuniti i PitanjeKvizTabelu
                    db.pitanjeDao().insertAllPitanje(
                        *PitanjeKvizRepository.getPitanja(kvizovi[i].id).toTypedArray()
                    )
                    db.odgovorDao().insertAllOdgovor(
                        *OdgovorRepository.getOdgovoriKviz(kvizovi[i].id).toTypedArray()
                    )
                }
            }
        }

        fun getHash(): String {
            //return acHash
            return acHash
        }

        suspend fun getStudent(): String {
            return withContext(Dispatchers.IO) {
                try {
                    val url1 = "https://rma21-etf.herokuapp.com/student/$acHash"
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val rezultat = JSONObject(result)
                        return@withContext rezultat.getString("student")
                    }
                    return@withContext ""
                } catch (e: MalformedURLException) {
                    return@withContext ""
                } catch (e: IOException) {
                    return@withContext ""
                } catch (e: JSONException) {
                    return@withContext ""
                }
            }
        }
    }
}