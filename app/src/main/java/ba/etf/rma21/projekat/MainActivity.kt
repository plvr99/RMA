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
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.view.KvizAdapter
import ba.etf.rma21.projekat.viewmodel.NajjaciViewModelNaSvijetu
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.FieldPosition
import java.util.*


class MainActivity : AppCompatActivity(){
    private lateinit var kvizoviRecyclerView: RecyclerView
    private lateinit var kvizAdapter: KvizAdapter
    private lateinit var spinner: Spinner
    private var kvizViewModel : NajjaciViewModelNaSvijetu = NajjaciViewModelNaSvijetu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kvizoviRecyclerView = findViewById(R.id.kvizoviRecyclerView)
        kvizoviRecyclerView.layoutManager = GridLayoutManager(this,2,RecyclerView.VERTICAL,false)

        kvizAdapter = KvizAdapter(kvizViewModel.getAll())
        kvizoviRecyclerView.adapter = kvizAdapter

        spinner = findViewById(R.id.spinner)
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
        val floatingActionButton : View = findViewById(R.id.floatingActionButton2)
        floatingActionButton.setOnClickListener{dodajPredmet()}
    }

    override fun onRestart() {
        println("RESTART!!!!!!")
            prikaziKvizoveSaOpcijom(spinner.selectedItemPosition)
        println("ODABRANA GODINA JE " + NajjaciViewModelNaSvijetu.Companion.odabranaGodina)
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
            0 -> {kvizAdapter = KvizAdapter(kvizViewModel.getMyKvizes().sortedBy { kviz -> kviz.datumPocetka })
                kvizoviRecyclerView.adapter = kvizAdapter}
            1 -> {kvizAdapter = KvizAdapter(kvizViewModel.getAll().sortedBy { kviz -> kviz.datumPocetka })
                kvizoviRecyclerView.adapter = kvizAdapter}
            2 -> {kvizAdapter = KvizAdapter(kvizViewModel.getDone().sortedBy { kviz -> kviz.datumPocetka })
                kvizoviRecyclerView.adapter = kvizAdapter}
            3 -> {kvizAdapter = KvizAdapter(kvizViewModel.getFuture().sortedBy { kviz -> kviz.datumPocetka })
                kvizoviRecyclerView.adapter = kvizAdapter}
            4 -> {kvizAdapter = KvizAdapter(kvizViewModel.getNotTaken().sortedBy { kviz -> kviz.datumPocetka })
                kvizoviRecyclerView.adapter = kvizAdapter}
        }
    }

}

