package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.viewmodel.KvizViewModel


class FragmentPoruka : Fragment() {
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString("string")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_poruka, container, false)
        if (message!!.startsWith("Uspješno")){
            KvizViewModel.odabranaGodina = 0
            KvizViewModel.odabraniPredmet = -1
            KvizViewModel.odabranaGrupa = -1
        }
        val textView : TextView= view.findViewById(R.id.tvPoruka)
        textView.text = message
        return view
    }

    companion object {
        fun newInstance(string: String) : FragmentPoruka{
            val fragment = FragmentPoruka();
            val args = Bundle()
            args.putString("string", string)
            fragment.arguments=args
            return fragment
        }
    }
}