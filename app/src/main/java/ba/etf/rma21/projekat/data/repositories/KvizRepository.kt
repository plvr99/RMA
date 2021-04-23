package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Kviz
import java.util.*

class KvizRepository {

    companion object {
        var kvizovi : ArrayList<Kviz>
        var mojiKvizovi : ArrayList<Kviz>
        init {
            kvizovi = arrayListOf(
                    Kviz("Kviz 1","IM1", createDate(2021, 3, 2), createDate(2021, 8, 2), createDate(2021, 8, 3), 2, "Grupa 1",null),
                    Kviz("Kviz 2","IM1", createDate(2021, 4, 1), createDate(2021, 8,2), createDate(2021, 8, 3), 3, "Grupa 2",null),
                    Kviz("Kviz 1","ASP",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa 1",2f),
                    Kviz("Kviz 2","ASP",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 5, "Grupa 2",null),
                    Kviz("Kviz 1","OE", createDate(2021, 9,10), createDate(2021, 9,10), createDate(2021, 9,10) ,4, "Grupa 1",null),
                    Kviz("Kviz 2","OE", Calendar.getInstance().time, createDate(2021,5,22), createDate(2021,5,22) ,4, "Grupa 2",null),
                    Kviz("Kviz 1","OOAD",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa 1",0f),
                    Kviz("Kviz 2","OOAD",Calendar.getInstance().time, Calendar.getInstance().time,Calendar.getInstance().time, 2, "Grupa 2",0.8f),
                    Kviz("Kviz 1","DM", createDate(2021, 5,15),createDate(2021, 5,15), createDate(2021, 5,15), 5,"DM grupa 1", null),
                    Kviz("Kviz 1","OBP", createDate(2021, 4,2),createDate(2021, 4,2), Calendar.getInstance().time, 5,"OBP grupa 1", null),
                    Kviz("Kviz 1","RMA", createDate(2021, 3,15),createDate(2021, 3,15), createDate(2021, 3,15), 15,"RMA grupa 1", 2f),
                    Kviz("Kviz 1","IEK", createDate(2021, 4,1),createDate(2021, 4,1), createDate(2021, 4,1), 10,"IEK grupa 1", 0.5f))

            mojiKvizovi = arrayListOf(kvizovi.get(0), kvizovi.get(8), kvizovi.get(9), kvizovi.get(10), kvizovi.get(11))
        }
        fun dajNeupisaneKvizove(): List<Kviz> {
            return kvizovi.filter { kviz -> !mojiKvizovi.contains(kviz) }
        }

        fun getMyKvizes(): List<Kviz> {
            return mojiKvizovi
        }

        fun getAll(): List<Kviz> {
            return kvizovi
        }
//        fun getDone(): List<Kviz> {
//            return kvizovi.filter { kviz: Kviz -> odrediTipKviza(kviz)==1 }
//        }
//
//        fun getFuture(): List<Kviz> {
//            return kvizovi.filter { kviz: Kviz -> odrediTipKviza(kviz) == 3 }
//        }
//
//        fun getNotTaken(): List<Kviz> {
//            return kvizovi.filter { kviz: Kviz -> odrediTipKviza(kviz)== 4 }
//        }
        fun getDone(): List<Kviz> {
            return mojiKvizovi.filter { kviz: Kviz -> kviz.odrediTipKviza()==1 }
        }

        fun getFuture(): List<Kviz> {
            return mojiKvizovi.filter { kviz: Kviz -> kviz.odrediTipKviza() == 3 }
        }

        fun getNotTaken(): List<Kviz> {
            return mojiKvizovi.filter { kviz: Kviz -> kviz.odrediTipKviza()== 4 }
        }
        fun createDate(year : Int, month: Int, day : Int) : Date{
            return GregorianCalendar(year, month-1, day).getTime()
        }

        fun upisiKvizove(nazivPredmeta : String, grupa: String){
            for (kviz in kvizovi){
                if(kviz.nazivPredmeta.equals(nazivPredmeta) && kviz.nazivGrupe.equals(grupa)){
                    mojiKvizovi.add(kviz)
                }
            }
        }
    }
}