package com.yofhi.aplikasigithubuser.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity
import com.yofhi.aplikasigithubuser.data.remote.response.User
import com.yofhi.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.yofhi.aplikasigithubuser.data.repository.FavoriteUserRepository
import com.yofhi.aplikasigithubuser.data.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val retrofit = ApiConfig.create()
    val user = MutableLiveData<Resource<User>>()
    private val listUserFollowers = MutableLiveData<Resource<List<User>>>()
    private val listUserFollowing = MutableLiveData<Resource<List<User>>>()
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(favoritUserEntity: FavoritUserEntity) {
        mFavoriteUserRepository.insert(favoritUserEntity)
    }

    fun delete(id: Int) {
        mFavoriteUserRepository.delete(id)
    }

    fun getAllFavorites(): LiveData<List<FavoritUserEntity>> = mFavoriteUserRepository.getAllFavorites()


    fun getDetailUser(username: String): LiveData<Resource<User>> {
        user.postValue(Resource.Loading())
        retrofit.getDetailUser(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("API_RESPONSE", response.toString())
                val result = response.body()
                user.postValue(Resource.Success(result))
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                user.postValue(Resource.Error(t.message))
            }
        })
        return user
    }

    fun getUserFollowers(username: String): LiveData<Resource<List<User>>> {
        listUserFollowers.postValue(Resource.Loading())
        retrofit.getUserFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Log.d("API_RESPONSE", response.toString())
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listUserFollowers.postValue(Resource.Error(null))
                } else {
                    listUserFollowers.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUserFollowers.postValue(Resource.Error(t.message))
            }
        })
        return listUserFollowers
    }

    fun getUserFollowing(username: String): LiveData<Resource<List<User>>> {
        listUserFollowing.postValue(Resource.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Log.d("API_RESPONSE", response.toString())
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    listUserFollowing.postValue(Resource.Error(null))
                } else {
                    listUserFollowing.postValue(Resource.Success(list))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUserFollowing.postValue(Resource.Error(t.message))
            }
        })
        return listUserFollowing
    }
}