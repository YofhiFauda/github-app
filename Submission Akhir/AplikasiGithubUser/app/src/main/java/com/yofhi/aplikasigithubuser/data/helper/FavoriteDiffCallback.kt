package com.yofhi.aplikasigithubuser.data.helper

import androidx.recyclerview.widget.DiffUtil
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity

class FavoriteDiffCallback(private val mOldFavList: List<FavoritUserEntity>, private val mNewFavList: List<FavoritUserEntity>):
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldFavList.size
    }

    override fun getNewListSize(): Int {
        return mNewFavList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavList = mOldFavList[oldItemPosition]
        val newFavList = mNewFavList[newItemPosition]
        return oldFavList.username == newFavList.username && oldFavList.avatar == newFavList.avatar
    }


}