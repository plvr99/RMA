package ba.etf.rma21.projekat.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class KvizAdapter(private var kvizovi : List<Kviz>) : RecyclerView.Adapter<KvizAdapter.KvizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KvizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kviz_item2,parent,false)
        return KvizViewHolder(view)
    }

    override fun onBindViewHolder(holder: KvizViewHolder , position: Int) {
        holder.kvizNazivPredmeta.text = kvizovi[position].nazivPredmeta
        holder.kvizNaziv.text = kvizovi[position].naziv
        holder.kvizBodovi.text = kvizovi[position].osvojeniBodovi.toString()
        holder.kvizTrajanje.text = kvizovi[position].trajanje.toString() + " min"
        odrediTipKviza(kvizovi[position]).also {
            postaviSlikuIDatum(it, holder, kvizovi[position])
        }
    }
    fun updateList(newList : List<Kviz>){
        kvizovi = newList
        notifyDataSetChanged();
    }
    override fun getItemCount(): Int {
       return  kvizovi.size
    }
    inner class KvizViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.findViewById(R.id.imageView7)
        var kvizNaziv : TextView = itemView.findViewById(R.id.textViewNaziv)
        var kvizNazivPredmeta : TextView = itemView.findViewById(R.id.textViewNazivPredmeta)
        var kvizDatum : TextView = itemView.findViewById(R.id.textViewDatum)
        var kvizTrajanje : TextView = itemView.findViewById(R.id.textViewTrajanje)
        var kvizBodovi : TextView = itemView.findViewById(R.id.textViewBodovi)
    }

    private fun odrediTipKviza(kviz: Kviz) : Int{
        //Calendar.getInstance().time KAO CURRENT TIME
        //ima bodove i datum kraj je prije now - plava, kviz koji je urađen,
        if(kviz.osvojeniBodovi != null) return 1
        else{
            //nema bodove i now je prije pocetka - zuta kviz koji tek treba biti aktivan,
            if(Calendar.getInstance().time.before(kviz.datumPocetka)) return 3
            // nema bodove i now je poslije datum kraj - crvena  kviz koji je prošao, a nije urađen.
            if(Calendar.getInstance().time.after(kviz.datumKraj)) return 4
            //nema bodove i datum rada + minute je prije datum kraja - zelena kviz koji je aktivan, ali nije urađen,
            if(kviz.datumPocetka.before(Calendar.getInstance().time) && kviz.datumKraj.after(Calendar.getInstance().time)) return 2
        }
        return 0
    }
    private fun postaviSlikuIDatum(tip : Int , holder: KvizViewHolder, kviz : Kviz){
        val context: Context = holder.image.context
        val sd = SimpleDateFormat("dd.MM.yyyy")
        var dateFormat  = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(kviz.datumPocetka)
        val boje : List<String> = arrayListOf("","plava", "zelena","zuta","crvena")
        when (tip) {
            1 -> dateFormat = sd.format(kviz.datumRada)
            2 -> dateFormat = sd.format(kviz.datumKraj)
            3 -> dateFormat = sd.format(kviz.datumPocetka)
            4 -> dateFormat = sd.format(kviz.datumKraj)
        }
        val id=context.resources
            .getIdentifier(boje.get(tip), "drawable", context.packageName)
        holder.image.setImageResource(id)
        if(tip==1) holder.kvizBodovi.text = kviz.osvojeniBodovi.toString() else holder.kvizBodovi.text = " "
        holder.kvizDatum.text = dateFormat
    }

}