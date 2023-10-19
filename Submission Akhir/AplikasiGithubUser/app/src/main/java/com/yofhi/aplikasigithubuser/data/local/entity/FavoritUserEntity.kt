package com.yofhi.aplikasigithubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")

data class FavoritUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "login")
    var username: String,

    @ColumnInfo(name = "avatar_url")
    var avatar: String,

)
