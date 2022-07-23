package com.dicoding.picodiploma.submission_alfan.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.picodiploma.submission_alfan.ui.fragment.FollowersFragment
import com.dicoding.picodiploma.submission_alfan.ui.fragment.FollowingFragment
import com.dicoding.picodiploma.submission_alfan.ui.fragment.RepositoryFragment

class SectionPageAdapter(activity: AppCompatActivity, bundle: Bundle) :
    FragmentStateAdapter(activity) {
    private val fragBundle = bundle

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var allFragment: Fragment? = null

        when (position) {
            0 -> allFragment = FollowersFragment()
            1 -> allFragment = FollowingFragment()
            2 -> allFragment = RepositoryFragment()
        }

        allFragment?.arguments = this.fragBundle
        return allFragment as Fragment
    }

}