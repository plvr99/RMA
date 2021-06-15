package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel

class FragmentPitanje : Fragment() {
    var pitanje : Pitanje? = null
    lateinit var odgovor: Odgovor
    private lateinit var ponudjeniOdgovori : ListView
    private lateinit var naslov : TextView
    var zavrseno : Boolean = false
    var tacnoOdgovoreno : Boolean? = null
    private val pitanjeKvizViewModel = PitanjeKvizViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pitanje = it.getParcelable("pitanje")
            odgovor = it.getParcelable("odgovor")!!
            zavrseno = it.getBoolean("zavrseno")
        }
        if(!zavrseno) zavrseno = odgovor.odgovoreno != -1
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        naslov = view.findViewById(R.id.tekstPitanja)
        naslov.text = pitanje?.tekstPitanja ?: ""
        ponudjeniOdgovori = view.findViewById(R.id.odgovoriLista)
        val adapterOdgovori= object : ArrayAdapter<String>(
            view.context!!,
            android.R.layout.simple_list_item_1,
            pitanje!!.opcije!!.split(",")
        )
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                println("POSITIONNNN $position")
                val text = super.getView(position, convertView, parent)
                if (position == pitanje!!.tacan!! && zavrseno) text.setBackgroundColor(Color.parseColor("#3DDC84")) //Zelena
                if(position == odgovor.odgovoreno) {
                    if (zavrseno && odgovor.odgovoreno != -1) {
                        if ( odgovor.odgovoreno != pitanje!!.tacan!!) text.setBackgroundColor(Color.parseColor("#DB4F3D"))//crvena
                    }
                }
                return text
            }
        }
        ponudjeniOdgovori.adapter = adapterOdgovori
        if(odgovor.odgovoreno != -1 || zavrseno) ponudjeniOdgovori.isEnabled=false
        ponudjeniOdgovori.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tacnoOdgovoreno = colorAnswer(view, position)
                (parentFragment as FragmentPokusaj).colorPitanje(tacnoOdgovoreno!!)
                ponudjeniOdgovori.isEnabled=false
                odgovor.odgovoreno = position
                sacuavajOdgovor(pitanje!!.id, odgovor.odgovoreno)
                zavrseno = true
            }
        }

        return view
    }

    fun sacuavajOdgovor(pitanjeId : Int, odgovoreno : Int) {
        val idKvizTaken = (parentFragment as FragmentPokusaj).idKvizTaken
            pitanjeKvizViewModel.postaviOdgovorKviz(idKvizTaken, pitanjeId, odgovoreno)
    }

    private fun colorAnswer(view: View?, redniBroj: Int) :Boolean {
        val text = view as TextView
        if(redniBroj.equals(pitanje!!.tacan)) text.setBackgroundColor(Color.parseColor("#3DDC84")) //Zelena
        else{
            text.setBackgroundColor(Color.parseColor("#DB4F3D"))//crvena
            (ponudjeniOdgovori.getChildAt(pitanje!!.tacan!!) as TextView).setBackgroundColor(Color.parseColor("#3DDC84"))
            return false
        }
        return true
    }

    companion object {
        fun newInstance(pitanje: Pitanje, odgovor: Odgovor, zavrseno: Boolean) : FragmentPitanje {
            val fragment = FragmentPitanje()
            val args = Bundle()
            args.putParcelable("pitanje", pitanje)
            args.putParcelable("odgovor", odgovor)
            args.putBoolean("zavrseno", zavrseno)
            fragment.arguments=args
            return fragment
        }
    }

}