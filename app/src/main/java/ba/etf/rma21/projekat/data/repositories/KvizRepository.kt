package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import java.util.*

class KvizRepository {

    companion object {
        // TODO: Implementirati
        public var kvizovi : List<Kviz>
        init {
            // TODO: Implementirati
            var calendar : Calendar = Calendar.getInstance()
            var date : Date = calendar.time
            kvizovi = arrayListOf(
                    Kviz("Kviz 1","IM1",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa1",0f),
            Kviz("Kviz 2","IM1",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 3, "Grupa2",null),
            Kviz("Kviz 1","ASP",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa1",0f),
            Kviz("Kviz 2","ASP",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 5, "Grupa1",null),
            Kviz("Kviz 1","OE",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 4, "Grupa1",0f),
            Kviz("Kviz 1","OOAD",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa1",0f),
            Kviz("Kviz 2","OOAD",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa2",0f))
           // var  k = Kviz("a","a", Date(2021, 2 , 12),Date(2021, 2 , 12),Date(2021, 2 , 12),2,"a",null )
        }

        fun getMyKvizes(): List<Kviz> {
            // TODO: Implementirati
            return emptyList()
        }

        fun getAll(): List<Kviz> {
            // TODO: Implementirati
            return kvizovi;
        }

        fun getDone(): List<Kviz> {
            // TODO: Implementirati
            return emptyList()
        }

        fun getFuture(): List<Kviz> {
            // TODO: Implementirati
            return emptyList()
        }

        fun getNotTaken(): List<Kviz> {
            // TODO: Implementirati
            return emptyList()
        }
        // TODO: Implementirati i ostale potrebne metode
    }
}