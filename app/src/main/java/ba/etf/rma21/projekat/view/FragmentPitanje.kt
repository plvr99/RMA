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
import ba.etf.rma21.projekat.data.models.Pitanje

class FragmentPitanje : Fragment() {
    private var pitanje : Pitanje? = null
    private lateinit var ponudjeniOdgovori : ListView
    private lateinit var naslov : TextView
    var odgovorenaPozicija : Int = -1
    //private var parentFragmentPokusaj : FragmentPokusaj = parentFragment as FragmentPokusaj
    var tacnoOdgovoreno : Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pitanje = it.getParcelable("pitanje")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        naslov = view.findViewById(R.id.tekstPitanja)
        naslov.text = pitanje?.tekst ?: ""
        ponudjeniOdgovori = view.findViewById(R.id.odgovoriLista)
        val adapterOdgovori= object : ArrayAdapter<String>(
            view.context!!,
            android.R.layout.simple_list_item_1,
            pitanje!!.opcije!!
        )
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val text = super.getView(position, convertView, parent)
                if(odgovorenaPozicija != -1) {
                    if (position == pitanje!!.tacan!!) (text as TextView).setBackgroundColor(Color.parseColor("#3DDC84"))
                    if (position == odgovorenaPozicija && odgovorenaPozicija != pitanje!!.tacan) (text as TextView).setBackgroundColor(Color.parseColor("#DB4F3D"))
                }
                    return text
            }
        }
        ponudjeniOdgovori.adapter = adapterOdgovori
        if(odgovorenaPozicija != -1 || odgovorenaPozicija ==-2) ponudjeniOdgovori.isEnabled=false
        ponudjeniOdgovori.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tacnoOdgovoreno = colorAnswer(view, position)
                (parentFragment as FragmentPokusaj).colorPitanje(tacnoOdgovoreno!!)
                ponudjeniOdgovori.isEnabled=false
                odgovorenaPozicija = position
            }
        }
        return view
    }

    private fun colorAnswer(view: View?, redniBroj: Int) :Boolean {
        val text = view as TextView
        if(redniBroj.equals(pitanje!!.tacan)) text.setBackgroundColor(Color.parseColor("#3DDC84")) //Zelena
        else{
            text.setBackgroundColor(Color.parseColor("#DB4F3D"))//crvena
            //ponudjeniOdgovori.performItemClick(ponudjeniOdgovori.getChildAt(pitanje!!.tacan!!), pitanje!!.tacan!!, ponudjeniOdgovori.adapter.getItemId(pitanje!!.tacan!!))
            (ponudjeniOdgovori.getChildAt(pitanje!!.tacan!!) as TextView).setBackgroundColor(Color.parseColor("#3DDC84"))
            return false
        }
        return true
    }

    companion object {
        fun newInstance(pitanje: Pitanje) : FragmentPitanje {
            val fragment = FragmentPitanje()
            val args = Bundle()
            args.putParcelable("pitanje", pitanje)
            fragment.arguments=args
            return fragment
        }
    }

}