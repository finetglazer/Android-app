package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable

class Category (
    var id: Int? = null,
    var name: String ? = null,
    var parent: Category ? = null,
    var icon: String ? = null,
    var note: String ? = null,
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        name = parcel.readString(),
        parent = parcel.readParcelable(Category::class.java.classLoader),
        icon = parcel.readString(),
        note = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeParcelable(parent, flags)
        parcel.writeString(icon)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}