package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable

class User(
    var username: String? = null,
    var password: String? = null,
    var fullname: String? = null,
    var email: String? = null
) : Parcelable {

    // Constructor used by the CREATOR to create a User instance from a Parcel
    private constructor(parcel: Parcel) : this(
        username = parcel.readString(),
        password = parcel.readString(),
        fullname = parcel.readString(),
        email = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(fullname)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
