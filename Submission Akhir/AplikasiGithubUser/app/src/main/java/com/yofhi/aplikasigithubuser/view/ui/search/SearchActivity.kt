package com.yofhi.aplikasigithubuser.view.ui.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yofhi.aplikasigithubuser.R
import com.yofhi.aplikasigithubuser.data.remote.response.User
import com.yofhi.aplikasigithubuser.data.utils.Resource
import com.yofhi.aplikasigithubuser.data.utils.StateCallback
import com.yofhi.aplikasigithubuser.databinding.ActivitySearchBinding
import com.yofhi.aplikasigithubuser.view.adapter.UserAdapter
import com.yofhi.aplikasigithubuser.view.ui.favorit.FavoriteActivity
import com.yofhi.aplikasigithubuser.view.ui.setting.SettingsPreferences
import com.yofhi.aplikasigithubuser.view.ui.setting.ThemeSettingsActivity
import com.yofhi.aplikasigithubuser.viewModel.SearchViewModel
import com.yofhi.aplikasigithubuser.viewModel.SettingsViewModelFactory


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private lateinit var searchViewModel: SearchViewModel

class SearchActivity : AppCompatActivity(), StateCallback<List<User>> {

    private lateinit var userAdapter: UserAdapter
    private val viewModel by viewModels<SearchViewModel>()
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val pref = SettingsPreferences.getInstance(dataStore)
        searchViewModel = ViewModelProvider(this, SettingsViewModelFactory(pref)).get(SearchViewModel::class.java)

        userAdapter = UserAdapter()
        binding?.includeMainSearch?.listRecyclerViewUser?.apply {
            adapter = userAdapter
            layoutManager =
                LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        searchViewModel.getThemeSettings().observe(this) { isLightModeActive: Boolean ->
            if (isLightModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.soft_green)))
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.purple_700)))
            }
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

    /**
     * Function when item on menu is clicked.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_setting -> {
                val intent = Intent(this@SearchActivity, ThemeSettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.btn_favorit -> {
                val intent = Intent(this@SearchActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
        binding?.includeMainSearch?.apply {
            listRecyclerViewUser.visibility = visible
            messageSearchTextView.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding?.includeMainSearch?.apply {
            progressBar.visibility = visible
            messageSearchTextView.visibility = gone
            listRecyclerViewUser.visibility = gone
        }
    }

    override fun onFailed(message: String?) {
        binding?.includeMainSearch?.apply {
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