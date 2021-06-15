package ba.etf.rma21.projekat.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Pitanje(@PrimaryKey val id : Int,
              @ColumnInfo(name = "naziv") val naziv: String?,
              @ColumnInfo (name = "tekstPitanja") val tekstPitanja: String?,
              @ColumnInfo (name = "opcije") val opcije: String?,
              @ColumnInfo (name = "tacan") val tacan: Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(naziv)
        parcel.writeString(tekstPitanja)
        parcel.writeString(opcije)
        parcel.writeInt(tacan)
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