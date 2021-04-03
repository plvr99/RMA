package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

class KvizRepository {

    companion object {
        // TODO: Implementirati
        var kvizovi : List<Kviz>
        fun create() : KvizRepository = KvizRepository()
        init {
            // TODO: Implementirati
            kvizovi = arrayListOf(
                    Kviz("Kviz 1","IM1", createDate(2021, 3, 2), createDate(2021, 3, 2), createDate(2021, 3, 2), 2, "Grupa1",null),
            Kviz("Kviz 2","IM1", createDate(2021, 4, 1), createDate(2021, 8,2), createDate(2021, 8, 3), 3, "Grupa2",null),
            Kviz("Kviz 1","ASP",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa1",2f),
            Kviz("Kviz 2","ASP",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 5, "Grupa1",null),
            Kviz("Kviz 1","OE", createDate(2021, 9,10), createDate(2021, 9,10), createDate(2021, 9,10) ,4, "Grupa1",null),
            Kviz("Kviz 1","OOAD",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa1",0f),
            Kviz("Kviz 2","OOAD",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa2",0f))

        }

        fun getMyKvizes(): List<Kviz> {
            // TODO: Implementirati
            return emptyList()
        }

        fun getAll(): List<Kviz> {
            // TODO: Implementirati
            return kvizovi
        }

        fun getDone(): List<Kviz> {
            // TODO: Implementirati
            return kvizovi.filter { kviz: Kviz -> odrediTipKviza(kviz)==1 }
        }

        fun getFuture(): List<Kviz> {
            // TODO: Implementirati
            return kvizovi.filter { kviz: Kviz -> odrediTipKviza(kviz) == 3 }
        }

        fun getNotTaken(): List<Kviz> {
            // TODO: Implementirati
            return kvizovi.filter { kviz: Kviz -> odrediTipKviza(kviz)== 4 }
        }
        // TODO: Implementirati i ostale potrebne metode
        fun createDate(year : Int, month: Int, day : Int) : Date{
            val date = LocalDate.of(year, month, day).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
            return Date.from(date)
        }
        fun odrediTipKviza(kviz: Kviz) : Int{
            val bodovi = kviz.osvojeniBodovi
            //Calendar.getInstance().time KAO CURRENT TIME
            //ima bodove i datum kraj je prije now - plava, kviz koji je urađen,
            if(bodovi != null && kviz.datumKraj.before(Calendar.getInstance().time)) return 1
            if(bodovi == null){
                //nema bodove i now je prije pocetka - zuta kviz koji tek treba biti aktivan,
                if(Calendar.getInstance().time.before(kviz.datumPocetka)) return 3
                // nema bodove i now je poslije datum kraj - crvena  kviz koji je prošao, a nije urađen.
                if(Calendar.getInstance().time.after(kviz.datumKraj)) return 4
                //nema bodove i datum rada + minute je prije datum kraja - zelena kviz koji je aktivan, ali nije urađen,
                if(kviz.datumPocetka.before(Calendar.getInstance().time) && kviz.datumKraj.after(Calendar.getInstance().time)) return 2
            }
            return 0
        }
    }
}