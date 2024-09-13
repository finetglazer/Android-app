package com.map.hung.myapplication.activity

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.map.hung.myapplication.model.User

class UserPreferences(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Function to get all users
    fun getUsers(): Array<User>? {
        val json = preferences.getString("users", null)
        return if (json != null) {
            val type = object : TypeToken<Array<User>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    // Function to save users
    fun addUser(users: Array<User>) {
        val editor = preferences.edit()
        val json = gson.toJson(users)
        editor.putString("users", json)
        editor.apply()
    }



    // Function to clear all users
    fun clearUsers() {
        val editor = preferences.edit()
        editor.remove("users")
        editor.apply()
    }

    fun deleteUser(user: User) {
        val users = getUsers()?.toMutableList() ?: mutableListOf()
        users.remove(user)
        addUser(users.toTypedArray())
    }

}
