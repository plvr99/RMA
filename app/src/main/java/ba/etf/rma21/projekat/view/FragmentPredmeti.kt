package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
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
        var predmetChoiceList : List<String> = arrayListOf()
        var groupChoiceList : List<String> = arrayListOf()

        ArrayAdapter.createFromResource(
            context!!,
            R.array.godina_choice_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGodina.adapter = adapter
        }

        adapterPredmet = ArrayAdapter(
            context!!, android.R.layout.simple_spinner_item, predmetChoiceList
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        adapterGrupa = ArrayAdapter(
            context!!, android.R.layout.simple_spinner_item, groupChoiceList
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerPredmet.adapter = adapterPredmet
        spinnerGrupa.adapter = adapterGrupa

        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
               // println("spinner godina pozvan")
                predmetChoiceList = kvizModel.dajNeupisanePredmete().filter { predmet -> predmet.godina.equals(
                    position + 1
                )}.map { predmet -> predmet.toString() }
                adapterPredmet.clear()
                if(predmetChoiceList.isEmpty()) {
                    button.isVisible = false
                }
                adapterPredmet.addAll(predmetChoiceList)
                adapterPredmet.notifyDataSetChanged()
                if(predmetChoiceList.isEmpty())
                    (spinnerGrupa.adapter as ArrayAdapter<*>).clear()
                if(predmetChoiceList.isNotEmpty()) spinnerGrupa.onItemSelectedListener!!.onItemSelected(
                    parent,
                    view,
                    0,
                    id
                )
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerPredmet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                //println("spinner predmet pozvan")
                val predmet : String = spinnerPredmet.getItemAtPosition(position).toString()
                groupChoiceList = kvizModel.dajSveGrupeZaPredmet(predmet).map { grupa -> grupa.toString() }
                adapterGrupa.clear()
                if(groupChoiceList.isEmpty()) {
                    adapterGrupa.clear()
                    button.isVisible = false
                }
                adapterGrupa.addAll(groupChoiceList)
                adapterGrupa.notifyDataSetChanged()
                spinnerGrupa.setSelection(0, false)
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
        loadSpinners()
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

        activity!!.supportFragmentManager.beginTransaction()
            .remove(this@FragmentPredmeti)
            .replace(R.id.container, nextFrag, "findThisFragment")
            .addToBackStack(null)
            .commit()
    }
    private fun loadSpinners(){
        if(KvizViewModel.odabraniPredmet==-1){
            adapterPredmet.clear()
            adapterGrupa.clear()
            return
        }
        val predmeti = kvizModel.dajNeupisanePredmete().filter { predmet -> predmet.godina.equals(
            KvizViewModel.odabranaGodina + 1
        )}.map { predmet -> predmet.toString() }

        val predmet = predmeti[KvizViewModel.odabraniPredmet]
        val grupe = kvizModel.dajSveGrupeZaPredmet(predmet).map { grupa -> grupa.toString() }

        adapterPredmet.clear()
        adapterPredmet.addAll(predmeti)

        spinnerPredmet.setSelection(KvizViewModel.odabraniPredmet)
        if(KvizViewModel.odabranaGrupa == -1) {
            adapterGrupa.clear()
            return
        }
        adapterGrupa.clear()
        adapterGrupa.addAll(grupe)
        spinnerGrupa.setSelection(KvizViewModel.odabranaGrupa)
        Handler().postDelayed({
            spinnerGrupa.setSelection(KvizViewModel.odabranaGrupa)
        }, 100)

    }

    override fun onPause() {
        KvizViewModel.odabranaGodina = spinnerGodina.selectedItemPosition
        KvizViewModel.odabraniPredmet = spinnerPredmet.selectedItemPosition
        KvizViewModel.odabranaGrupa = spinnerGrupa.selectedItemPosition
        super.onPause()
    }
}