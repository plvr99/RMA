package ba.etf.rma21.projekat

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ba.etf.rma21.projekat.viewmodel.KvizViewModel

class UpisPredmet : AppCompatActivity() {
    var kvizModel : KvizViewModel = KvizViewModel()
    lateinit var spinnerGodina : Spinner
    lateinit var button : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_predmet)
        val spinnerPredmet : Spinner = findViewById(R.id.odabirPredmet)
        val spinnerGrupa : Spinner = findViewById(R.id.odabirGrupa)
        spinnerGodina = findViewById(R.id.odabirGodina)
        button = findViewById(R.id.dodajPredmetDugme)
        var predmetChoiceList : List<String> = arrayListOf()
        var groupChoiceList : List<String> = arrayListOf()

        ArrayAdapter.createFromResource(this, R.array.godina_choice_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGodina.adapter = adapter
        }

        val adapterPredmet: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, predmetChoiceList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val adapterGrupa: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupChoiceList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerPredmet.adapter = adapterPredmet
        spinnerGrupa.adapter = adapterGrupa

        spinnerGodina.setSelection(KvizViewModel.odabranaGodina-1)
        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                predmetChoiceList = kvizModel.dajNeupisanePredmete().filter { predmet -> predmet.godina.equals(position + 1)}.map { predmet -> predmet.toString() }
                adapterPredmet.clear()
                if(predmetChoiceList.isEmpty()) {
                    adapterPredmet.clear()
                    button.isVisible = false
                }
                else adapterPredmet.addAll(predmetChoiceList)
                adapterPredmet.notifyDataSetChanged()
                if(!spinnerPredmet.adapter.isEmpty)
                    spinnerPredmet.onItemSelectedListener!!.onItemSelected(parent,view,0,id)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerPredmet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
                spinnerGrupa.onItemSelectedListener!!.onItemSelected(parent,view,0,id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                button.isVisible = false
                adapterGrupa.clear()
                adapterGrupa.notifyDataSetChanged()
            }
        }

        spinnerGodina.performItemClick(spinnerGodina.getChildAt(KvizViewModel.odabranaGodina),
                KvizViewModel.odabranaGodina, spinnerGodina.adapter.getItemId(KvizViewModel.odabranaGodina))
        button.setOnClickListener {
            upisiNaPredmet(spinnerGodina.selectedItemPosition, spinnerPredmet.selectedItem.toString(), spinnerGrupa.selectedItem.toString())
            KvizViewModel.odabranaGodina = spinnerGodina.selectedItem.toString().toInt()
            finish()
        }

        spinnerGrupa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                button.isVisible=true
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                button.isVisible=false
            }
        }
    }

    private fun upisiNaPredmet(godina : Int, predmet: String, grupa: String) {
        val godinaPredmeta = godina + 1
        kvizModel.upisiNaPredmet(godinaPredmeta, predmet, grupa)
        Toast.makeText(this, "Upisan", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        KvizViewModel.odabranaGodina = spinnerGodina.selectedItem.toString().toInt()
    }
}