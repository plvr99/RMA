package ba.etf.rma21.projekat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ba.etf.rma21.projekat.data.interfaces.*
import ba.etf.rma21.projekat.data.models.*

@Database(entities = arrayOf(Grupa::class, Kviz::class, Pitanje::class, Predmet::class,
    Account::class, PitanjeKviz::class, Odgovor::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun grupaDao(): GrupaDAO
    abstract fun kvizDao(): KvizDAO
    abstract fun pitanjeDao() : PitanjeDAO
    abstract fun predmetDao() : PredmetDAO
    abstract fun accountDao() : AccountDAO
    abstract fun pitanjeKvizDao() : PitanjeKvizDAO
    abstract fun odgovorDao() : OdgovorDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }
        fun setInstance(db: AppDatabase){
            INSTANCE = db
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "RMA21DB"
            ).build()
    }
}