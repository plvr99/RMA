package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.get
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.OdgovoriRepository
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.collections.ArrayList

class FragmentPokusaj : Fragment() {
    private lateinit var navigacijaPitanja : NavigationView
    private lateinit var pitanjeFrame : FrameLayout
    private lateinit var pitanja : List<Pitanje>
    var pitanjeKvizViewModel : PitanjeKvizViewModel = PitanjeKvizViewModel()
    lateinit var nazivKviza : String
    private lateinit var nazivPredmeta : String
    private lateinit var nazivGrupe : String
    lateinit var odgovor: Odgovor
    private var selectedPitanje : Int? = null
    var brojTacnoOdgovorenih : Int = 0
    private val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
        item ->
        if(item.title.equals("Rezultat")){
            val poruka = "Završili ste kviz ${nazivKviza} sa tačnosti ${procenatTacnosti()}"
            val frag = FragmentPoruka.newInstance(poruka)
            activity!!.supportFragmentManager.beginTransaction().replace(R.id.container, frag, "poruka").commit()
        }
        else {
            selectedPitanje = item.title.toString().toInt() - 1
            postaviPitanje(item.title.toString().toInt())
        }
        return@OnNavigationItemSelectedListener true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pitanja = it.getParcelableArrayList("pitanja")!!
            nazivKviza = it.getString("nazivKviza")!!
            nazivPredmeta = it.getString("nazivPredmeta")!!
            nazivGrupe = it.getString("nazivGrupe")!!
        }
        println("PITANJAAAAAAAAAAAAAA")
        println(pitanja)
        if (pitanjeKvizViewModel.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe) != null){
            odgovor = pitanjeKvizViewModel.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe)!!
            }
        else odgovor = Odgovor(nazivKviza, nazivPredmeta, nazivGrupe, initFragments())
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
        navigacijaPitanja.menu.add(R.id.rezultat, pitanja.size, pitanja.size, "Rezultat")
        navigacijaPitanja.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigacijaPitanja.menu.get(pitanja.size).isVisible = false
        //oboji redne br pitanjaa
        for ( i in 0 until pitanja.size)
            colorPitanje(odgovor.pitanja.get(i).tacnoOdgovoreno, i)

        if(odgovor.zavrseno) {
                (activity as MainActivity).hideMenuItems(arrayListOf(0,1,2,3))
            navigacijaPitanja.menu.get(pitanja.size).isVisible = true
        }
        return view
    }

    companion object {
        fun newInstance(pitanja : List<Pitanje>, nazivKviza : String, nazivPredmeta : String, nazivGrupe : String) : FragmentPokusaj{
            val fragment = FragmentPokusaj();
            val args = Bundle()
            args.putParcelableArrayList("pitanja", pitanja as ArrayList<out Parcelable>)
            args.putString("nazivKviza", nazivKviza)
            args.putString("nazivPredmeta", nazivPredmeta)
            args.putString("nazivGrupe", nazivGrupe)
            fragment.arguments=args
            return fragment
        }
    }
    fun initFragments(): ArrayList<FragmentPitanje> {
        val fragmenti : ArrayList<FragmentPitanje> = arrayListOf()
        for (i in 0 until pitanja.size) fragmenti.add(FragmentPitanje.newInstance(pitanja.get(i)))
        return fragmenti
    }

    fun postaviPitanje(brojPitanja: Int) {
        val nextFrag = odgovor.pitanja.get(brojPitanja-1)
        childFragmentManager.beginTransaction().replace(R.id.framePitanje, nextFrag, "FRAG_PITANJE").addToBackStack(null).commit()
    }

    fun colorPitanje(colorAnswer: Boolean?, position: Int? = selectedPitanje) {
        if (colorAnswer == null) return
        val spanString = SpannableString((navigacijaPitanja.menu.getItem(position!!) as MenuItem ).title.toString())
        var colorString  = "#DB4F3D"
        if (colorAnswer) {
            colorString = "#3DDC84"
            brojTacnoOdgovorenih++
        }
        spanString.setSpan(ForegroundColorSpan(Color.parseColor(colorString)), 0, spanString.length, 0)
        (navigacijaPitanja.menu.getItem(position) as MenuItem).title = spanString
    }
    fun procenatTacnosti(): Float {
        return (brojTacnoOdgovorenih.toFloat() / pitanja.size)
    }

}