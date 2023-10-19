package com.yofhi.aplikasigithubuser.view.adapter

import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yofhi.aplikasigithubuser.data.helper.FavoriteDiffCallback
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity
import com.yofhi.aplikasigithubuser.databinding.ListDataUserBinding
import com.yofhi.aplikasigithubuser.view.ui.details.DetailActivity

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoritUserViewHolder>(){
    private val listFavorite = ArrayList<FavoritUserEntity>()

    fun setFavorites(favoritUserEntity: List<FavoritUserEntity>){
        val diffCallback = FavoriteDiffCallback(this.listFavorite, favoritUserEntity)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorite.clear()
        this.listFavorite.addAll(favoritUserEntity)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoritUserViewHolder(private val view: ListDataUserBinding): RecyclerView.ViewHolder(view.root){
        fun bind(favoritUserEntity: FavoritUserEntity){
            with(view){
                usernameTextView.text = favoritUserEntity.username
                nameUserTextView.text = favoritUserEntity.id.toString()
                Glide.with(itemView.context).load(favoritUserEntity.avatar).into(avatarImageView)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_USER, favoritUserEntity.username)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): FavoritUserViewHolder {
        val listDataUserBinding = ListDataUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritUserViewHolder(listDataUserBinding)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoritUserViewHolder, position: Int) {
        val favorites = listFavorite[position]
        holder.bind(favorites)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

}