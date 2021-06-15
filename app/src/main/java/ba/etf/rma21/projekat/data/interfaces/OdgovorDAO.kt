package ba.etf.rma21.projekat.data.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Odgovor

@Dao
interface OdgovorDAO {
    @Insert
    suspend fun insertAllOdgovor(vararg odgovor : Odgovor)
    @Delete
    suspend fun deleteKviz(odgovor: Odgovor)
    @Query("Delete from Odgovor")
    suspend fun deleteAllOdgovor()
    @Query("Select * from Odgovor where kvizTakenId=:kvizTakenId")
    suspend fun getOdgovorForKvizTaken(kvizTakenId : Int) : List<Odgovor>
    @Query("Select count(*) from Odgovor where kvizTakenId=:kvizTakenId and pitanjeId=:pitanjeId")
    suspend fun countOdgovor(kvizTakenId: Int, pitanjeId : Int) : Int
    @Query("select count(*) from odgovor")
    suspend fun brojOdgovora() : Int
    @Query("Select count(*) from Odgovor o, Pitanje p where o.kvizTakenId=:kvizTakenId and o.pitanjeId = p.id and o.odgovoreno = p.tacan")
    suspend fun dajBrojTacnihNaKvizu(kvizTakenId: Int) : Int
}