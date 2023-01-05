package com.example.soeco.ui.admin.users

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.soeco.R
import com.example.soeco.data.Models.CustomData

class UsersAdapter(
    private val optionsMenuClickListener: (view: View, user: CustomData) -> Unit
): ListAdapter<CustomData, UsersAdapter.UserViewHolder>(DataDiffCallback()) {

    inner class UserViewHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView) {
        val name: TextView
        val email: TextView
        val role: TextView
        val menuBtn: ImageButton

        init {
            name = itemView.findViewById(R.id.tvName)
            email = itemView.findViewById(R.id.tvEmail)
            role = itemView.findViewById(R.id.tvRole)
            menuBtn = itemView.findViewById(R.id.btnMenu)
        }

        @SuppressLint("SetTextI18n")
        fun bind(user: CustomData){
            name.text = "${user.firstname} ${user.lastname}"
            email.text = user.email
            role.text = user.role?.uppercase()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)

        return UserViewHolder(view, view.context)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
            menuBtn.setOnClickListener { view ->
                optionsMenuClickListener.invoke(view, getItem(position))
            }
        }
    }

    class DataDiffCallback: DiffUtil.ItemCallback<CustomData>(){
        override fun areItemsTheSame(oldItem: CustomData, newItem: CustomData): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: CustomData, newItem: CustomData): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: CustomData, newItem: CustomData): Any? {
            return super.getChangePayload(oldItem, newItem)
        }
    }
}