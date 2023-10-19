package com.yofhi.aplikasigithubuser.view.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yofhi.aplikasigithubuser.data.utils.Constant.TAB_TITLES
import com.yofhi.aplikasigithubuser.view.ui.details.FollowersFragment
import com.yofhi.aplikasigithubuser.view.ui.details.FollowingFragment

class DetailAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = TAB_TITLES.size

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.getInstance(username)
            1 -> fragment = FollowingFragment.getInstance(username)
        }
        return fragment as Fragment
    }
}