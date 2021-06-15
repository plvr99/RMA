package ba.etf.rma21.projekat.data.interfaces

import androidx.room.*
import ba.etf.rma21.projekat.data.models.Grupa

@Dao
interface GrupaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGrupa(vararg grupe : Grupa)
    @Delete
    suspend fun deleteGrupa(grupa: Grupa)
    @Query("DELETE FROM Grupa")
    suspend fun deleteAllGrupa()
    @Query("SELECT * FROM Grupa")
    suspend fun getAllGrupe(): List<Grupa>
    @Query("SELECT * FROM Grupa where id =:id LIMIT 1")
    suspend fun getGrupaWithId(id : Int) : Grupa
    @Query("SELECT * FROM Grupa WHERE predmetId=:predmetId")
    suspend fun getgrupeByPredmetId(predmetId : Int) :List<Grupa>
}