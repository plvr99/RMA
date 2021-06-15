package ba.etf.rma21.projekat.data.interfaces

import androidx.room.*
import ba.etf.rma21.projekat.data.models.Predmet

@Dao
interface PredmetDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg predmet : Predmet)
    @Delete
    suspend fun deletePredmet(predmet: Predmet)

    @Query("Delete from Predmet")
    suspend fun deleteAllPredmet()

    @Query("Select * from Predmet")
    suspend fun getAllPredmet(): List<Predmet>

    @Query("Select * from Predmet where id=:id limit 1")
    suspend fun getPredmetById(id : Int): Predmet
}