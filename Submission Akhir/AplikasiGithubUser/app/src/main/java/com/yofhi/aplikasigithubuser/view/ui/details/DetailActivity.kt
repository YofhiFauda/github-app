package com.yofhi.aplikasigithubuser.view.ui.details

import android.content.Intent.EXTRA_USER
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.yofhi.aplikasigithubuser.R
import com.yofhi.aplikasigithubuser.data.helper.Helper
import com.yofhi.aplikasigithubuser.data.local.entity.FavoritUserEntity
import com.yofhi.aplikasigithubuser.data.remote.response.User
import com.yofhi.aplikasigithubuser.data.utils.Constant.TAB_TITLES
import com.yofhi.aplikasigithubuser.data.utils.Resource
import com.yofhi.aplikasigithubuser.data.utils.StateCallback
import com.yofhi.aplikasigithubuser.databinding.ActivityDetailBinding
import com.yofhi.aplikasigithubuser.view.adapter.DetailAdapter
import com.yofhi.aplikasigithubuser.viewModel.DetailViewModel
import com.yofhi.aplikasigithubuser.viewModel.ViewModelFactory

class DetailActivity : AppCompatActivity(), StateCallback<User?> {

    private var binding: ActivityDetailBinding? = null
    private val viewModel: DetailViewModel by lazy {
    ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(DetailViewModel::class.java)
    }

    private val helper = Helper()
    private var detailUser: User? = null
    private var buttonState: Boolean = false
    private var favoritUserEntity: FavoritUserEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val username = intent.getStringExtra(EXTRA_USER)
        val detailAdapter = DetailAdapter(this, username.toString())
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
            elevation = 0f
        }

        if (username == null) {
            // Handle kasus dimana username adalah null, misalnya dengan menampilkan pesan kesalahan.
            Toast.makeText(this@DetailActivity, "Username is missing.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (!viewModel.getDetailUser(username).hasActiveObservers()) {
            viewModel.getDetailUser(username).observe(this) { resources ->
                when (resources) {
                    is Resource.Error -> onFailed(resources.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> {
                        onSuccess(resources.data)
                        detailUser = resources.data
                        detailUser?.let { user ->
                            favoritUserEntity = FavoritUserEntity(user.id, user.username, user.avatar)
                        }
                        viewModel.getAllFavorites().observe(this) { favoriteList ->
                            if (favoriteList != null) {
                                buttonState = favoriteList.any { it.id == detailUser?.id }
                                binding?.fab?.setImageResource(if (buttonState) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)
                            } else {
                                Toast.makeText(this@DetailActivity, resources.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this@DetailActivity, resources.message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding?.apply {
            viewPager.adapter = detailAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        binding?.fab?.setOnClickListener {
            if (!buttonState) {
                buttonState = true
                binding?.fab?.setImageResource(R.drawable.ic_baseline_favorite_24)
                insertToDatabase(detailUser)
            } else {
                buttonState = false
                binding?.fab?.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                viewModel.delete(detailUser?.id ?: 0)
                helper.showToast(this, "Favorite user has been deleted.")
            }
        }
    }

    private fun insertToDatabase(detailList: User?) {
        favoritUserEntity?.let { favoritUser ->
            detailList?.let { detail ->
                favoritUser.id = detail.id
                favoritUser.username = detail.username
                favoritUser.avatar = detail.avatar
                favoritUserEntity?.let { entity ->
                    viewModel.insert(entity)
                }
                helper.showToast(this, "User has been favorited.")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    override fun onSuccess(data: User?) {
        binding?.progressBar?.visibility = View.INVISIBLE
        binding?.avatarImageView?.visibility = View.VISIBLE
        binding?.nameUserTextView?.visibility = View.VISIBLE
        binding?.usernameTextView?.visibility = View.VISIBLE
        binding?.companyUserTextView?.visibility = View.VISIBLE
        binding?.locationUserTextView?.visibility = View.VISIBLE
        binding?.totalRepositoryUserTextView?.visibility = View.VISIBLE
        binding?.totalFollowersUserTextView?.visibility = View.VISIBLE
        binding?.totalFollowingUserTextView?.visibility = View.VISIBLE
        binding?.fab?.visibility = View.VISIBLE
        binding?.apply {
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
        binding?.apply {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.avatarImageView?.visibility = View.INVISIBLE
            binding?.nameUserTextView?.visibility = View.INVISIBLE
            binding?.usernameTextView?.visibility = View.INVISIBLE
            binding?.companyUserTextView?.visibility = View.INVISIBLE
            binding?.locationUserTextView?.visibility = View.INVISIBLE
            binding?.totalRepositoryUserTextView?.visibility = View.INVISIBLE
            binding?.totalFollowersUserTextView?.visibility = View.INVISIBLE
            binding?.totalFollowingUserTextView?.visibility = View.INVISIBLE
            binding?.fab?.visibility = View.INVISIBLE
        }
    }

    override fun onFailed(message: String?) {
        binding?.apply {
        binding?.progressBar?.visibility = View.INVISIBLE
        binding?.avatarImageView?.visibility = View.INVISIBLE
        binding?.nameUserTextView?.visibility = View.INVISIBLE
        binding?.usernameTextView?.visibility = View.INVISIBLE
        binding?.companyUserTextView?.visibility = View.INVISIBLE
        binding?.locationUserTextView?.visibility = View.INVISIBLE
        binding?.totalRepositoryUserTextView?.visibility = View.INVISIBLE
        binding?.totalFollowersUserTextView?.visibility = View.INVISIBLE
        binding?.totalFollowingUserTextView?.visibility = View.INVISIBLE
        binding?.fab?.visibility = View.INVISIBLE
        }
        Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}