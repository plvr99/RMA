package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Pitanje
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class FragmentPokusaj : Fragment() {
    private lateinit var navigacijaPitanja : NavigationView
    private lateinit var pitanjeFrame : FrameLayout
    private lateinit var pitanja : List<Pitanje>

    private val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        item ->
        println(item.title)
        postaviPitanje(item.title.toString().toInt())
        return@OnNavigationItemSelectedListener true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pitanja = it.getParcelableArrayList("pitanja")!!
        }
        println("PITANJAAAAAAAAAAAAAA")
        println(pitanja)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        pitanjeFrame = view.findViewById(R.id.framePitanje)
        navigacijaPitanja = view.findViewById(R.id.navigacijaPitanja)
        for (i in 1 .. pitanja.size) navigacijaPitanja.menu.add(R.id.nav_pitanja, i, i, i.toString())
        navigacijaPitanja.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        return view
    }

    companion object {
        fun newInstance(pitanja : List<Pitanje>) : FragmentPokusaj{
            val fragment = FragmentPokusaj();
            val args = Bundle()
            //todo put pitanja
            args.putParcelableArrayList("pitanja", pitanja as ArrayList<out Parcelable>)
            fragment.arguments=args
            return fragment
        }
    }
    fun postaviPitanje(brojPitanja: Int) {

        var pitanje : Pitanje = pitanja.get(brojPitanja-1)

        val nextFrag = FragmentPitanje.newInstance(pitanje)
        //todo CHILD FRAG MNGR
        childFragmentManager.beginTransaction().replace(R.id.framePitanje, nextFrag, "FRAG_PITANJE").addToBackStack(null).commit()
//        activity!!.supportFragmentManager.beginTransaction()
//            //.remove(this@FragmentPitanje)
//            .replace(R.id.framePitanje, nextFrag, "FRAG_PITANJE")
//            .addToBackStack(null)
//            .commit()

    }
}