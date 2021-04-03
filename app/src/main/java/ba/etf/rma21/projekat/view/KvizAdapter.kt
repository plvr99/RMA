package ba.etf.rma21.projekat.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import java.text.DateFormat
import java.time.LocalDateTime
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
        // TODO provjeriti Datume i odreditii stanje kviza
        holder.kvizTrajanje.text = kvizovi[position].trajanje.toString() +" min"

        odrediTipKviza(kvizovi[position]).also {
            postaviSlikuIDatum(it, holder, kvizovi[position])
        }

//        holder.kvizDatum.text = kvizovi[position].datumPocetka.toString()

    }

    override fun getItemCount(): Int {
       return  kvizovi.size
    }
//    val naziv: String, val nazivPredmeta: String, val datumPocetka: Date, val datumKraj: Date,
//    val datumRada: Date, val trajanje: Int, val nazivGrupe: String, val osvojeniBodovi: Float
    inner class KvizViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image : ImageView = itemView.findViewById(R.id.imageView7)
        var kvizNaziv : TextView = itemView.findViewById(R.id.textViewNaziv)
        var kvizNazivPredmeta : TextView = itemView.findViewById(R.id.textViewNazivPredmeta)
        var kvizDatum : TextView = itemView.findViewById(R.id.textViewDatum)
        var kvizTrajanje : TextView = itemView.findViewById(R.id.textViewTrajanje)
        var kvizBodovi : TextView = itemView.findViewById(R.id.textViewBodovi)
    }

    private fun odrediTipKviza(kviz: Kviz) : Int{
        val bodovi = kviz.osvojeniBodovi
        //Calendar.getInstance().time KAO CURRENT TIME
        //ima bodove i datum kraj je prije now - plava, kviz koji je urađen,
        if(bodovi != null && kviz.datumKraj.before(Calendar.getInstance().time)) return 1
        if(bodovi == null){
            //nema bodove i now je prije pocetka - zuta kviz koji tek treba biti aktivan,
            if(Calendar.getInstance().time.before(kviz.datumPocetka)) return 3
            // nema bodove i now je poslije datum kraj - crvena  kviz koji je prošao, a nije urađen.
            if(Calendar.getInstance().time.after(kviz.datumKraj)) return 4
            //nema bodove i datum rada + minute je prije datum kraja - zelena kviz koji je aktivan, ali nije urađen,
            return 2;
        }
        return 0
    }
    private fun postaviSlikuIDatum(tip : Int , holder: KvizViewHolder, kviz : Kviz){
        val context: Context = holder.image.getContext()
        var id : Int = 0
        var dateFormat  = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(kviz.datumPocetka)
        if(tip==1){
            id=context.getResources()
                    .getIdentifier("plava", "drawable", context.getPackageName())
            dateFormat  = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(kviz.datumRada)
        }
        else if (tip==2){
            id=context.getResources()
                    .getIdentifier("zelena", "drawable", context.getPackageName())
            dateFormat  = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(kviz.datumKraj)
        }
        else if (tip == 3){
            id=context.getResources()
                    .getIdentifier("zuta", "drawable", context.getPackageName())
            dateFormat  = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(kviz.datumPocetka)
        }
        else if (tip == 4){
            id=context.getResources()
                    .getIdentifier("crvena", "drawable", context.getPackageName())
            dateFormat  = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(kviz.datumKraj)
        }
        holder.image.setImageResource(id)
        holder.kvizDatum.text = dateFormat
    }

}