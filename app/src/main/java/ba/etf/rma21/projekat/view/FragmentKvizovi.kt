package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel

class FragmentKvizovi : Fragment() {
    private lateinit var kvizoviRecyclerView: RecyclerView
    private lateinit var kvizAdapter: KvizAdapter
    private lateinit var spinner: Spinner
    private var kvizViewModel : KvizViewModel = KvizViewModel()
    //new
    private var pitanjeKvizViewModel : PitanjeKvizViewModel = PitanjeKvizViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_kvizovi, container, false)
        kvizoviRecyclerView = view.findViewById(R.id.listaKvizova)
        kvizoviRecyclerView.layoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)

        kvizAdapter = KvizAdapter(kvizViewModel.getAll()) {kviz -> otvoriKviz(kviz) }
        kvizoviRecyclerView.adapter = kvizAdapter

        spinner = view.findViewById(R.id.filterKvizova)

        ArrayAdapter.createFromResource(context!!, R.array.spinner_choice_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(KvizViewModel.odabraniKvizovi)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                prikaziKvizoveSaOpcijom(position)
                KvizViewModel.odabraniKvizovi = spinner.selectedItemPosition
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return view
    }
    override fun onResume() {
        prikaziKvizoveSaOpcijom(spinner.selectedItemPosition)
        super.onResume()
    }

    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()
    }
    fun prikaziKvizoveSaOpcijom(position: Int){
        when(position){
            0 ->{kvizAdapter.updateList(kvizViewModel.getMyKvizes().sortedBy { kviz -> kviz.datumPocetka })}
            1 ->{kvizAdapter.updateList(kvizViewModel.getAll().sortedBy { kviz -> kviz.datumPocetka })}
            2 ->{kvizAdapter.updateList(kvizViewModel.getDone().sortedBy { kviz -> kviz.datumPocetka })}
            3 ->{kvizAdapter.updateList(kvizViewModel.getFuture().sortedBy { kviz -> kviz.datumPocetka })}
            4 ->{kvizAdapter.updateList(kvizViewModel.getNotTaken().sortedBy { kviz -> kviz.datumPocetka })}
        }
    }

    private fun otvoriKviz(kviz : Kviz){
        val pitanja = pitanjeKvizViewModel.getPitanja(kviz.naziv, kviz.nazivPredmeta)
        when {
            kviz.odrediTipKviza() != 2 -> Toast.makeText(context, "Ne mozete pristupiti ovom kvizu", Toast.LENGTH_SHORT).show()
            pitanja.isEmpty() -> Toast.makeText(context, "Nema pitanja za ovaj kviz", Toast.LENGTH_SHORT).show()
            else -> {
                val nextFrag = FragmentPokusaj.newInstance(
                    pitanja,
                    kviz.naziv, kviz.nazivPredmeta, kviz.nazivGrupe
                )
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, nextFrag, "FRAG_POKUSAJ")
                    .addToBackStack(null)
                    .commit()
                val activity = activity as MainActivity
                activity.hideMenuItems(arrayListOf(0, 1))
            }
        }
    }

}