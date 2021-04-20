package ba.etf.rma21.projekat.data.models

import android.os.Parcel
import android.os.Parcelable

class Pitanje(val naziv: String?, val tekst: String?, val opcije: List<String>?, val tacan: Int?) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(naziv)
        parcel.writeString(tekst)
        parcel.writeStringList(opcije)
        parcel.writeInt(tacan!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pitanje> {
        override fun createFromParcel(parcel: Parcel): Pitanje {
            return Pitanje(parcel)
        }

        override fun newArray(size: Int): Array<Pitanje?> {
            return arrayOfNulls(size)
        }
    }
}