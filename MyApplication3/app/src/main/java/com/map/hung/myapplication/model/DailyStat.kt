package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class DailyStat(
    var id: Int? = null,           // Add the ID field here
    var day: Date? = null,
    var income: Int? = null,
    var outcome: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        day = Date(parcel.readLong()),
        income = parcel.readInt(),
        outcome = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(income)
        parcel.writeValue(outcome)
        parcel.writeLong(day?.time ?: -1L)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DailyStat> {
        override fun createFromParcel(parcel: Parcel): DailyStat {
            return DailyStat(parcel)
        }

        override fun newArray(size: Int): Array<DailyStat?> {
            return arrayOfNulls(size)
        }
    }
}
