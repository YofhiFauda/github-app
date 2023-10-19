package com.yofhi.aplikasigithubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity

@Database(entities = [FavoritUserEntity::class], version = 1, exportSchema = false)
abstract class FavoritRoomDatabase : RoomDatabase() {
    abstract fun favoritUserDao(): FavoritUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoritRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(FavoritRoomDatabase::class.java) {
                val instance2 = INSTANCE
                if (instance2 != null) {
                    return instance2
                }

                val createdInstance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritRoomDatabase::class.java, "favorite_user"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = createdInstance
                return createdInstance
            }
        }
    }
}