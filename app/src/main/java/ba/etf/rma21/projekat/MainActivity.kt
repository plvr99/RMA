package ba.etf.rma21.projekat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.view.KvizAdapter
import ba.etf.rma21.projekat.viewmodel.KvizViewModel


class MainActivity : AppCompatActivity(){
    private lateinit var kvizoviRecyclerView: RecyclerView
    private lateinit var kvizAdapter: KvizAdapter
    private lateinit var spinner: Spinner
    private var kvizViewModel : KvizViewModel = KvizViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kvizoviRecyclerView = findViewById(R.id.listaKvizova)
        kvizoviRecyclerView.layoutManager = GridLayoutManager(this,2,RecyclerView.VERTICAL,false)

        kvizAdapter = KvizAdapter(kvizViewModel.getAll())
        kvizoviRecyclerView.adapter = kvizAdapter

        spinner = findViewById(R.id.filterKvizova)
        ArrayAdapter.createFromResource(this, R.array.spinner_choice_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                prikaziKvizoveSaOpcijom(position)
        }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val floatingActionButton : View = findViewById(R.id.upisDugme)
        floatingActionButton.setOnClickListener{dodajPredmet()}
    }

    override fun onRestart() {
        prikaziKvizoveSaOpcijom(spinner.selectedItemPosition)
        super.onRestart()
    }

    private fun dodajPredmet() {
        val intent = Intent(this, UpisPredmet::class.java).apply {
           // putExtra("model", kvizViewModel)
        }
        startActivity(intent)
    }

    fun prikaziKvizoveSaOpcijom(position: Int){
        when(position){
            0 ->{kvizAdapter.updateList(kvizViewModel.getMyKvizes().sortedBy { kviz -> kviz.datumPocetka })}
            1 ->{kvizAdapter.updateList(kvizViewModel.getAll().sortedBy { kviz -> kviz.datumPocetka })}
            2 ->{kvizAdapter.updateList(kvizViewModel.getMyDone().sortedBy { kviz -> kviz.datumPocetka })}
            3 ->{kvizAdapter.updateList(kvizViewModel.getMyFuture().sortedBy { kviz -> kviz.datumPocetka })}
            4 ->{kvizAdapter.updateList(kvizViewModel.getMyNotTaken().sortedBy { kviz -> kviz.datumPocetka })}
        }
    }

}

