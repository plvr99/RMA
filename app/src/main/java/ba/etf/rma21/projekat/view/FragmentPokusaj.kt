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
import kotlin.math.roundToInt

class FragmentPokusaj : Fragment() {
    lateinit var navigacijaPitanja : NavigationView
    private lateinit var pitanjeFrame : FrameLayout
    lateinit var pitanja : List<Pitanje>
    var pitanjeKvizViewModel : PitanjeKvizViewModel = PitanjeKvizViewModel()
    var kvizId: Int = 0
    lateinit var nazivKviza : String
    private lateinit var nazivPredmeta : String
    private lateinit var nazivGrupe : String
    var idKvizTaken: Int = -1
    private lateinit var pitanjaFragmenti: List<FragmentPitanje>
    lateinit var odgovori: List<Odgovor>
    private var selectedPitanje : Int? = null
    private var brojTacnoOdgovorenih : Int = 0
    var osvojeniBodovi = 0
    private var zavrseno : Boolean = false
    private val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
            item ->
        if(item.title.equals("Rezultat")){
            val poruka = "Završili ste kviz $nazivKviza sa tačnosti ${osvojeniBodovi} %"
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
            kvizId = it.getInt("kvizId")
            nazivKviza = it.getString("nazivKviza")!!
            nazivPredmeta = it.getString("nazivPredmeta")!!
            nazivGrupe = it.getString("nazivGrupe")!!
            idKvizTaken = it.getInt("idKvizTaken")
        }
        pitanjeKvizViewModel.getOdgovoriKviz(idKvizTaken, funcReturn = ::dajOdgovore)

//            if (pitanjeKvizViewModel.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe) != null){
//                pitanjeKvizViewModel.pronadjiOdgovorZaKviz(nazivKviza, nazivPredmeta, nazivGrupe)!!
//            } else Odgovor(1,nazivKviza, nazivPredmeta, nazivGrupe, initFragments())

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
        navigacijaPitanja.menu[pitanja.size].isVisible = false

        // TODO: 1.6.2021 odrediti da li je kviz zavrsen pa ovo uraditi
//        if(pitanjaFragmenti.odgovoreno) {
//            (activity as MainActivity).hideMenuItems(arrayListOf(2,3))
//            navigacijaPitanja.menu.get(pitanja.size).isVisible = true
//        }
        return view
    }

    companion object {
        fun newInstance(
            pitanja: List<Pitanje>,
            kvizId: Int,
            nazivKviza: String,
            nazivPredmeta: String,
            nazivGrupe: String,
            idKvizTaken: Int
        ) : FragmentPokusaj{
            val fragment = FragmentPokusaj()
            val args = Bundle()
            args.putParcelableArrayList("pitanja", pitanja as ArrayList<out Parcelable>)
            args.putInt("kvizId", kvizId)
            args.putString("nazivKviza", nazivKviza)
            args.putString("nazivPredmeta", nazivPredmeta)
            args.putString("nazivGrupe", nazivGrupe)
            args.putInt("idKvizTaken", idKvizTaken)
            fragment.arguments=args
            return fragment
        }
    }
    private fun initFragments(): ArrayList<FragmentPitanje> {
        zavrseno = (odgovori.size == pitanja.size)
        println("ODGOVORI EMPTY IS " + odgovori.isEmpty().toString())
        println("ODGOVORI SIZE IS " + odgovori.size + " PITANJA SIZE IS "  + pitanja.size)
        if (odgovori.isEmpty()) odgovori = napraviOdgvore(pitanja)
        else if (odgovori.size != pitanja.size) odgovori = dodajOdgovoreUPostojece(odgovori, pitanja)
        println("ODGOVORI SIZE IS " + odgovori.size)
        val fragmenti : ArrayList<FragmentPitanje> = arrayListOf()

        val a = pitanja.withIndex().associate { it.value.id to it.index }
        odgovori = odgovori.sortedBy { a.get(it.pitanjeId) }

        for (i in odgovori.indices) fragmenti.add(FragmentPitanje.newInstance(pitanja.get(i), odgovori.get(i), zavrseno))
        return fragmenti
    }

    private fun dodajOdgovoreUPostojece(odgovori: List<Odgovor>, pitanja: List<Pitanje>): List<Odgovor>{
        val odgovori1 = arrayListOf<Odgovor>()
        odgovori1.addAll(odgovori)
        for (i in pitanja.indices){
            val id = pitanja[i].id
            if(odgovori1.find { odgovor -> odgovor.pitanjeId == id } == null) odgovori1.add(Odgovor(null, id, idKvizTaken, -1))
        }
        return odgovori1
    }

    private fun napraviOdgvore(pitanja: List<Pitanje>): List<Odgovor> {
        val odgovori = arrayListOf<Odgovor>()
        for (i in pitanja.indices) odgovori.add(Odgovor(null, pitanja[i].id, idKvizTaken, -1))
        return odgovori
    }

    private fun postaviPitanje(brojPitanja: Int) {
        val nextFrag = pitanjaFragmenti.get(brojPitanja-1)
//        println("Odgovor selektovano je " + odgovori.get(brojPitanja-1).odgovoreno)
        childFragmentManager.beginTransaction().replace(R.id.framePitanje, nextFrag, "FRAG_PITANJE").addToBackStack(null).commit()
    }

    fun colorPitanje(colorAnswer: Boolean?, position: Int? = selectedPitanje) {
        if (colorAnswer == null) return
        val spanString = SpannableString((navigacijaPitanja.menu.getItem(position!!) as MenuItem ).title.toString())
        var colorString  = "#DB4F3D"
        if (colorAnswer) {
            colorString = "#3DDC84"
            brojTacnoOdgovorenih++
            updateOsvojeniBodovi()
        }
        spanString.setSpan(ForegroundColorSpan(Color.parseColor(colorString)), 0, spanString.length, 0)
        (navigacijaPitanja.menu.getItem(position) as MenuItem).title = spanString
    }
    private fun updateOsvojeniBodovi() {
        osvojeniBodovi = ((brojTacnoOdgovorenih.toDouble() / pitanja.size) * 100).roundToInt()
        println("OSVOJENI BODOVI " + osvojeniBodovi + " broj tacno odg " + brojTacnoOdgovorenih)
    }
    private fun dajOdgovore(odgovori1 : List<Odgovor>) {

        odgovori = odgovori1
        println(odgovori)
        println(pitanja)

        if (odgovori.size == pitanja.size){
            navigacijaPitanja.menu.get(pitanja.size).isVisible = true
            (requireActivity() as MainActivity).hideMenuItems(arrayListOf(2,3))
        }

        pitanjaFragmenti = initFragments()

        for ( i in pitanja.indices) {
            if(zavrseno) colorPitanje(pitanja[i].tacan == odgovori[i].odgovoreno, i)
            else if(odgovori[i].odgovoreno != -1) colorPitanje(pitanja[i].tacan == odgovori[i].odgovoreno, i)
        }
    }
    fun sacuavajOdgovor(pitanjeId : Int, odgovoreno : Int) {
        println("SACUVAJ ODGOVORE POZVAN IDKVIZTAKEN = " + idKvizTaken)
        println("pitanjeid: " + pitanjeId + " odgovoreno: "  + odgovoreno)
        // f-ja za MainActivity sacuvajOdgovore()
        pitanjeKvizViewModel.postaviOdgovorKviz(idKvizTaken, pitanjeId, odgovoreno)
    }

}