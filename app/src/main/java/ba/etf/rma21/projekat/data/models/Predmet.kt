package ba.etf.rma21.projekat.data.models

data class Predmet(val id : Int, val naziv: String, val godina: Int) {
    override fun toString(): String {
        return naziv
    }
}