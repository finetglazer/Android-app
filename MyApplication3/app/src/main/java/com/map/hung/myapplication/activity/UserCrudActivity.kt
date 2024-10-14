package com.map.hung.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.adapter.UserAdapter
import com.map.hung.myapplication.model.User

class UserCrudActivity : AppCompatActivity(), UserAdapter.OnUserClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private var userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_item)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(userList, this)
        recyclerView.adapter = userAdapter

        // Example data
        userList.add(User(1, "Alice", "alice@gmail.com"))
        userList.add(User(2, "Bob","bob@gmail.com"))
    }

    override fun onEditClick(user: User) {
        val intent = Intent(this, AddEditUserActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    override fun onDeleteClick(user: User) {
        userAdapter.deleteUser(user)
    }

    // For adding/updating user
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val user = data?.getParcelableExtra<User>("user")
            if (user != null) {
                userAdapter.addUser(user)
            }


        }
    }
}