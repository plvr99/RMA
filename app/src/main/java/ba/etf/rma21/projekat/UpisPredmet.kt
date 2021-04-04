package ba.etf.rma21.projekat

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import ba.etf.rma21.projekat.viewmodel.NajjaciViewModelNaSvijetu

class UpisPredmet : AppCompatActivity() {
    private var kvizViewModel : NajjaciViewModelNaSvijetu = NajjaciViewModelNaSvijetu()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_predmet)
        val spinnerGodina : Spinner = findViewById(R.id.spinner2)
        val spinnerPredmet : Spinner = findViewById(R.id.spinner3)
        val spinnerGrupa : Spinner = findViewById(R.id.spinner4)
        val kvizModel : NajjaciViewModelNaSvijetu? = intent.getParcelableExtra("model")
        ArrayAdapter.createFromResource(
                this,
                R.array.godina_choice_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerGodina.adapter = adapter
        }

        var predmetChoiceList : List<Predmet> = arrayListOf()
        var groupChoice : List<Grupa> = arrayListOf()
        val adapterPredmet: ArrayAdapter<Predmet> = ArrayAdapter(this, android.R.layout.simple_spinner_item, predmetChoiceList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val adapterGrupa: ArrayAdapter<Grupa> = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupChoice).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerPredmet.adapter = adapterPredmet

        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                predmetChoiceList = kvizViewModel.dajSvePredmeteZaGodinu(position + 1)
                adapterPredmet.clear()
                adapterPredmet.addAll(predmetChoiceList)
                adapterPredmet.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        spinnerGrupa.adapter = adapterGrupa
        spinnerPredmet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var predmet : String = spinnerPredmet.getItemAtPosition(position).toString()
                println(spinnerPredmet.getItemAtPosition(position).toString())
                groupChoice = kvizViewModel.dajSveGrupeZaPredmet(predmet)
                adapterGrupa.clear()
                adapterGrupa.addAll(groupChoice)
                adapterGrupa.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        val button : Button = findViewById(R.id.button)
        button.setOnClickListener(){
            upisiNaPredmet(spinnerGodina.selectedItemPosition, spinnerPredmet.selectedItem.toString(), spinnerGrupa.selectedItem.toString())
        }

    }

    private fun upisiNaPredmet(godina : Int, predmet: String, grupa: String) {
        //todo
    }
}