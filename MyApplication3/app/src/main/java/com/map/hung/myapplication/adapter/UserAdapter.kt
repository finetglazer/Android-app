package com.map.hung.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.map.hung.myapplication.R
import com.map.hung.myapplication.model.User

class UserAdapter(private val userList: MutableList<User>, private val listener: OnUserClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnUserClickListener {
        fun onEditClick(user: User)
        fun onDeleteClick(user: User)


    }


    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.userName)
        val tvEmail: TextView = itemView.findViewById(R.id.userEmail)
        val btnEdit: Button = itemView.findViewById(R.id.editButton)
        val btnDelete: Button = itemView.findViewById(R.id.deleteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.tvName.text = currentItem.name
        holder.tvEmail.text = currentItem.email

        holder.btnEdit.setOnClickListener {
            listener.onEditClick(currentItem)
        }
        holder.btnDelete.setOnClickListener {
            listener.onDeleteClick(currentItem)
        }
    }

    override fun getItemCount() = userList.size

    fun addUser(user: User) {
        userList.add(user)
        notifyItemInserted(userList.size - 1)// notifyItemInserted() is used to update the RecyclerView and display the new item.
    }

    fun updateUser(user: User) {
        val index = userList.indexOfFirst { it.id == user.id }
        if (index != -1) {
            userList[index] = user
            notifyItemChanged(index)
        }
    }

    fun deleteUser(user: User) {
        val index = userList.indexOfFirst { it.id == user.id }
        if (index != -1) {
            userList.removeAt(index)
            notifyItemRemoved(index)
        }
    }




}