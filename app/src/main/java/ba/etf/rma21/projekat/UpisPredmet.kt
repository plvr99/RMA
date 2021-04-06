package ba.etf.rma21.projekat

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.viewmodel.NajjaciViewModelNaSvijetu

class UpisPredmet : AppCompatActivity() {
    var kvizModel : NajjaciViewModelNaSvijetu = NajjaciViewModelNaSvijetu()
    lateinit var spinnerGodina : Spinner
    lateinit var button : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_predmet)
        val spinnerPredmet : Spinner = findViewById(R.id.spinner3)
        val spinnerGrupa : Spinner = findViewById(R.id.spinner4)
        spinnerGodina = findViewById(R.id.spinner2)
        button = findViewById(R.id.button)
        var predmetChoiceList : List<Predmet> = arrayListOf()
        var groupChoiceList : List<Grupa> = arrayListOf()

        ArrayAdapter.createFromResource(this, R.array.godina_choice_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGodina.adapter = adapter
        }

        val adapterPredmet: ArrayAdapter<Predmet> = ArrayAdapter(this, android.R.layout.simple_spinner_item, predmetChoiceList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val adapterGrupa: ArrayAdapter<Grupa> = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupChoiceList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerPredmet.adapter = adapterPredmet
        spinnerGrupa.adapter = adapterGrupa
        println("GODINAAAAAAAAAAAAAAAAAAAAAAA " + NajjaciViewModelNaSvijetu.Companion.odabranaGodina)


        //todo updateat druga 2 spinnera ZBOG BUTTONA

        spinnerGodina.setSelection(NajjaciViewModelNaSvijetu.Companion.odabranaGodina-1)
        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                predmetChoiceList = kvizModel.dajNeupisanePredmete().filter { predmet -> predmet.godina.equals(position + 1)}
                adapterPredmet.clear()
                if(predmetChoiceList.size==0) adapterPredmet.clear()
                else adapterPredmet.addAll(predmetChoiceList)
                adapterPredmet.notifyDataSetChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spinnerPredmet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val predmet : String = spinnerPredmet.getItemAtPosition(position).toString()
                groupChoiceList = kvizModel.dajSveGrupeZaPredmet(predmet)
                adapterGrupa.clear()
                adapterGrupa.addAll(groupChoiceList)
                adapterGrupa.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                adapterGrupa.clear()
                adapterGrupa.notifyDataSetChanged()
                button.isVisible = false
            }
        }

        spinnerGodina.performItemClick(spinnerGodina.getChildAt(NajjaciViewModelNaSvijetu.odabranaGodina),
                NajjaciViewModelNaSvijetu.odabranaGodina, spinnerGodina.adapter.getItemId(NajjaciViewModelNaSvijetu.odabranaGodina))
        button.setOnClickListener {
            upisiNaPredmet(spinnerGodina.selectedItemPosition, spinnerPredmet.selectedItem.toString(), spinnerGrupa.selectedItem.toString())
            NajjaciViewModelNaSvijetu.Companion.odabranaGodina = spinnerGodina.selectedItem.toString().toInt()
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
        println(""+godinaPredmeta + predmet+ grupa)
        kvizModel.upisiNaPredmet(godinaPredmeta, predmet, grupa)
        Toast.makeText(this, "Upisan", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //println(spinnerGodina.selectedItem.toString().toInt())
        NajjaciViewModelNaSvijetu.Companion.odabranaGodina = spinnerGodina.selectedItem.toString().toInt()
        println("POSTAVIO " + NajjaciViewModelNaSvijetu.Companion.odabranaGodina)
    }
}