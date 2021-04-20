package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R


class FragmentPoruka : Fragment() {
    // TODO: Rename and change types of parameters
    private var grupa: String? = null
    private var predmet: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            grupa = it.getString("grupa")
            predmet = it.getString("predmet")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_poruka, container, false)
        val textView : TextView= view.findViewById(R.id.tvPoruka)
        textView.text = "Uspješno ste upisani u grupu $grupa predmeta $predmet!"
        return view
    }

    companion object {
        fun newInstance(grupa: String, predmet: String) : FragmentPoruka{
            val fragment = FragmentPoruka();
            val args = Bundle()
            args.putString("grupa", grupa)
            args.putString("predmet", predmet)
            fragment.arguments=args
            return fragment
        }
    }
}