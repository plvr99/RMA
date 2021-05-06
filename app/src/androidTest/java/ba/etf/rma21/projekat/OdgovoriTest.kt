package ba.etf.rma21.projekat

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma21.projekat.UtilTestClass.Companion.withBackground
import ba.etf.rma21.projekat.UtilTestClass.Companion.withTextColor
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import org.hamcrest.CoreMatchers.*
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class OdgovoriTest {
    val GREEN = Color.parseColor("#3DDC84")
    val RED = Color.parseColor("#DB4F3D")
    @get:Rule
    val intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun postojeOdgovori(){
        val kvizovi = odaberiKviz()
        val pitanja = PitanjeKvizRepository.getPitanja(kvizovi[0].naziv, kvizovi[0].nazivPredmeta)
        var indeks = 0
        for (pitanje in pitanja) {
            Espresso.onView(withId(R.id.navigacijaPitanja))
                .perform(NavigationViewActions.navigateTo(indeks))
            Espresso.onView(withId(R.id.tekstPitanja))
                .check(matches(withText(pitanja[indeks].tekst)))
            val odgovori = pitanja[indeks].opcije
            for(odgovor in odgovori!!){
                onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovor))).inAdapterView(withId(R.id.odgovoriLista)).check(matches(
                    isDisplayed()))
            }
            indeks++
        }
    }
    fun odaberiPitanjeklikniTacanOdgovor(indeks:Int,pitanje:Pitanje){
        Espresso.onView(withId(R.id.navigacijaPitanja))
            .perform(NavigationViewActions.navigateTo(indeks))
        val odgovori = pitanje.opcije
        val tacan = pitanje.tacan
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovori!![tacan!!]))).inAdapterView(withId(R.id.odgovoriLista)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovori[tacan]))).inAdapterView(
            allOf(withId(R.id.odgovoriLista), hasDescendant(anyOf(
                withTextColor(GREEN),
                withBackground(GREEN))
            ), hasDescendant(not(anyOf(
                withTextColor(RED),
                withBackground(RED))
            )))).check(matches(
            isDisplayed()))
    }
    fun odaberiPitanjeklikniPogresanOdgovor(indeks:Int,pitanje: Pitanje){
        Espresso.onView(withId(R.id.navigacijaPitanja))
            .perform(NavigationViewActions.navigateTo(indeks))
        val odgovori = pitanje.opcije
        val tacan = pitanje.tacan
        var pogresan = 0
        while (pogresan==tacan&&pogresan< odgovori!!.size-1){pogresan++;}
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovori!![pogresan]))).inAdapterView(withId(R.id.odgovoriLista)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovori[tacan!!]))).inAdapterView(
            allOf(withId(R.id.odgovoriLista), hasDescendant(anyOf(
                withTextColor(RED),
                withBackground(RED))
            ), hasDescendant(anyOf(
                withTextColor(GREEN),
                withBackground(GREEN)
            )))).check(matches(
            isDisplayed()))
    }
    @Test
    fun A_tacanOdgovor(){
        val kvizovi = odaberiKviz()
        val pitanja = PitanjeKvizRepository.getPitanja(kvizovi[0].naziv, kvizovi[0].nazivPredmeta)
        odaberiPitanjeklikniTacanOdgovor(0,pitanja[0])
    }

    @Test
    fun B_pogresanOdgovor(){
        val kvizovi = odaberiKviz()
        val pitanja = PitanjeKvizRepository.getPitanja(kvizovi[0].naziv, kvizovi[0].nazivPredmeta)
        odaberiPitanjeklikniPogresanOdgovor(1,pitanja[1])
        Espresso.onView(withId(R.id.zaustaviKviz)).perform(click())
    }

    @Test
    fun C_provjeraNakonZaustavljana(){
        val kvizovi = odaberiKviz()
        val pitanja = PitanjeKvizRepository.getPitanja(kvizovi[0].naziv, kvizovi[0].nazivPredmeta)
        for(indeks in 0..1){
            Espresso.onView(withId(R.id.navigacijaPitanja))
                .perform(NavigationViewActions.navigateTo(indeks))
            Espresso.onView(withId(R.id.tekstPitanja))
                .check(matches(withText(pitanja[indeks].tekst)))
            val odgovori = pitanja[indeks].opcije

            if(indeks==0)
                onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovori!![pitanja[indeks].tacan!!]))).inAdapterView(
                    allOf(withId(R.id.odgovoriLista), hasDescendant(anyOf(
                        withTextColor(GREEN),
                        withBackground(GREEN))
                    ), hasDescendant(not(anyOf(
                        withTextColor(RED),
                        withBackground(RED))
                    )))).check(matches(
                    isDisplayed()))
            else {
                val odgovori = pitanja[indeks].opcije
                val tacan = pitanja[indeks].tacan
                var pogresan = 0
                while (pogresan==tacan&&pogresan< odgovori!!.size-1){pogresan++;}

                onData(allOf(`is`(instanceOf(String::class.java)), `is`(odgovori!![pogresan]))).inAdapterView(
                    allOf(withId(R.id.odgovoriLista), hasDescendant(anyOf(
                        withTextColor(RED),
                        withBackground(RED))
                    ), hasDescendant(anyOf(
                        withTextColor(GREEN),
                        withBackground(GREEN)
                    )))).check(matches(
                    isDisplayed()))
            }

        }
    }

    @Test
    fun predajIZavrsi(){
        val kvizovi = odaberiKviz()
        val pitanja = PitanjeKvizRepository.getPitanja(kvizovi[0].naziv, kvizovi[0].nazivPredmeta)
        if(pitanja.size>2){
            for (i in 2..pitanja.size-1){
                odaberiPitanjeklikniTacanOdgovor(i,pitanja[i])
            }
        }
        Espresso.onView(withId(R.id.predajKviz)).perform(click())
        Espresso.onView(withSubstring("Završili ste kviz ${UpisAndOdgovor.odabraniKviz}"))
    }
    private fun odaberiKviz(): List<Kviz> {
        Espresso.onView(withId(R.id.filterKvizova)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Svi moji kvizovi"))).perform(
            click()
        )
        val kvizovi = KvizRepository.getMyKvizes()
        Espresso.onView(withId(R.id.listaKvizova)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText(kvizovi[0].naziv)),
                    hasDescendant(withText(kvizovi[0].nazivPredmeta))
                ), click()
            )
        )
        return kvizovi
    }

}