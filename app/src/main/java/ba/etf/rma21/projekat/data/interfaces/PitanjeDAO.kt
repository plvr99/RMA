package ba.etf.rma21.projekat.data.interfaces

import androidx.room.*
import ba.etf.rma21.projekat.data.models.Pitanje

@Dao
interface PitanjeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPitanje(vararg pitanje: Pitanje)
    @Delete
    suspend fun deletePitanje(pitanje: Pitanje)

    @Query("Delete from Pitanje")
    suspend fun deleteAllPitanja()

    @Query("SELECT * FROM Pitanje")
    suspend fun getAllPitanja() : List<Pitanje>

    @Query("Select * from Pitanje where id=:id LIMIT 1")
    suspend fun getPitanjeById(id : Int) : Pitanje
}
