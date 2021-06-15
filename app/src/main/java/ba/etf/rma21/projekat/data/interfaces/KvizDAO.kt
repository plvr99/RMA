package ba.etf.rma21.projekat.data.interfaces

import androidx.room.*
import ba.etf.rma21.projekat.data.models.Kviz

@Dao
interface KvizDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKviz(vararg kvizovi : Kviz)
    @Delete
    suspend fun deleteKviz(kviz: Kviz)
    @Query("Select * from Kviz")
    suspend fun getAllKviz() : List<Kviz>
    @Query("Delete from Kviz")
    suspend fun deleteAllKviz()
    @Query("SELECT * FROM Kviz where id =:id LIMIT 1")
    suspend fun getKvizById(id : Int) : Kviz

}