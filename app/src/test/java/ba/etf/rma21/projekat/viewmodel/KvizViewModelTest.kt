package ba.etf.rma21.projekat.viewmodel

import junit.framework.TestCase
import org.junit.Test

class KvizViewModelTest : TestCase() {
    val kvizViewModel : KvizViewModel = KvizViewModel()
    @Test
    fun testGetAllPredmets() {
        assertEquals(8,kvizViewModel.getAllPredmets().size)
    }
    @Test
    fun testGetMyKvizes() {
        assertTrue(!kvizViewModel.getMyKvizes().isEmpty())
        assertEquals(5, kvizViewModel.getMyKvizes().size)
    }
    @Test
    fun testGetAll() {
        assertEquals(12,kvizViewModel.getAll().size)
    }
    @Test
    fun testGetDone() {
        assertFalse(kvizViewModel.getDone().isEmpty())
    }
    @Test
    fun testGetFuture() {
        assertEquals(1,kvizViewModel.getFuture().size)
        assert(kvizViewModel.getFuture().all { kviz -> kviz.osvojeniBodovi==null })
    }
    @Test
    fun testDajNeupisanePredmete() {
        assertEquals("ASP",kvizViewModel.dajNeupisanePredmete()[0].naziv )
    }
    @Test
    fun testUpisiNaPredmet() {
        kvizViewModel.upisiNaPredmet(1,"OE","Grupa 2")
        assertTrue(kvizViewModel.dajNeupisanePredmete().find { predmet -> predmet.godina.equals(1) &&
        predmet.naziv.equals("OE")} == null)
    }
    @Test
    fun testDajNeupisaneGrupe() {
        assertEquals("Grupa 1", kvizViewModel.dajNeupisaneGrupe().find { grupa -> grupa.naziv.equals("Grupa 1")}!!.naziv)
    }
    @Test
    fun testGetNotTaken() {
        assert(!kvizViewModel.getNotTaken().isEmpty())
    }
    @Test
    fun testDajSveGrupeZaPredmet() {
        assertEquals(2, kvizViewModel.dajSveGrupeZaPredmet("OOAD").size)
        assertNotNull(kvizViewModel.dajSveGrupeZaPredmet("OBP"))
    }
}