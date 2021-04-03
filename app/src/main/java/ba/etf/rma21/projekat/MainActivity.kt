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
      //  kvizoviRecyclerView.layoutManager = GridLayoutManager(this,2)
        kvizoviRecyclerView.layoutManager = GridLayoutManager(this,2,RecyclerView.VERTICAL,false)

        kvizAdapter = KvizAdapter(kvizViewModel.getAll())
        kvizoviRecyclerView.adapter = kvizAdapter

        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.spinner_choice_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> {kvizAdapter = KvizAdapter(kvizViewModel.getMyKvizes())
                         kvizoviRecyclerView.adapter = kvizAdapter}
                    1 -> {kvizAdapter = KvizAdapter(kvizViewModel.getAll())
                        kvizoviRecyclerView.adapter = kvizAdapter}
                    2 -> {kvizAdapter = KvizAdapter(kvizViewModel.getDone())
                        kvizoviRecyclerView.adapter = kvizAdapter}
                    3 -> {kvizAdapter = KvizAdapter(kvizViewModel.getFuture())
                        kvizoviRecyclerView.adapter = kvizAdapter}
                    4 -> {kvizAdapter = KvizAdapter(kvizViewModel.getNotTaken())
                        kvizoviRecyclerView.adapter = kvizAdapter}
            }
        }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val floatingActionButton : View = findViewById(R.id.floatingActionButton2)
        floatingActionButton.setOnClickListener{dodajPredmet()}
    }

    private fun dodajPredmet() {
        val intent = Intent(this, UpisPredmet::class.java)
        startActivity(intent)
    }

}

