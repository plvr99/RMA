package ba.etf.rma21.projekat.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class AccountRepository {
    companion object {
        var acHash = "ebad9b23-f0d6-4574-ac0b-3a6f320b20bd"
        suspend fun postaviHash(acHash: String): Boolean {
            return withContext(Dispatchers.IO) {
                val url1 = "https://rma21-etf.herokuapp.com/student/$acHash"
                try {
                    val url = URL(url1)
                    (url.openConnection() as? HttpURLConnection)?.run {
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val rezultat = JSONObject(result)
                        if (rezultat.has("message")) return@withContext false
                    }
                    this@Companion.acHash = acHash
                    return@withContext true
                } catch (e: MalformedURLException) {
                    return@withContext false
                } catch (e: IOException) {
                    return@withContext false
                } catch (e: JSONException) {
                    return@withContext false
                }
            }
        }

        fun getHash(): String {
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