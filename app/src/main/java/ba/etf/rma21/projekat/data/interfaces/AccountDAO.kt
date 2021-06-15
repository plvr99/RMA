package ba.etf.rma21.projekat.data.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Account

@Dao
interface AccountDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account : Account)

    @Query("DELETE FROM Account")
    suspend fun deleteAllAccounts()
    @Query("Select * from Account Limit 1")
    suspend fun getCurrentAccount() : Account
}