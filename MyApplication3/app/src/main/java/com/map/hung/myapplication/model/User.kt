package com.map.hung.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(val id: Int, var name: String, var email: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}