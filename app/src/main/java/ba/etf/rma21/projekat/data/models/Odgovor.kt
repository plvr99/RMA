package ba.etf.rma21.projekat.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Odgovor(@PrimaryKey(autoGenerate = true) val id : Int?,
                   @ColumnInfo(name = "pitanjeId") val pitanjeId: Int,
                   @ColumnInfo(name = "kvizTakenId") val kvizTakendId: Int,
                   @ColumnInfo(name = "odgovoreno") var odgovoreno : Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id!!)
        parcel.writeInt(pitanjeId)
        parcel.writeInt(kvizTakendId)
        parcel.writeInt(odgovoreno)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Odgovor> {
        override fun createFromParcel(parcel: Parcel): Odgovor {
            return Odgovor(parcel)
        }

        override fun newArray(size: Int): Array<Odgovor?> {
            return arrayOfNulls(size)
        }
    }

}
