package com.yofhi.aplikasigithubuser.view.ui.details

import android.content.Intent.EXTRA_USER
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yofhi.aplikasigithubuser.R
import com.yofhi.aplikasigithubuser.data.remote.response.User
import com.yofhi.aplikasigithubuser.data.utils.Resource
import com.yofhi.aplikasigithubuser.data.utils.StateCallback
import com.yofhi.aplikasigithubuser.databinding.FragmentFollowingBinding
import com.yofhi.aplikasigithubuser.view.adapter.UserAdapter
import com.yofhi.aplikasigithubuser.viewModel.DetailViewModel

class FollowingFragment : Fragment(), StateCallback<List<User>> {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private lateinit var userAdapter: UserAdapter
    private var username: String? = null

    companion object {
        fun getInstance(username: String): Fragment = FollowingFragment().apply {
            arguments = Bundle().apply { putString(EXTRA_USER, username) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { username = it.getString(EXTRA_USER) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        userAdapter = UserAdapter()
        binding.listFollowingUserRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getUserFollowing(username.toString()).observe(viewLifecycleOwner) { a ->
            when (a) {
                is Resource.Error -> onFailed(a.message)
                is Resource.Loading -> onLoading()
                is Resource.Success -> a.data?.let { b -> onSuccess(b) }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(data: List<User>) {
        userAdapter.setAllUser(data)
        binding.apply {
            listFollowingUserRecyclerView.visibility = visible
            messageSearchTextView.visibility = gone
            progressBar.visibility = gone
        }
    }

    override fun onLoading() {
        binding.apply {
            listFollowingUserRecyclerView.visibility = gone
            messageSearchTextView.visibility = gone
            progressBar.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        binding.apply {
            if (message == null) {
                messageSearchTextView.text = resources.getString(R.string.following_not_found)
                messageSearchTextView.visibility = visible
            }
            messageSearchTextView.text = message
            messageSearchTextView.visibility = visible
            listFollowingUserRecyclerView.visibility = gone
            progressBar.visibility = gone
        }
    }
}