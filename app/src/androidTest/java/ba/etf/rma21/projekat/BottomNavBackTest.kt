package ba.etf.rma21.projekat

import android.view.View
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BottomNavBackTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)
    data class odabrano(var vrijednost:String="",var indeks:Int=-1)
    @Test
    fun KvizoviPredmetBack1() {

        Espresso.onView(ViewMatchers.withId(R.id.listaKvizova)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.kvizovi)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.predmeti)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.predmeti)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.odabirGodina)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());
        Espresso.onView(ViewMatchers.withId(R.id.listaKvizova)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }

    @Test
    fun KvizoviKvizBack1() {

        Espresso.onView(ViewMatchers.withId(R.id.listaKvizova)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.kvizovi)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.predmeti)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.filterKvizova)).perform(ViewActions.click())
        Espresso.onData(CoreMatchers.allOf(CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`("Svi moji kvizovi"))).perform(
            ViewActions.click())
        val kvizovi = KvizRepository.getMyKvizes()
        Espresso.onView(ViewMatchers.withId(R.id.listaKvizova)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                CoreMatchers.allOf(
                    ViewMatchers.hasDescendant(ViewMatchers.withText(kvizovi[0].naziv)),
                    ViewMatchers.hasDescendant(ViewMatchers.withText(kvizovi[0].nazivPredmeta))), ViewActions.click()))
        Espresso.onView(ViewMatchers.withId(R.id.navigacijaPitanja)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.predajKviz)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.zaustaviKviz)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());
        Espresso.onView(ViewMatchers.withId(R.id.kvizovi)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.predmeti)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()))
    }
    fun getSelected(vrijednost:odabrano)=object:TypeSafeMatcher<View>(){
        override fun describeTo(description: Description) {
            description.appendText("Nije tipa adapterview")
        }

        override fun matchesSafely(item: View?): Boolean {
            if(!(item is Spinner)) return false;
            if(vrijednost.indeks==-1)vrijednost.indeks=item.count-1
            vrijednost.vrijednost=item.getItemAtPosition(vrijednost.indeks).toString()
            return true;
        }

    }
    @Test
    fun odaberiGodinuIPredmetIBack(){
        onView(withId(R.id.predmeti)).perform(click())

        val odabranaGodina=odabrano()
        odabranaGodina.indeks=1
        val odabraniPredmet=odabrano()
        val odabranaGrupa=odabrano()

        onView(withId(R.id.odabirGodina)).check(matches(getSelected(odabranaGodina)))
        onView(withId(R.id.odabirGodina)).perform(click())
        onData(anything()).atPosition(odabranaGodina.indeks).perform(click())


        onView(withId(R.id.odabirPredmet)).check(matches(getSelected(odabraniPredmet)))
        onView(withId(R.id.odabirPredmet)).perform(click())
        onData(anything()).atPosition(odabraniPredmet.indeks).perform(click())

        onView(withId(R.id.odabirGrupa)).check(matches(getSelected(odabranaGrupa)))
        onView(withId(R.id.odabirGrupa)).perform(click())
        onData(anything()).atPosition(odabranaGrupa.indeks).perform(click())

        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack());

        onView(withId(R.id.predmeti)).perform(click())

        onView(withId(R.id.odabirGodina)).check(matches(hasDescendant(withText(odabranaGodina.vrijednost))))
        onView(withId(R.id.odabirPredmet)).check(matches(hasDescendant(withText(odabraniPredmet.vrijednost))))
        onView(withId(R.id.odabirGrupa)).check(matches(hasDescendant(withText(odabranaGrupa.vrijednost))))
    }

}