package ba.etf.rma21.projekat.data.interfaces

import androidx.room.*
import ba.etf.rma21.projekat.data.models.PitanjeKviz

@Dao
interface PitanjeKvizDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg pitanjeKviz: PitanjeKviz)
    @Delete
    suspend fun deletePitanjeKviz(pitanjeKviz: PitanjeKviz)
    @Query("Delete from PitanjeKviz")
    suspend fun deleteAllPitanjeKviz()
    @Query("Select * from PitanjeKviz where kvizId=:idKviza")
    suspend fun getAllPitanjaZakviz(idKviza : Int) : List<PitanjeKviz>
    @Query("Select count(*) from PitanjeKviz where kvizId=:idKviza and pitanjeId=:idPitanje")
    suspend fun provjeraInsert(idKviza: Int, idPitanje : Int) : Int

}