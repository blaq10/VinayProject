package com.hylton.vinayproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class UserCustomAdapter internal constructor(context: Context) : RecyclerView.Adapter<UserCustomAdapter.UserViewHolder>(){

    private val inflater : LayoutInflater = LayoutInflater.from(context)
    private var user = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = user[position]

        holder.firstName.text = currentUser.firstName
        holder.lastName.text = currentUser.lastName
        holder.email.text = currentUser.email

        holder.itemView.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return user.size
    }

    internal fun setUser(users: List<User>) {
        this.user = users
        notifyDataSetChanged()
    }

    inner class UserViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {

        val firstName : TextView = viewItem.findViewById(R.id.first_name_text_field)
        val lastName : TextView = viewItem.findViewById(R.id.last_name_text_field)
        val email : TextView = viewItem.findViewById(R.id.email_text_field)

    }

    interface OnUserClickListener {
        fun onUserClick(position: Int)
    }
}