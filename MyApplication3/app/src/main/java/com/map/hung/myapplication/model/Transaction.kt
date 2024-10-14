package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class Transaction(
    var id: Int? = null,
    var name: String? = null,
    var catInOut: CatInOut? = null,
    var amount: Double? = null,
    var date: Date? = null,
    var note: String? = null
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString(),
        catInOut = parcel.readParcelable(CatInOut::class.java.classLoader),
        amount = parcel.readDouble(),
        date = Date(parcel.readLong()),
        note = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeParcelable(catInOut, flags)
        parcel.writeValue(amount)
        parcel.writeLong(date?.time ?: -1L)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}
