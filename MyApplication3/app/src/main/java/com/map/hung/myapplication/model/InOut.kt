package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable

class InOut(
    var id: Int? = null,
    var name: String? = null,
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InOut> {
        override fun createFromParcel(parcel: Parcel): InOut {
            return InOut(parcel)
        }

        override fun newArray(size: Int): Array<InOut?> {
            return arrayOfNulls(size)
        }
    }
}
