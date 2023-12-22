package com.example.suitmediaintern

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class UserAdapter(private val users: MutableList<User>, private val onItemClick: (User) -> Unit) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { onItemClick(user) }
    }

    override fun getItemCount(): Int = users.size

    fun addUsers(newUsers: List<User>) {
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    fun clearData() {
        users.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userEmailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)
        private val userProfileImageView: ImageView = itemView.findViewById(R.id.userProfileImageView)

        fun bind(user: User) {
            val fullName = "${user.first_name} ${user.last_name}"
            userNameTextView.text = fullName
            userEmailTextView.text = user.email

            Picasso.get().load(user.avatar).into(userProfileImageView)
        }
    }
}
