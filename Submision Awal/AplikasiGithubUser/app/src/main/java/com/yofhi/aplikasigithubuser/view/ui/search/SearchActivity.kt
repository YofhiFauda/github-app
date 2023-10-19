package com.yofhi.aplikasigithubuser.view.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.yofhi.aplikasigithubuser.R
import com.yofhi.aplikasigithubuser.data.response.User
import com.yofhi.aplikasigithubuser.data.utils.Resource
import com.yofhi.aplikasigithubuser.data.utils.StateCallback
import com.yofhi.aplikasigithubuser.databinding.ActivitySearchBinding
import com.yofhi.aplikasigithubuser.view.adapter.UserAdapter
import com.yofhi.aplikasigithubuser.viewModel.SearchViewModel

class SearchActivity : AppCompatActivity(), StateCallback<List<User>> {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        binding.includeMainSearch.listRecyclerViewUser.apply {
            adapter = userAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.searchAppBar).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchUser(query).observe(this@SearchActivity) { a ->
                    when (a) {
                        is Resource.Loading -> onLoading()
                        is Resource.Success -> a.data?.let { b -> onSuccess(b) }
                        else -> onFailed(a.message)
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean = false
        })
        return true 
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
        binding.includeMainSearch.apply {
            listRecyclerViewUser.visibility = visible
            messageSearchTextView.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding.includeMainSearch.apply {
            progressBar.visibility = visible
            messageSearchTextView.visibility = gone
            listRecyclerViewUser.visibility = gone
        }
    }

    override fun onFailed(message: String?) {
        binding.includeMainSearch.apply {
            if (message.isNullOrEmpty()) {
                messageSearchTextView.apply {
                    text = resources.getString(R.string.user_not_found)
                    visibility = visible
                }
            }
            messageSearchTextView.apply {
                text = message
                visibility = visible
            }
            progressBar.visibility = gone
            listRecyclerViewUser.visibility = gone
        }
    }
}