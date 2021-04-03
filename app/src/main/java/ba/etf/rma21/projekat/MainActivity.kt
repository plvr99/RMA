package ba.etf.rma21.projekat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.view.KvizAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity(){
    private lateinit var kvizoviRecyclerView: RecyclerView
    private lateinit var kvizAdapter: KvizAdapter
    private lateinit var spinner: Spinner
    private lateinit var repo: KvizRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kvizoviRecyclerView = findViewById(R.id.kvizoviRecyclerView)
        repo = KvizRepository()
      //  kvizoviRecyclerView.layoutManager = GridLayoutManager(this,2)
        kvizoviRecyclerView.layoutManager = GridLayoutManager(this,2,RecyclerView.VERTICAL,false)
        //kvizoviRecyclerView.layoutManager = LinearLayoutManager(this,
       //     LinearLayoutManager.HORIZONTAL, false)
        var kviz1  : Kviz = Kviz("kviskoo","IM2", Date(1,1,2020),
            Date(1,2,2020), Date(5,6,2015),5,"gruppa",5f)
        var kviz2  : Kviz = Kviz("kvisko","ASP", Date(1,1,2020),
            Date(1,2,2020), Date(5,6,2015),3,"grupa",2f)
        kvizAdapter = KvizAdapter(KvizRepository.Companion.getAll())
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
        var floatingActionButton : View = findViewById(R.id.floatingActionButton2)
        floatingActionButton.setOnClickListener{dodajPredmet()}
    }

    private fun dodajPredmet() {
        val intent = Intent(this, UpisPredmet::class.java)
        startActivity(intent)
    }

}

