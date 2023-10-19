package com.yofhi.aplikasigithubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yofhi.aplikasigithubuser.data.response.SearchResponse
import com.yofhi.aplikasigithubuser.data.response.User
import com.yofhi.aplikasigithubuser.data.retrofit.ApiConfig
import com.yofhi.aplikasigithubuser.data.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val retrofit = ApiConfig.create()
    private val listUser = MutableLiveData<Resource<List<User>>>()

    fun searchUser(query: String): LiveData<Resource<List<User>>> {
        listUser.postValue(Resource.Loading())
        retrofit.searchUsers(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val list = response.body()?.items
                if (list.isNullOrEmpty()) {
                    listUser.postValue(Resource.Error(null))
                } else {
                    listUser.postValue(Resource.Success(data = list))
                }
                Log.d("API_RESPONSE", response.toString())
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }
}