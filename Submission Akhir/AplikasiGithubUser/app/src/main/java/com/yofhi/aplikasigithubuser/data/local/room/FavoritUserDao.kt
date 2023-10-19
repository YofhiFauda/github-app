package com.yofhi.aplikasigithubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity

@Dao
interface FavoritUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoritUserEntity: FavoritUserEntity)

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    fun removeFavorite(id: Int)

    @Query("SELECT * FROM favorite_user ORDER BY login ASC")
    fun getAllUser(): LiveData<List<FavoritUserEntity>>

    @Query("SELECT * FROM favorite_user WHERE favorite_user.id = :id")
    fun getUserById(id: Int): LiveData<FavoritUserEntity>

}


