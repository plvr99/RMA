package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import java.util.*


class FragmentPredmeti : Fragment() {
    var kvizModel : KvizViewModel = KvizViewModel()
    lateinit var spinnerGodina : Spinner
    lateinit var spinnerPredmet : Spinner
    lateinit var spinnerGrupa : Spinner
    lateinit var button : Button
    lateinit var adapterPredmet : ArrayAdapter<String>
    lateinit var adapterGrupa : ArrayAdapter<String>
    var predmetChoiceList : List<String> = arrayListOf()
    var groupChoiceList : List<String> = arrayListOf()
    var predmet = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_predmeti, container, false)
        spinnerPredmet = view.findViewById(R.id.odabirPredmet)
        spinnerGrupa  = view.findViewById(R.id.odabirGrupa)
        spinnerGodina = view.findViewById(R.id.odabirGodina)
        button = view.findViewById(R.id.dodajPredmetDugme)
        button.isVisible = false
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.godina_choice_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGodina.adapter = adapter
        }

        adapterPredmet = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, predmetChoiceList
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        adapterGrupa = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, groupChoiceList
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerPredmet.adapter = adapterPredmet
        spinnerGrupa.adapter = adapterGrupa

        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                Toast.makeText(context, "Ucitavanje...", Toast.LENGTH_SHORT).show()
                KvizViewModel.odabranaGodina = position
                adapterPredmet.clear()
                adapterGrupa.clear()
                // println("spinner godina pozvan")
                kvizModel.dajNeupisanePredmete(populateWithPredmet = ::populateWithPredmet)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerPredmet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                println("spinner predmet pozvan")
                //val predmet : String = spinnerPredmet.getItemAtPosition(position).toString()
                predmet = predmetChoiceList[position]
                println("Predmet koji se ubacuje za pretragu je " + predmet)
                kvizModel.dajSveGrupeZaPredmet(predmet, populateWithGrupe = ::populateWithGrupe)
//                groupChoiceList = kvizModel.dajSveGrupeZaPredmet(predmet).map { grupa -> grupa.toString() }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                button.isVisible = false
                adapterGrupa.clear()
                adapterGrupa.notifyDataSetChanged()
            }
        }
        spinnerGrupa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //println("spinner grupa pozvan")
                button.isVisible=true
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                button.isVisible=false
            }
        }
        button.setOnClickListener {
            upisiNaPredmet(
                spinnerGodina.selectedItemPosition,
                spinnerPredmet.selectedItem.toString(),
                spinnerGrupa.selectedItem.toString()
            )
            obavijestiKorisnika(
                spinnerGrupa.selectedItem.toString(),
                spinnerPredmet.selectedItem.toString()
            )
        }

        spinnerGodina.setSelection(KvizViewModel.odabranaGodina, false)
        //loadSpinners()
        return view
    }

    companion object {
        fun newInstance() : FragmentPredmeti = FragmentPredmeti()
    }
    private fun upisiNaPredmet(godina: Int, predmet: String, grupa: String) {
        val godinaPredmeta = godina + 1
        kvizModel.upisiNaPredmet(godinaPredmeta, predmet, grupa)
    }

    private fun obavijestiKorisnika(grupa: String, predmet: String) {
        val string  = "Uspješno ste upisani u grupu $grupa predmeta $predmet!"
        val nextFrag = FragmentPoruka.newInstance(string)

        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this@FragmentPredmeti)
            .replace(R.id.container, nextFrag, "findThisFragment")
            .addToBackStack(null)
            .commit()
    }
//    private fun loadSpinners(){
//        if(KvizViewModel.odabraniPredmet==-1){
//            adapterPredmet.clear()
//            adapterGrupa.clear()
//            return
//        }
//        kvizModel.dajNeupisanePredmete(populateWithPredmet = ::populateWithPredmet)
////        predmetChoiceList = kvizModel.dajNeupisanePredmete().filter { predmet -> predmet.godina.equals(
////            KvizViewModel.odabranaGodina + 1
////        )}.map { predmet -> predmet.toString() }
//
//        val predmet = predmetChoiceList[KvizViewModel.odabraniPredmet]
//        kvizModel.dajSveGrupeZaPredmet(predmet, populateWithGrupe = ::populateWithGrupe)
//        //groupChoiceList = kvizModel.dajSveGrupeZaPredmet(predmet).map { grupa -> grupa.toString() }
//
//        adapterPredmet.clear()
//        adapterPredmet.addAll(predmetChoiceList)
//
//        spinnerPredmet.setSelection(KvizViewModel.odabraniPredmet)
//        if(KvizViewModel.odabranaGrupa == -1) {
//            adapterGrupa.clear()
//            return
//        }
//        adapterGrupa.clear()
//        adapterGrupa.addAll(groupChoiceList)
//        spinnerGrupa.setSelection(KvizViewModel.odabranaGrupa)
//        Handler().postDelayed({
//            spinnerGrupa.setSelection(KvizViewModel.odabranaGrupa)
//        }, 100)
//
//    }

    override fun onPause() {
        KvizViewModel.odabranaGodina = spinnerGodina.selectedItemPosition
        KvizViewModel.odabraniPredmet = spinnerPredmet.selectedItemPosition
        KvizViewModel.odabranaGrupa = spinnerGrupa.selectedItemPosition
        super.onPause()
    }

    fun populateWithPredmet(predmeti: List<Predmet>){
        predmetChoiceList = predmeti.filter { predmet -> predmet.godina.equals(
            KvizViewModel.odabranaGodina + 1
        )}.map { predmet -> predmet.toString() }
        println("Populate w predmeti " + predmetChoiceList)
        adapterPredmet.clear()
        if(predmetChoiceList.isEmpty()) {
            button.isVisible = false
        }
        adapterPredmet.addAll(predmetChoiceList)
        adapterPredmet.notifyDataSetChanged()
        if(predmetChoiceList.isNotEmpty()) predmet = predmetChoiceList[0]

        if(predmetChoiceList.isEmpty())
            (spinnerGrupa.adapter as ArrayAdapter<*>).clear()
        spinnerPredmet.setSelection(0,false)
        spinnerGrupa.setSelection(0,false)
    }

    fun populateWithGrupe(grupe: List<Grupa>){
        groupChoiceList = grupe.map { grupa -> grupa.toString() }
        println("Populate w grupe " + groupChoiceList)
        adapterGrupa.clear()
        if(groupChoiceList.isEmpty()) {
            adapterGrupa.clear()
            button.isVisible = false
        }
        adapterGrupa.addAll(groupChoiceList)
        adapterGrupa.notifyDataSetChanged()
    }
}