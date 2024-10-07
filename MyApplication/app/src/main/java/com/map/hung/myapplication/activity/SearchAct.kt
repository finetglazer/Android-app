package com.map.hung.myapplication.activity

//import UserPreferences
//import android.app.Activity
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.UserDao
import com.map.hung.myapplication.model.User
import java.text.SimpleDateFormat
import java.util.Locale


//import com.map.hung.myapplication.UserPreferences

class SearchAct : Activity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var userList: LinearLayout
    private lateinit var txt_back: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search) // Assuming the XML layout file is named search.xml

        searchEditText = findViewById(R.id.search)
        searchButton = findViewById(R.id.searchButton)
        userList = findViewById(R.id.userList)
        txt_back = findViewById(R.id.back)

        searchButton.setOnClickListener {
            onClick()
        }

        txt_back.setOnClickListener {
            val intent = Intent(this, UserHomeAct::class.java)
            startActivity(intent)
        }
    }


    private fun onClick() {

        val userDao = UserDao(this)

        val query = searchEditText.text.toString()
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            return
        }

        userList.removeAllViewsInLayout()


        // Filter users by query using full name and email
        var filteredUsers = userDao.searchUsers(query)

        if (filteredUsers.isNotEmpty()) {
           display(filteredUsers, userList);
        } else {
            userList.visibility = View.GONE
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show()
        }
    }

    fun display(users: List<User>, container: LinearLayout) {
        container.removeAllViews()
        container.visibility = View.VISIBLE

        for (user in users) {
            val userView = layoutInflater.inflate(R.layout.user_info, container, false)

            userView.findViewById<TextView>(R.id.userNameTextView).text = "Username: ${user.username}"
            userView.findViewById<TextView>(R.id.emailTextView).text = "Email: ${user.email}"
            userView.findViewById<TextView>(R.id.fullNameTextView).text = "Full Name: ${user.fullname}"
            userView.findViewById<TextView>(R.id.telTextView).text = "Phone: ${user.phone}"
            userView.findViewById<TextView>(R.id.dobTextView).text =
                "Date of Birth: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(user.dob)}"
            userView.findViewById<TextView>(R.id.genderTextView).text = "Gender: ${user.gender}"

            container.addView(userView)


            val menuOptions = userView.findViewById<ImageView>(R.id.menuOptions)
            menuOptions.setOnClickListener{
                showPopupMenu(menuOptions, user, container, userView)
                // update number of users found

            }
        }
    }



    fun showPopupMenu(view: View, user: User, container: LinearLayout, userView: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit_option -> {
                    val intent = Intent(this, EditAct::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    true
                }
                R.id.delete_option -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes") { _, _ ->
                            val userDao = UserDao(this)
                            userDao.deleteUser(user)
                            container.removeView(userView)
                            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()

                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                    alertDialog.show()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
