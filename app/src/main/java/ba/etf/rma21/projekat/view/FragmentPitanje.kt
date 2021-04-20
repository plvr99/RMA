package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Pitanje
import java.util.ArrayList

class FragmentPitanje : Fragment() {
    private var pitanje : Pitanje? = null
    private lateinit var odgovori : ListView
    private lateinit var naslov : TextView
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
        println("ZZZZZZZZZZZ")
        println(parentFragment)
        val view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        naslov = view.findViewById(R.id.tekstPitanja)
        naslov.text = pitanje?.tekst ?: ""
        //todo popuniti listu odgovora nekako
        odgovori = view.findViewById(R.id.odgovoriLista)

        val adapterOdgovori: ArrayAdapter<String> = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,

            pitanje!!.opcije!!
        )
        odgovori.adapter = adapterOdgovori
        odgovori.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                colorAnswer(view, position)
                odgovori.isEnabled=false
                //informisi pokusaj
                //sacuvaj state ili odgovore
            }
        }

        return view
    }

    private fun colorAnswer(view: View?, redniBroj: Int) :Boolean {
        println(redniBroj)
        val text = view as TextView
        if(redniBroj.equals(pitanje!!.tacan)) text.setTextColor(Color.parseColor("#3DDC84")) //Zelena
        else{
            text.setTextColor(Color.parseColor("#DB4F3D"))//crvena
            odgovori.performItemClick(odgovori.getChildAt(pitanje!!.tacan!!), pitanje!!.tacan!!, odgovori.adapter.getItemId(pitanje!!.tacan!!))
            return false
        }
        return true
    }

    companion object {
        fun newInstance(pitanje: Pitanje) : FragmentPitanje {
            val fragment = FragmentPitanje();
            val args = Bundle()
            args.putParcelable("pitanje", pitanje)
            fragment.arguments=args
            return fragment
        }
    }

}