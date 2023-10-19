package com.yofhi.aplikasigithubuser.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity
import com.yofhi.aplikasigithubuser.data.local.room.FavoritRoomDatabase
import com.yofhi.aplikasigithubuser.data.local.room.FavoritUserDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoritUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoritRoomDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoritUserDao()
    }

    fun getAllFavorites(): LiveData<List<FavoritUserEntity>> = mFavoriteUserDao.getAllUser()

    fun insert(favoritUserEntity: FavoritUserEntity) {
        executorService.execute { mFavoriteUserDao.insertFavorite(favoritUserEntity) }
    }

    fun delete(id: Int) {
        executorService.execute { mFavoriteUserDao.removeFavorite(id) }
    }
}