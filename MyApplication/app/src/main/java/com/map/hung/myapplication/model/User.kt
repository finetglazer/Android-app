package com.map.hung.myapplication.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

class User(
    // today add 2 new attribution
    var username: String? = null,
    var password: String? = null,
    var fullname: String? = null,
    var email: String? = null,
    var dob: Date? = null,
    var gender: String? = null
    ) : Parcelable {

    private constructor(parcel: Parcel) : this(
        username = parcel.readString(),
        password = parcel.readString(),
        fullname = parcel.readString(),
        email = parcel.readString(),
        dob = Date(parcel.readLong()),   // Reading Date as Long (milliseconds)
        gender = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(fullname)
        parcel.writeString(email)
        parcel.writeLong(dob?.time ?: -1L)  // Write Date as Long (milliseconds)
        parcel.writeString(gender)
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
