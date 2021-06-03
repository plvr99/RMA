package ba.etf.rma21.projekat.data.models

data class Grupa(val id: Int, val naziv: String, val nazivPredmeta: String) {
    override fun toString(): String {
        return naziv
    }
}