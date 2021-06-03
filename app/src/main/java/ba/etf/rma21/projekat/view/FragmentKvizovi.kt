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
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel

class FragmentKvizovi : Fragment() {
    private lateinit var kvizoviRecyclerView: RecyclerView
    private lateinit var kvizAdapter: KvizAdapter
    private lateinit var spinner: Spinner
    private var kvizViewModel : KvizViewModel = KvizViewModel()
    private var pitanjeKvizViewModel : PitanjeKvizViewModel = PitanjeKvizViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_kvizovi, container, false)
        kvizoviRecyclerView = view.findViewById(R.id.listaKvizova)
        kvizoviRecyclerView.layoutManager = GridLayoutManager(activity,2,RecyclerView.VERTICAL,false)
        spinner = view.findViewById(R.id.filterKvizova)
        kvizAdapter = KvizAdapter(arrayListOf()) { kviz -> otvoriKviz(kviz) }
        kvizoviRecyclerView.adapter = kvizAdapter
//        kvizAdapter.updateList(arrayListOf())
      //  kvizViewModel.init(initFunc = ::adapterStart)

        ArrayAdapter.createFromResource(requireContext(), R.array.spinner_choice_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                prikaziKvizoveSaOpcijom(position)
                KvizViewModel.odabraniKvizovi = spinner.selectedItemPosition
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        spinner.setSelection(KvizViewModel.odabraniKvizovi)
        return view
    }
    override fun onResume() {
        prikaziKvizoveSaOpcijom(spinner.selectedItemPosition)
        super.onResume()
    }

    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()
    }

    private fun adapterStart(kvizovi : List<Kviz>){
        kvizAdapter = KvizAdapter(kvizovi) { kviz -> otvoriKviz(kviz) }
        kvizoviRecyclerView.adapter = kvizAdapter
    }

    fun prikaziKvizoveSaOpcijom(position: Int){
        when(position){
            0 ->kvizViewModel.getMyKvizes(showKvizovi = ::showKvizovi)
            1 ->kvizViewModel.getAll(showKvizovi = ::showKvizovi)
            2 ->kvizViewModel.getDone(showKvizovi = ::showKvizovi)
            3 ->kvizViewModel.getFuture(showKvizovi = ::showKvizovi)
            4 ->kvizViewModel.getNotTaken(showKvizovi = ::showKvizovi)
        }
        spinner.setSelection(position)
    }

    private fun showKvizovi(list: List<Kviz>){
        println("showKvizovi lista size je " + list.size)
//        kvizAdapter = KvizAdapter(list.sortedBy { kviz -> kviz.datumPocetka }) { kviz -> otvoriKviz(kviz) }
//        kvizoviRecyclerView.adapter = kvizAdapter

        kvizAdapter.updateList(list.sortedBy { kviz -> kviz.datumPocetka })
    }

    private fun otvoriKviz(kviz : Kviz){
        pitanjeKvizViewModel.zapocniKviz(kviz, funcSucces = ::zapocniKviz, funcError = ::errorOtvaranjeKviza)

    }
    private fun zapocniKviz(pitanja : List<Pitanje>, kviz: Kviz, idKvizTaken: Int){
        val nextFrag = FragmentPokusaj.newInstance(
            pitanja,
            kviz.id, kviz.naziv, kviz.nazivPredmeta, kviz.nazivGrupe,
            idKvizTaken
        )
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, nextFrag, "FRAG_POKUSAJ")
            .addToBackStack(null)
            .commit()
        val activity = activity as MainActivity
        activity.hideMenuItems(arrayListOf(0, 1))
    }
    private fun errorOtvaranjeKviza(message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    //napravi ili nadji KVIZTAKEN,
    //u svakom slucaju naci PITANJA za kviz, te poslati pitanja, naziv kviza, nazivpredmeta, nazivGrupe(???????)
    //u fragment pokusaju -----initFragments() za pitanja sa odgovorima
    //U fragment pokusaju na osnovu kvizTakena naci ODGOVORE, ako postoje na osnovu njih odluciti da li ce odgovoaranje biti disableano ili
    //ne,
// ako odgovori vec postoje obojiti broj pitanja i odgovore na pitanja
    //itd.

}