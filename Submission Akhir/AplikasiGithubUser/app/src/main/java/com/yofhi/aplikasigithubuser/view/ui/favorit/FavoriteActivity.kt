package com.yofhi.aplikasigithubuser.view.ui.favorit

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yofhi.aplikasigithubuser.R
import com.yofhi.aplikasigithubuser.databinding.ActivityFavoriteBinding
import com.yofhi.aplikasigithubuser.view.adapter.FavoriteAdapter
import com.yofhi.aplikasigithubuser.viewModel.FavoriteViewModel
import com.yofhi.aplikasigithubuser.viewModel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val viewModel = obtainViewModel(this)

        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        val appBarColor = if (isDarkTheme) {
            ContextCompat.getColor(this, R.color.soft_green)
        } else {
            ContextCompat.getColor(this, R.color.purple_700)
        }

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(appBarColor))
            setDisplayHomeAsUpEnabled(true)
            title = "Favorit"
            elevation = 0f
        }

        if (!viewModel.getAllFavorites().hasActiveObservers()){
            viewModel.getAllFavorites().observe(this) { favoriteList ->
            if (favoriteList != null) {
                adapter.setFavorites(favoriteList)
            }
        }
        }
        adapter = FavoriteAdapter()
        binding?.rvFavorites?.layoutManager = LinearLayoutManager(this)
        binding?.rvFavorites?.setHasFixedSize(false)
        binding?.rvFavorites?.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel{
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}