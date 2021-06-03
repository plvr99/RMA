package ba.etf.rma21.projekat.data.models

import android.os.Parcel
import android.os.Parcelable


data class Odgovor(val id : Int, var odgovoreno : Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
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
