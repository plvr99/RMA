package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView
import java.util.*

class FragmentPokusaj : Fragment() {
    lateinit var navigacijaPitanja : NavigationView
    private lateinit var pitanjeFrame : FrameLayout
    private lateinit var pitanja : List<Pitanje>
    var pitanjeKvizViewModel : PitanjeKvizViewModel = PitanjeKvizViewModel()
    lateinit var nazivKviza : String
    private lateinit var nazivPredmeta : String
    private lateinit var nazivGrupe : String
    lateinit var odgovor: Odgovor
    private var selectedPitanje : Int? = null
    private var brojTacnoOdgovorenih : Int = 0
    private val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            item ->
        if(item.title.equals("Rezultat")){
            val poruka = "Završili ste kviz $nazivKviza sa tačnosti ${odgovor.procenatTacnosti * 100} %"
            val frag = FragmentPoruka.newInstance(poruka)
            childFragmentManager.beginTransaction().replace(R.id.framePitanje, frag, "poruka").addToBackStack(null).commit()
            (activity as MainActivity).hideMenuItems(arrayListOf(2,3))
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

        println(pitanja)
        odgovor =
            if (pitanjeKvizViewModel.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe) != null){
                pitanjeKvizViewModel.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe)!!
            } else Odgovor(nazivKviza, nazivPredmeta, nazivGrupe, initFragments())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        pitanjeFrame = view.findViewById(R.id.framePitanje)
        navigacijaPitanja = view.findViewById(R.id.navigacijaPitanja)
        for (i in pitanja.indices) navigacijaPitanja.menu.add(R.id.nav_pitanja, i, i, (i+1).toString())
        navigacijaPitanja.menu.add(R.id.rezultat, pitanja.size, pitanja.size, "Rezultat")
        navigacijaPitanja.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigacijaPitanja.menu.get(pitanja.size).isVisible = false
        //oboji redne br pitanjaa
        for ( i in pitanja.indices)
            colorPitanje(odgovor.pitanja.get(i).tacnoOdgovoreno, i)

        if(odgovor.zavrseno) {
            (activity as MainActivity).hideMenuItems(arrayListOf(2,3))
            navigacijaPitanja.menu.get(pitanja.size).isVisible = true
        }
        return view
    }

    companion object {
        fun newInstance(pitanja : List<Pitanje>, nazivKviza : String, nazivPredmeta : String, nazivGrupe : String) : FragmentPokusaj{
            val fragment = FragmentPokusaj()
            val args = Bundle()
            args.putParcelableArrayList("pitanja", pitanja as ArrayList<out Parcelable>)
            args.putString("nazivKviza", nazivKviza)
            args.putString("nazivPredmeta", nazivPredmeta)
            args.putString("nazivGrupe", nazivGrupe)
            fragment.arguments=args
            return fragment
        }
    }
    private fun initFragments(): ArrayList<FragmentPitanje> {
        val fragmenti : ArrayList<FragmentPitanje> = arrayListOf()
        for (i in pitanja.indices) fragmenti.add(FragmentPitanje.newInstance(pitanja.get(i)))
        return fragmenti
    }

    private fun postaviPitanje(brojPitanja: Int) {
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
            updateProcenatTacnosti()
        }
        spanString.setSpan(ForegroundColorSpan(Color.parseColor(colorString)), 0, spanString.length, 0)
        (navigacijaPitanja.menu.getItem(position) as MenuItem).title = spanString
    }
    private fun updateProcenatTacnosti() {
        odgovor.procenatTacnosti = brojTacnoOdgovorenih.toFloat() / pitanja.size
    }

}