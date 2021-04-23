package ba.etf.rma21.projekat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.view.FragmentKvizovi
import ba.etf.rma21.projekat.view.FragmentPokusaj
import ba.etf.rma21.projekat.view.FragmentPoruka
import ba.etf.rma21.projekat.view.FragmentPredmeti
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    var pitanjeKvizViewModel: PitanjeKvizViewModel = PitanjeKvizViewModel()
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.kvizovi -> {
                    //println("FRAG_KVIZOVI")
                    val kvizFragment = FragmentKvizovi.newInstance()
                    hideMenuItems(arrayListOf(2, 3))
                    openFragment(kvizFragment, "FRAG_KVIZOVI")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.predmeti -> {
                    //println("FRAG_PREDMETI")
                    val predmetFragment = FragmentPredmeti.newInstance()
                    hideMenuItems(arrayListOf(2, 3))
                    openFragment(predmetFragment, "FRAG_PREDMETI")
                    return@OnNavigationItemSelectedListener true
                }
                R.id.predajKviz -> {
                    //prikazi rez
                    val frag = getVisibleFragment() as FragmentPokusaj
                    val string =
                        "Završili ste kviz ${frag.nazivKviza} sa tačnosti ${frag.odgovor.procenatTacnosti * 100} %"
                    val poruka = FragmentPoruka.newInstance(string)
                    //sacuvaj odgovore
                    disableSelectionOdgovora(frag.odgovor)
                    sacuvajOdgovor(frag.odgovor, true)
                    hideMenuItems(arrayListOf(2, 3))
                    openFragment(poruka, "poruka")
                }
                R.id.zaustaviKviz -> {
                    //saqcuvaj odgovore
                    onBackPressed()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("MENU_OPTION", bottomNavigation.selectedItemId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.kvizovi
        val kvizFragment = FragmentKvizovi.newInstance()
        if (savedInstanceState == null) openFragment(kvizFragment, "FRAG_KVIZOVI")
        else bottomNavigation.selectedItemId = savedInstanceState.getInt("MENU_OPTION")
    }

    private fun openFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, tag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        //ako je pokusaj sacuvaj odgovore pa zatvori
        if (getVisibleFragment() is FragmentPokusaj)
            sacuvajOdgovor((getVisibleFragment() as FragmentPokusaj).odgovor, false)
        while (supportFragmentManager.backStackEntryCount > 1) supportFragmentManager.popBackStackImmediate()
        bottomNavigation.selectedItemId = R.id.kvizovi

    }

    private fun getVisibleFragment(): Fragment? {
        val fragmentManager = this@MainActivity.supportFragmentManager
        for (fragment in fragmentManager.fragments) {
            if (fragment != null && fragment.isVisible) return fragment
        }
        return null
    }

    fun hideMenuItems(id: List<Int>) {
        for (i in 0 until bottomNavigation.menu.size()) bottomNavigation.menu.get(i).isVisible =
            true
        for (i in id) bottomNavigation.menu.get(i).isVisible = false
    }

    private fun disableSelectionOdgovora(odgovor: Odgovor) {
        //disableat ce svaki list view fragmenata
        for (i in 0 until odgovor.pitanja.size)
            if (odgovor.pitanja.get(i).odgovorenaPozicija == -1) odgovor.pitanja.get(i).odgovorenaPozicija = -2
    }

    private fun sacuvajOdgovor(odgovor: Odgovor, zavrseno: Boolean) {
        odgovor.zavrseno = zavrseno
        pitanjeKvizViewModel.dodajOdgovor(odgovor)
    }
}


