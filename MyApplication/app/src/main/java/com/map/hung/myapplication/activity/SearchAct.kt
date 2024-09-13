package com.map.hung.myapplication.activity

//import UserPreferences
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User
//import com.map.hung.myapplication.UserPreferences

class SearchAct : Activity() {
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var userList: LinearLayout
    private lateinit var txt_back: TextView
    private lateinit var userPreferences: UserPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search) // Assuming the XML layout file is named search.xml

        searchEditText = findViewById(R.id.search)
        searchButton = findViewById(R.id.searchButton)
        userList = findViewById(R.id.userList)
        txt_back = findViewById(R.id.back)
        userPreferences = UserPreferences(this)


        searchButton.setOnClickListener {
            onClick()
        }

        txt_back.setOnClickListener {
            val intent = Intent(this, UserHomeAct::class.java)
            startActivity(intent)
        }
    }

    private fun onClick() {
        val query = searchEditText.text.toString()
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            return
        }

//        userList.removeAllViews()
        userList.removeAllViewsInLayout()

        // Retrieve users from SharedPreferences
        var users = userPreferences.getUsers()
        if (users == null || users.isEmpty()) {
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show()
            return
        }

        // Filter users by query using full name and email
        var filteredUsers = users.filter { user -> user.fullname?.contains(query, ignoreCase = true) == true || user.email?.contains(query, ignoreCase = true) == true }

        // Create a frame (CardView) to display the number of users found
        val frameLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 16, 16, 16) // Add margin around the frame
            }
            setPadding(16, 16, 16, 16) // Add padding inside the frame
            orientation = LinearLayout.VERTICAL
            background = resources.getDrawable(R.drawable.frame_background, null) // Set a background drawable
        }

            // add the number of users found to the end of the list
        // TextView for displaying the number of users found
        val countTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = "${filteredUsers.size} users found"
            textSize = 20f
            gravity = Gravity.CENTER // Center text in the TextView
            typeface = ResourcesCompat.getFont(context, R.font.comici)
        }



        // Add the TextView to the frame
        frameLayout.addView(countTextView)

        // Add the frame to the user list
        userList.addView(frameLayout)


// Display filtered users by adding them to the linear layout of the scroll view with the form of text view
        for (user in filteredUsers) {

            // Customize each text view, add the horizontal line below
            val line = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
                )
                setBackgroundColor(resources.getColor(R.color.sad))
            }
            userList.addView(line)
            // Create a horizontal LinearLayout to hold the TextView and the buttons
            val horizontalLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.HORIZONTAL
            }

            // Create the TextView for displaying user information
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0, // Weight is set to 0 so that it takes up space proportionally with the buttons
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // Weight of 1 makes it take up the remaining space after the buttons
                )
                text = "Username: ${user.username}\nFullname: ${user.fullname}\nEmail: ${user.email}"
                setPadding(0, 0, 0, 30)
            }

            // Create the "Edit" button
            val editButton = Button(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Edit"
                setOnClickListener {
                    // Handle edit action here
                    // Start the EditUserActivity with the selected user
//                    val intent = Intent(this@SearchAct, EditAct::class.java)
                    val intent = Intent(this@SearchAct, EditAct::class.java)
                    intent.putExtra("user", user)

                    startActivity(intent)
                }
            }
            var checkDeleteButton = false
// Create the "Delete" button
            val deleteButton = Button(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Delete"
                setOnClickListener {
                    // Show confirmation dialog
                    AlertDialog.Builder(this@SearchAct).apply {
                        setTitle("Confirm Delete")
                        setMessage("Are you sure you want to delete this user?")

                        // Set positive button to handle delete action
                        setPositiveButton("Yes") { _, _ ->
                            checkDeleteButton = true

                            // Remove the user from the list
                            var newUsersList: Array<User> = emptyArray()

                            for ( savedUser in users!!) {
                                if (savedUser.username != user.username) {
                                    newUsersList = newUsersList.plus(savedUser)
                                }
                            }


                            // Save the updated user list in UserPreferences
                            userPreferences.addUser(newUsersList)

                            // Remove the UI elements corresponding to the deleted user
                            userList.removeView(horizontalLayout)
                            userList.removeView(line)
//

                            // Filter users by query using full name and email
                            filteredUsers = newUsersList.filter { user -> user.fullname?.contains(query, ignoreCase = true) == true || user.email?.contains(query, ignoreCase = true) == true }
                            countTextView.text = "${filteredUsers.size} users found"

                            users = userPreferences.getUsers()

                            // Show a toast message
                            Toast.makeText(this@SearchAct, "User deleted", Toast.LENGTH_SHORT).show()




                        }

                        // Set negative button to cancel the action
                        setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }

                        create().show()  // Show the dialog
                    }
                }
            }


            // Add the TextView and buttons to the horizontal layout
            horizontalLayout.addView(textView)
            horizontalLayout.addView(editButton)
            horizontalLayout.addView(deleteButton)

            // Add the horizontal layout to the main layout (userList)
            userList.addView(horizontalLayout)

        }

    }
}
