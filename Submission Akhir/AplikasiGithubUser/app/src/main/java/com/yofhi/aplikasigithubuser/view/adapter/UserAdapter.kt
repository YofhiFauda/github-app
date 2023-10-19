package com.yofhi.aplikasigithubuser.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yofhi.aplikasigithubuser.data.remote.response.User
import com.yofhi.aplikasigithubuser.databinding.ListDataUserBinding
import com.yofhi.aplikasigithubuser.view.ui.details.DetailActivity

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val listUser = ArrayList<User>()

    inner class UserViewHolder(private val view: ListDataUserBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(user: User) {
            with(view) {
                usernameTextView.text = user.username
                nameUserTextView.text = user.id.toString()
                Glide.with(itemView.context).load(user.avatar).into(avatarImageView)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_USER, user.username)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ListDataUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAllUser(user: List<User>) {
        listUser.apply {
            clear()
            addAll(user)
        }
        notifyDataSetChanged()
    }
}
