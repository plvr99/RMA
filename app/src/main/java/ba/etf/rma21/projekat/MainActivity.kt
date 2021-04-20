package ba.etf.rma21.projekat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.view.FragmentKvizovi
import ba.etf.rma21.projekat.view.FragmentPoruka
import ba.etf.rma21.projekat.view.FragmentPredmeti
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity(){
    private lateinit var bottomNavigation: BottomNavigationView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_kvizovi -> {
                println("FRAG_KVIZOVI")
                println(bottomNavigation.menu.size())
                val kvizFragment = FragmentKvizovi.newInstance()
                hideMenuItems(arrayListOf(2,3))
                openFragment(kvizFragment, "FRAG_KVIZOVI")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_predmeti -> {
                println("FRAG_PREDMETI")
                println(bottomNavigation.menu.size())
                val predmetFragment = FragmentPredmeti.newInstance()
                hideMenuItems(arrayListOf(2,3))
                openFragment(predmetFragment, "FRAG_PREDMETI")
                return@OnNavigationItemSelectedListener true
            }
            R.id.predajKviz ->{
                //todo popravit
                println(bottomNavigation.menu.size())
            }
            R.id.zaustaviKviz ->{
                //todo popravit
                println("FRAG_KVIZOVI")
                println(bottomNavigation.menu.size())
                val kvizFragment = FragmentKvizovi.newInstance()
                hideMenuItems(arrayListOf(2,3))
                openFragment(kvizFragment, "FRAG_KVIZOVI")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigation = findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId = R.id.navigation_kvizovi
        val kvizFragment = FragmentKvizovi.newInstance()
        if(savedInstanceState == null) openFragment(kvizFragment, "FRAG_KVIZOVI")
            //TODO ROTATE FIX

    }

    private fun openFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        //todo wtf container
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)

        transaction.commit()
    }

    override fun onBackPressed() {
        when {
            getVisibleFragment() is FragmentKvizovi -> {
                return
            }
            getVisibleFragment() is FragmentPredmeti -> {
                super.onBackPressed()
                bottomNavigation.selectedItemId = R.id.navigation_kvizovi
            }
            getVisibleFragment() is FragmentPoruka -> {
                openFragment(FragmentKvizovi.newInstance(), "FRAG_KVIZOVI")
                bottomNavigation.selectedItemId = R.id.navigation_kvizovi
            }
        }
    }
    private fun getVisibleFragment(): Fragment? {
        val fragmentManager = this@MainActivity.supportFragmentManager
        for (fragment in fragmentManager.fragments) {
            if (fragment != null && fragment.isVisible) return fragment
        }
        return null
    }
    fun hideMenuItems(id : List<Int>){
        for (i in 0 until bottomNavigation.menu.size()) bottomNavigation.menu.get(i).isVisible= true
        for (i in id) bottomNavigation.menu.get(i).isVisible= false
    }
}

