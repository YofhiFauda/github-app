package com.yofhi.aplikasigithubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.yofhi.aplikasigithubuser.data.remote.response.SearchResponse
import com.yofhi.aplikasigithubuser.data.remote.response.User
import com.yofhi.aplikasigithubuser.data.remote.retrofit.ApiConfig
import com.yofhi.aplikasigithubuser.data.utils.Resource
import com.yofhi.aplikasigithubuser.view.ui.setting.SettingsPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val pref: SettingsPreferences) : ViewModel() {
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
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}