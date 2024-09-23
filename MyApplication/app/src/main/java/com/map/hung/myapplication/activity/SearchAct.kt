package com.map.hung.myapplication.activity

//import UserPreferences
//import android.app.Activity
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User
import java.text.SimpleDateFormat
import java.util.Locale


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

            // Create a horizontal LinearLayout to hold the TextView and the Spinner
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
                    0,  // Weight is set to 0 so that it takes up space proportionally with the spinner
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f  // Weight of 1 makes it take up the remaining space after the spinner
                )
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDob = user.dob?.let { dateFormat.format(it) } ?: "No Date Available"
                text = "Username: ${user.username}\nFullname: ${user.fullname}\nEmail: ${user.email}\nGender: ${user.gender}\nDob: ${formattedDob}"
                setPadding(0, 0, 0, 30)
            }

            // Add a Button or other UI element to trigger the PopupMenu
            val moreOptionsButton = Button(this).apply {


                setBackgroundColor(resources.getColor(R.color.white))
                // Get the drawable and resize it
                val drawable = ContextCompat.getDrawable(context, R.drawable.icon_menu)

                // Set the size (width and height in pixels)
                val iconSize = resources.getDimensionPixelSize(R.dimen.icon_size)  // Or any specific size in pixels
                drawable?.setBounds(0, 0, iconSize, iconSize)  // Set bounds for width and height

                setText("Options")

                // Add the resized drawable to the right of the text
                setCompoundDrawables(null, null, drawable, null)

                // Optional: Set text if needed

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }


            // Add the button to your layout (Assuming `userList` is your layout)
//            userList.addView(moreOptionsButton)

            // Create the PopupMenu when the button is clicked
            moreOptionsButton.setOnClickListener {
                val popupMenu = PopupMenu(this@SearchAct, moreOptionsButton)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

                // Handle item selection
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.edit_option -> {
                            // "Edit" selected
                            val intent = Intent(this@SearchAct, EditAct::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            true
                        }
                        R.id.delete_option -> {
                            // "Delete" selected
                            AlertDialog.Builder(this@SearchAct).apply {
                                setTitle("Confirm Delete")
                                setMessage("Are you sure you want to delete this user?")

                                setPositiveButton("Yes") { _, _ ->
                                    var newUsersList: Array<User> = emptyArray()
                                    for (savedUser in users!!) {
                                        if (savedUser.username != user.username) {
                                            newUsersList = newUsersList.plus(savedUser)
                                        }
                                    }

                                    userPreferences.addUser(newUsersList)

                                    // Remove the UI elements corresponding to the deleted user
                                    userList.removeView(horizontalLayout)
                                    userList.removeView(line)

                                    filteredUsers = newUsersList.filter {
                                        it.fullname?.contains(query, ignoreCase = true) == true ||
                                                it.email?.contains(query, ignoreCase = true) == true
                                    }
                                    countTextView.text = "${filteredUsers.size} users found"

                                    users = userPreferences.getUsers()

                                    Toast.makeText(this@SearchAct, "User deleted", Toast.LENGTH_SHORT).show()
                                }

                                setNegativeButton("No") { dialog, _ ->
                                    dialog.dismiss()
                                }

                                create().show()
                            }
                            true
                        }
                        else -> false
                    }
                }

                // Show the popup menu
                popupMenu.show()
            }


            // Add the TextView and spinner to the horizontal layout
            horizontalLayout.addView(textView)
            horizontalLayout.addView(moreOptionsButton)

            // Add the horizontal layout to the main layout (userList)
            userList.addView(horizontalLayout)

            // test


        }


    }
}
