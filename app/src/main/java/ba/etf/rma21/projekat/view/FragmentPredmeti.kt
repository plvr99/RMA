package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.viewmodel.KvizViewModel


class FragmentPredmeti : Fragment() {
    var kvizModel : KvizViewModel = KvizViewModel()
    lateinit var spinnerGodina : Spinner
    lateinit var button : Button

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_predmeti, container, false)
        val spinnerPredmet : Spinner = view.findViewById(R.id.odabirPredmet)
        val spinnerGrupa : Spinner = view.findViewById(R.id.odabirGrupa)
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

        val adapterPredmet: ArrayAdapter<String> = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            predmetChoiceList
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val adapterGrupa: ArrayAdapter<String> = ArrayAdapter(
            context!!,
            android.R.layout.simple_spinner_item,
            groupChoiceList
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerPredmet.adapter = adapterPredmet
        spinnerGrupa.adapter = adapterGrupa

        spinnerGodina.setSelection(KvizViewModel.odabranaGodina - 1)
        spinnerPredmet.setSelection(KvizViewModel.odabraniPredmet)
        spinnerGrupa.setSelection(KvizViewModel.odabraniPredmet)
        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                predmetChoiceList = kvizModel.dajNeupisanePredmete().filter { predmet -> predmet.godina.equals(
                    position + 1
                )}.map { predmet -> predmet.toString() }
                adapterPredmet.clear()
                if(predmetChoiceList.isEmpty()) {
                    adapterPredmet.clear()
                    button.isVisible = false
                }
                else adapterPredmet.addAll(predmetChoiceList)
                adapterPredmet.notifyDataSetChanged()
                if(!spinnerPredmet.adapter.isEmpty)
                    spinnerPredmet.onItemSelectedListener!!.onItemSelected(parent, view, 0, id)
                KvizViewModel.odabranaGodina = spinnerGodina.selectedItem.toString().toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerPredmet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val predmet : String = spinnerPredmet.getItemAtPosition(position).toString()
                groupChoiceList = kvizModel.dajSveGrupeZaPredmet(predmet).map { grupa -> grupa.toString() }
                adapterGrupa.clear()
                if(groupChoiceList.isEmpty()) {
                    adapterGrupa.clear()
                    button.isVisible = false
                }
                else adapterGrupa.addAll(groupChoiceList)
                adapterGrupa.notifyDataSetChanged()
                if(spinnerPredmet.selectedItem == null) spinnerPredmet.setSelection(0)
                //RAKESH
                spinnerGrupa.onItemSelectedListener!!.onItemSelected(parent, view, 0, id)
                KvizViewModel.odabraniPredmet = spinnerPredmet.selectedItemPosition
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                button.isVisible = false
                adapterGrupa.clear()
                adapterGrupa.notifyDataSetChanged()
            }
        }

        spinnerGodina.performItemClick(
            spinnerGodina.getChildAt(KvizViewModel.odabranaGodina),
            KvizViewModel.odabranaGodina,
            spinnerGodina.adapter.getItemId(KvizViewModel.odabranaGodina)
        )
        button.setOnClickListener {
            upisiNaPredmet(
                spinnerGodina.selectedItemPosition,
                spinnerPredmet.selectedItem.toString(),
                spinnerGrupa.selectedItem.toString()
            )
            KvizViewModel.odabranaGodina = spinnerGodina.selectedItem.toString().toInt()
            obavijestiKorisnika(spinnerGrupa.selectedItem.toString(), spinnerPredmet.selectedItem.toString())
        }

        spinnerGrupa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                button.isVisible=true
                KvizViewModel.odabranaGrupa = spinnerGrupa.selectedItemPosition
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                button.isVisible=false
            }
        }
        return view
    }

    companion object {
        fun newInstance() : FragmentPredmeti = FragmentPredmeti()
    }
    private fun upisiNaPredmet(godina: Int, predmet: String, grupa: String) {
        val godinaPredmeta = godina + 1
        kvizModel.upisiNaPredmet(godinaPredmeta, predmet, grupa)
       // Toast.makeText(context!!, "Upisan", Toast.LENGTH_SHORT).show()
        KvizViewModel.odabranaGodina = 1
        KvizViewModel.odabraniPredmet = 0
        KvizViewModel.odabranaGrupa = 0

    }

    fun obavijestiKorisnika(grupa: String, predmet: String) {
        val nextFrag = FragmentPoruka.newInstance(grupa, predmet)

        activity!!.supportFragmentManager.beginTransaction()
            .remove(this@FragmentPredmeti)
            .replace((view!!.parent as ViewGroup).id, nextFrag, "findThisFragment")
            //.addToBackStack(null)
            .commit()

    }

}