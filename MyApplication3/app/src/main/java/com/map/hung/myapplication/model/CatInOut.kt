package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable

class CatInOut(
    var id: Int? = null,
    var category: Category? = null,
    var inOut: InOut? = null,
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        category = parcel.readParcelable(Category::class.java.classLoader),
        inOut = parcel.readParcelable(InOut::class.java.classLoader)
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(category, flags)
        parcel.writeParcelable(inOut, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatInOut> {
        override fun createFromParcel(parcel: Parcel): CatInOut {
            return CatInOut(parcel)
        }

        override fun newArray(size: Int): Array<CatInOut?> {
            return arrayOfNulls(size)
        }
    }
}
