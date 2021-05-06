package ba.etf.rma21.projekat

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.number.OrderingComparison.greaterThan
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.time.LocalDate
import java.util.*

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UpisAndOdgovor {
    val GREEN = Color.parseColor("#3DDC84")
    val RED = Color.parseColor("#DB4F3D")
    @get:Rule
    val intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    private fun odaberiKviz(predmet:String,naziv:String) {
        onView(withId(R.id.filterKvizova)).perform(click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`("Svi moji kvizovi"))).perform(
            click()
        )
        onView(withId(R.id.listaKvizova)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                CoreMatchers.allOf(
                    hasDescendant(ViewMatchers.withText(naziv)),
                    hasDescendant(ViewMatchers.withText(predmet))
                ), click()
            )
        )
    }
    companion object{
        var odabraniKviz:String=""
        var odabraniPredmet:String=""
    }

    fun prijeDanas(datum:Date):Boolean{
        var godina = Calendar.getInstance().get(Calendar.YEAR)
        var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
        var dan = Calendar.getInstance().get(Calendar.DATE)
        var drugiDatum = Calendar.getInstance()
        drugiDatum.setTime(datum)
        var drugaGodina = drugiDatum.get(Calendar.YEAR)
        var drugiMjesec = drugiDatum.get(Calendar.MONTH)+1
        var drugiDan = drugiDatum.get(Calendar.DATE)
        if(drugaGodina>3900){
            drugaGodina=datum.year
            drugiMjesec=datum.month
            drugiDan=datum.day
        }
        if(godina>drugaGodina)return true
        else if(godina<drugaGodina) return  false
        else if(mjesec>drugiMjesec) return true
        else if(mjesec<drugiMjesec) return false
        else return dan>drugiDan
    }
    fun prijeDanas(datum:LocalDate):Boolean{
        return datum.isBefore(LocalDate.now())
    }
    fun odaberiPitanjeklikniTacanOdgovor(indeks:Int,pitanje: Pitanje){
        onView(withId(R.id.navigacijaPitanja))
            .perform(NavigationViewActions.navigateTo(indeks))
        val odgovori = pitanje.opcije
        val tacan = pitanje.tacan
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`(
            odgovori!![tacan!!]))).inAdapterView(withId(R.id.odgovoriLista)).perform(click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`(odgovori[tacan!!]))).inAdapterView(
            CoreMatchers.allOf(withId(R.id.odgovoriLista), hasDescendant(CoreMatchers.anyOf(
                UtilTestClass.withTextColor(GREEN),
                UtilTestClass.withBackground(GREEN))
            ), hasDescendant(CoreMatchers.not(CoreMatchers.anyOf(
                UtilTestClass.withTextColor(RED),
                UtilTestClass.withBackground(RED))
            )))).check(ViewAssertions.matches(
            ViewMatchers.isDisplayed()))
    }
    @Test
    fun A_upisValidan(){
        onView(withId(R.id.predmeti)).perform(click())
        onView(withId(R.id.odabirGodina)).perform(click())
        val nedodjeljeniKvizovi = KvizRepository.getAll().minus(KvizRepository.getMyKvizes())
        val nedodjeljeniPredmeti = PredmetRepository.getAll().minus(PredmetRepository.getUpisani())
        var grupaVrijednost = ""
        var predmetNaziv = ""
        var godinaVrijednost = -1

        vanjska@ for (nk in nedodjeljeniKvizovi) {
            for (np in nedodjeljeniPredmeti) {
                if (nk.nazivPredmeta == np.naziv&&(nk.datumRada==null||nk.osvojeniBodovi==null)&&!prijeDanas(nk.datumKraj)&&prijeDanas(nk.datumPocetka)) {
                    grupaVrijednost = nk.nazivGrupe
                    godinaVrijednost = np.godina
                    predmetNaziv = np.naziv
                    odabraniKviz=nk.naziv
                    break@vanjska
                }
            }
        }
        ViewMatchers.assertThat(
            "Nema neupisanih predmeta sa aktivnim kvizovima",
            godinaVrijednost,
            CoreMatchers.not(CoreMatchers.`is`(-1))
        )
        odabraniPredmet=predmetNaziv
        Espresso.onData(
            CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(godinaVrijednost.toString())
            )
        ).perform(click())
        onView(withId(R.id.odabirPredmet)).perform(click())
        Espresso.onData(
            CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(predmetNaziv)
            )
        ).perform(click())
        onView(withId(R.id.odabirGrupa)).perform(click())
        Espresso.onData(
            CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                CoreMatchers.`is`(grupaVrijednost)
            )
        ).perform(click())
        onView(withId(R.id.dodajPredmetDugme)).perform(click())
        onView(ViewMatchers.withSubstring("Uspješno ste upisani u grupu"))
        onView(withId(R.id.kvizovi)).perform(click())
    }


    @Test
    fun B_tacnoPrvoIZaustavi(){
        onView(withId(R.id.kvizovi)).perform(click())
        assertThat(odabraniPredmet.length,`is`(greaterThan(0)))
        assertThat(odabraniKviz.length,`is`(greaterThan(0)))
        odaberiKviz(odabraniPredmet,odabraniKviz)
        var pitanja = PitanjeKvizRepository.getPitanja(odabraniKviz, odabraniPredmet)
        for(i in pitanja.indices){
            odaberiPitanjeklikniTacanOdgovor(i,pitanja[i])
        }
        onView(withId(R.id.zaustaviKviz)).perform(click())
    }
    @Test
    fun C_povratakNaZaustavljeniIPredaj(){
        odaberiKviz(odabraniPredmet, odabraniKviz)
        var pitanja = PitanjeKvizRepository.getPitanja(odabraniKviz, odabraniPredmet)
        var indeks=0
        for(pitanje in pitanja){
            onView(withId(R.id.navigacijaPitanja))
                .perform(NavigationViewActions.navigateTo(indeks))
            onView(withId(R.id.tekstPitanja))
                .check(ViewAssertions.matches(ViewMatchers.withText(pitanja[indeks].tekst)))
            val odgovori = pitanja[indeks].opcije

            Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`(
                odgovori!![pitanja[indeks].tacan!!]))).inAdapterView(
                CoreMatchers.allOf(withId(R.id.odgovoriLista), hasDescendant(CoreMatchers.anyOf(
                    UtilTestClass.withTextColor(GREEN),
                    UtilTestClass.withBackground(GREEN))
                ), hasDescendant(CoreMatchers.not(CoreMatchers.anyOf(
                    UtilTestClass.withTextColor(RED),
                    UtilTestClass.withBackground(RED))
                )))).check(ViewAssertions.matches(
                ViewMatchers.isDisplayed()))

            indeks++
        }
        onView(withId(R.id.predajKviz)).perform(click())
        onView(ViewMatchers.withSubstring("Završili ste kviz ${odabraniKviz}"))
    }
}