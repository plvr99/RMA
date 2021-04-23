package ba.etf.rma21.projekat

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.OdgovoriRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MySpirala2AndroidTest{
    @get:Rule
    val intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)
    @Before
    fun init() {
        OdgovoriRepository.odgovori.clear()
    }
    @Test
    fun MySpirala2AndroidTest1() {
        onView(withId(R.id.filterKvizova)).check(matches(isDisplayed()))
        onView(withId(R.id.filterKvizova)).perform(click())
        onData(
            CoreMatchers.allOf(
                CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)), CoreMatchers.`is`(
                    "Svi moji kvizovi"
                )
            )
        ).perform(click())
        val kvizovi = KvizRepository.getMyKvizes()
        onView(withId(R.id.listaKvizova)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                CoreMatchers.allOf(
                    hasDescendant(
                        withText(
                            kvizovi[0].naziv
                        )
                    ),
                    hasDescendant(withText(kvizovi[0].nazivPredmeta))
                ), click()
            )
        )
        onView(withId(R.id.navigacijaPitanja)).check(matches(isDisplayed()))
        onView(withId(R.id.navigacijaPitanja)).check(matches(isDisplayed()))
        onView(withId(R.id.navigacijaPitanja)).perform(NavigationViewActions.navigateTo(0))
        onData(anything()).inAdapterView(withId(R.id.odgovoriLista)).atPosition(0).perform(click());
        onView(withId(R.id.predajKviz)).perform(click())
        onView(withSubstring("50")).check(matches(isDisplayed()))
        onView(withId(R.id.kvizovi)).check(matches(isDisplayed()))
        onView(withId(R.id.kvizovi)).perform(click())
        onView(withId(R.id.listaKvizova)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                CoreMatchers.allOf(
                    hasDescendant(
                        withText(
                            kvizovi[0].naziv
                        )
                    ),
                    hasDescendant(withText(kvizovi[0].nazivPredmeta))
                ), click()
            )
        )
        onView(withId(R.id.navigacijaPitanja)).check(matches(isDisplayed()))
        onView(withId(R.id.navigacijaPitanja)).check(matches(isDisplayed()))
        //click on "Rezultat"
        onView(withId(R.id.navigacijaPitanja)).perform(NavigationViewActions.navigateTo(2))
        onView(withSubstring("Završili ste kviz")).check(matches(isDisplayed()))
    }

    @Test
    fun MySpirala2AndroidTest2() {
        onView(withId(R.id.filterKvizova)).check(matches(isDisplayed()))
        onView(withId(R.id.predajKviz)).check(matches(not(isDisplayed())))
        onView(withId(R.id.zaustaviKviz)).check(matches(not(isDisplayed())))
        onView(withId(R.id.predmeti)).perform(click())
        onView(withId(R.id.odabirGodina)).check(matches(isDisplayed()))
        onView(withId(R.id.dodajPredmetDugme)).check(matches(isDisplayed()))
        onView(withId(R.id.dodajPredmetDugme)).perform(click())
        onView(withSubstring("Uspješno ste upisani")).check(matches(isDisplayed()))
        onView(withId(R.id.kvizovi)).perform(click())
        try {
            onView(withId(R.id.odabirGodina)).check(matches(isDisplayed()))
            return assert(false)
        } catch (e: NoMatchingViewException) {}
        onView(withId(R.id.filterKvizova)).check(matches(isDisplayed()))
        onView(withId(R.id.predmeti)).check(matches(not(isSelected())))
    }
}