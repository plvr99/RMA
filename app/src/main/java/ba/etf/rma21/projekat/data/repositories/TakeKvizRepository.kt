package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.KvizTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat

class TakeKvizRepository {
    companion object {
        suspend fun zapocniKviz(idKviza: Int): KvizTaken?{
            return withContext(Dispatchers.IO){
                try {
                    val kvizTaken = getKvizTaken(idKviza)
                    if(kvizTaken != null) return@withContext kvizTaken
                    val hash = AccountRepository.getHash()
                    val url1 ="https://rma21-etf.herokuapp.com/student/$hash/kviz/$idKviza"
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                        this.requestMethod = "POST"
                        this.doOutput = true
                        val result = this.inputStream.bufferedReader().use { it.readText() }
                        val item = JSONObject(result)
                        val id = item.getInt("id")
                        val datumRadaString = item.getString("datumRada")
                        val student = item.getString("student")
                        val datumRada = SimpleDateFormat("yyyy-MM-dd").parse(datumRadaString)
                        return@withContext KvizTaken(id, student, datumRada, 0, idKviza)
                    }
                    return@withContext null
                }catch (e: MalformedURLException) {
                    return@withContext null
                } catch (e: IOException) {
                    return@withContext null
                } catch (e: JSONException) {
                    return@withContext null
                }
            }
        }

        suspend fun getPocetiKvizovi(): ArrayList<KvizTaken>? {
            return withContext(Dispatchers.IO) {
                val list = arrayListOf<KvizTaken>()
                try {
                    val hash = AccountRepository.getHash()
                    val url1 = "https://rma21-etf.herokuapp.com/student/$hash/kviztaken"
                    (URL(url1).openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val items = JSONArray(result)
                    if (items.length() == 0) return@withContext null
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val idKvizTaken = item.getInt("id")
                        val osvojeniBodovi = item.getInt("osvojeniBodovi")
                        val datumRadaString = item.getString("datumRada")
                        val student = item.getString("student")
                        val datumRada = SimpleDateFormat("yyyy-MM-dd").parse(datumRadaString)
                        val kvizId= item.getInt("KvizId")
                        list.add(KvizTaken(idKvizTaken,student, datumRada, osvojeniBodovi, kvizId))
                    }
                return@withContext list
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

        suspend fun getKvizTaken(idKviza: Int) : KvizTaken? {
        return withContext(Dispatchers.IO) {
            try {
                val hash = AccountRepository.getHash()
                val url1 = "https://rma21-etf.herokuapp.com/student/$hash/kviztaken"
                (URL(url1).openConnection() as? HttpURLConnection)?.run {
                    val result = this.inputStream.bufferedReader().use { it.readText() }
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        if (item.getInt("KvizId") == idKviza) {
                            val idKvizTaken = item.getInt("id")
                            val osvojeniBodovi = item.getInt("osvojeniBodovi")
                            val datumRadaString = item.getString("datumRada")
                            val student = item.getString("student")
                            val datumRada = SimpleDateFormat("yyyy-MM-dd").parse(datumRadaString)
                            return@withContext KvizTaken(idKvizTaken, student, datumRada, osvojeniBodovi, idKviza)
                        }
                    }
                }
                return@withContext null
            }catch (e: MalformedURLException) {
                return@withContext null
            } catch (e: IOException) {
                return@withContext null
            } catch (e: JSONException) {
                return@withContext null
            }
                }
            }
    }
}