package com.yofhi.aplikasigithubuser.view.ui.details

import android.content.Intent.EXTRA_USER
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.yofhi.aplikasigithubuser.data.response.User
import com.yofhi.aplikasigithubuser.data.utils.Constant.TAB_TITLES
import com.yofhi.aplikasigithubuser.data.utils.Resource
import com.yofhi.aplikasigithubuser.data.utils.StateCallback
import com.yofhi.aplikasigithubuser.databinding.ActivityDetailBinding
import com.yofhi.aplikasigithubuser.view.adapter.DetailAdapter
import com.yofhi.aplikasigithubuser.viewModel.DetailViewModel

class DetailActivity : AppCompatActivity(), StateCallback<User?> {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER)
        val detailAdapter = DetailAdapter(this, username.toString())

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        viewModel.getDetailUser(username!!).observe(this) {
            when (it) {
                is Resource.Error -> onFailed(it.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> onSuccess(it.data)
            }
        }

        binding.apply {
            viewPager.adapter = detailAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onSuccess(data: User?) {
        binding.progressBar.visibility = View.INVISIBLE
        binding.avatarImageView.visibility = View.VISIBLE
        binding.nameUserTextView.visibility = View.VISIBLE
        binding.usernameTextView.visibility = View.VISIBLE
        binding.companyUserTextView.visibility = View.VISIBLE
        binding.locationUserTextView.visibility = View.VISIBLE
        binding.totalRepositoryUserTextView.visibility = View.VISIBLE
        binding.totalFollowersUserTextView.visibility = View.VISIBLE
        binding.totalFollowingUserTextView.visibility = View.VISIBLE
        binding.apply {
            supportActionBar?.title = data?.username
            Glide.with(this@DetailActivity)
                .load(data?.avatar)
                .timeout(6000)
                .into(avatarImageView)
            nameUserTextView.text = data?.name
            usernameTextView.text = data?.username
            companyUserTextView.text = data?.company
            locationUserTextView.text = data?.location
            totalRepositoryUserTextView.text = data?.repository.toString()
            totalFollowersUserTextView.text = data?.follower.toString()
            totalFollowingUserTextView.text = data?.following.toString()
        }
    }

    override fun onLoading() {
        binding.apply {
            binding.progressBar.visibility = View.VISIBLE
            binding.avatarImageView.visibility = View.INVISIBLE
            binding.nameUserTextView.visibility = View.INVISIBLE
            binding.usernameTextView.visibility = View.INVISIBLE
            binding.companyUserTextView.visibility = View.INVISIBLE
            binding.locationUserTextView.visibility = View.INVISIBLE
            binding.totalRepositoryUserTextView.visibility = View.INVISIBLE
            binding.totalFollowersUserTextView.visibility = View.INVISIBLE
            binding.totalFollowingUserTextView.visibility = View.INVISIBLE
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
        binding.progressBar.visibility = View.INVISIBLE
        binding.avatarImageView.visibility = View.INVISIBLE
        binding.nameUserTextView.visibility = View.INVISIBLE
        binding.usernameTextView.visibility = View.INVISIBLE
        binding.companyUserTextView.visibility = View.INVISIBLE
        binding.locationUserTextView.visibility = View.INVISIBLE
        binding.totalRepositoryUserTextView.visibility = View.INVISIBLE
        binding.totalFollowersUserTextView.visibility = View.INVISIBLE
        binding.totalFollowingUserTextView.visibility = View.INVISIBLE
        }
        Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }
}