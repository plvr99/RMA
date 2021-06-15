package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class DBRepository {
    companion object{

        private lateinit var context: Context
        lateinit var db : AppDatabase
        fun setContext(_context: Context){
            context=_context
            db = AppDatabase.getInstance(context)
        }
        suspend fun updateNow():Boolean{
            return withContext(Dispatchers.IO) {
             try {
                 val hash = AccountRepository.getHash()
                 val account = db.accountDao().getCurrentAccount()
                 println(account)
                 val datumtext = if(account != null)  account.lastUpdate else "2021-06-15T12:00:00"
                 println("DATUM ZA UPDATE JE " + datumtext)
                 val url1 = "https://rma21-etf.herokuapp.com/account/$hash/lastUpdate?date=$datumtext"
                 val url = URL(url1)
                 (url.openConnection() as? HttpURLConnection)?.run {
                     val result = this.inputStream.bufferedReader().use { it.readText() }
                     val item = JSONObject(result)
                     val booleanText = item.getString("changed")
                     return@withContext booleanText.equals("true")
                 }
                 return@withContext false
             }
             catch (e: MalformedURLException) {
                 return@withContext false
             } catch (e: IOException) {
                 return@withContext false
             } catch (e: JSONException) {
                 return@withContext false
             }
            }
        }
    }
}