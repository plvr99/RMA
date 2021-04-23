package ba.etf.rma21.projekat.data.models

import ba.etf.rma21.projekat.view.FragmentPitanje

data class Odgovor(val nazivKviza: String, val nazivPredmeta : String, val nazivGrupe : String,
                   var pitanja : ArrayList<FragmentPitanje>, var zavrseno : Boolean = false, var procenatTacnosti : Float = 0f){
}
