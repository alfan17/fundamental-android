package com.dicoding.picodiploma.submission_alfan.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.submission_alfan.R
import com.dicoding.picodiploma.submission_alfan.adapter.UserGithubAdapter
import com.dicoding.picodiploma.submission_alfan.databinding.FragmentFollowingBinding
import com.dicoding.picodiploma.submission_alfan.ui.activity.UserDetailActivity
import com.dicoding.picodiploma.submission_alfan.viewmodel.FollowingViewModel

class FollowingFragment : Fragment(R.layout.fragment_following) {
    private var FwingFragBinding: FragmentFollowingBinding? = null
    private lateinit var FwingFragAdapter: UserGithubAdapter
    private lateinit var FwingVM: FollowingViewModel
    private lateinit var nameUser: String

    private fun showFollowingUserGithub(followingValue: Boolean) {
        if (followingValue) {
            FwingFragBinding?.followingProgressBar?.visibility = View.VISIBLE
        } else {
            FwingFragBinding?.followingProgressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameUser = arguments?.getString(UserDetailActivity.EXTRA_DETAIL_USER).toString()

        FwingFragBinding = FragmentFollowingBinding.bind(view)

        val followingRV = FwingFragBinding?.followingRv
        FwingFragAdapter = UserGithubAdapter()
        followingRV?.size?.let { FwingFragAdapter.notifyItemChanged(it) }

        followingRV?.setHasFixedSize(true)
        followingRV?.layoutManager = GridLayoutManager(activity, 2)
        followingRV?.adapter = FwingFragAdapter.apply {
            setOnItemClickCallback(object : UserGithubAdapter.OnItemClickCallback {
                override fun setItemClicked(nameUser: String) {
                    val searchUserIntent = Intent(requireContext(), UserDetailActivity::class.java)
                    searchUserIntent.putExtra(UserDetailActivity.EXTRA_DETAIL_USER, nameUser)
                    startActivity(searchUserIntent)
                }
            })
        }

        showFollowingUserGithub(true)

        FwingVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        FwingVM.getFollowingUserGithub(nameUser)
        FwingVM.getReturnUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isEmpty()) {
                    showNull(true)

                    showFollowingUserGithub(false)
                } else {
                    FwingFragAdapter.listUserGithub(it)

                    showNull(false)
                    showFollowingUserGithub(false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        FwingFragBinding?.root?.requestLayout()
    }

    override fun onDestroy() {
        FwingFragBinding = null

        super.onDestroy()
    }

    private fun showNull(value: Boolean) {
        if (value) {
            FwingFragBinding?.apply {
                nullIcon.visibility = View.VISIBLE
                nullText.visibility = View.VISIBLE
            }
        } else {
            FwingFragBinding?.apply {
                nullIcon.visibility = View.INVISIBLE
                nullText.visibility = View.INVISIBLE
            }
        }
    }
}