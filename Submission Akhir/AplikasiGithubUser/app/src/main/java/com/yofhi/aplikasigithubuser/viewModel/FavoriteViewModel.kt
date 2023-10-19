package com.yofhi.aplikasigithubuser.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity
import com.yofhi.aplikasigithubuser.data.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavorites(): LiveData<List<FavoritUserEntity>> = mFavoriteUserRepository.getAllFavorites()

}