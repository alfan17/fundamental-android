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
import com.dicoding.picodiploma.submission_alfan.databinding.FragmentFollowersBinding
import com.dicoding.picodiploma.submission_alfan.ui.activity.UserDetailActivity
import com.dicoding.picodiploma.submission_alfan.viewmodel.FollowersViewModel

class FollowersFragment : Fragment(R.layout.fragment_followers) {
    private var FwersFragBinding: FragmentFollowersBinding? = null
    private lateinit var FwersFragAdapter: UserGithubAdapter
    private lateinit var FwersVM: FollowersViewModel
    private lateinit var nameUser: String

    private fun showFollowersUserGithub(followersValue: Boolean) {
        if (followersValue) {
            FwersFragBinding?.followersProgressBar?.visibility = View.VISIBLE
        } else {
            FwersFragBinding?.followersProgressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameUser = arguments?.getString(UserDetailActivity.EXTRA_DETAIL_USER).toString()

        FwersFragBinding = FragmentFollowersBinding.bind(view)


        val followersRV = FwersFragBinding?.followersRv
        FwersFragAdapter = UserGithubAdapter()
        followersRV?.let { FwersFragAdapter.notifyItemChanged(it.size) }
        FwersFragAdapter.notifyItemInserted(0)
        followersRV?.scrollToPosition(0)
        followersRV?.setHasFixedSize(true)
        followersRV?.layoutManager = GridLayoutManager(activity, 2)
        followersRV?.adapter = FwersFragAdapter.apply {
            setOnItemClickCallback(object : UserGithubAdapter.OnItemClickCallback {
                override fun setItemClicked(nameUser: String) {
                    val searchUserIntent = Intent(requireContext(), UserDetailActivity::class.java)
                    searchUserIntent.putExtra(UserDetailActivity.EXTRA_DETAIL_USER, nameUser)
                    startActivity(searchUserIntent)
                }
            })
        }

        FwersVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        FwersVM.getFollowersUserGithub(nameUser)
        FwersVM.getReturnUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isEmpty()) {
                    showNull(true)
                    showFollowersUserGithub(false)
                } else {
                    FwersFragAdapter.listUserGithub(it)

                    showNull(false)
                    showFollowersUserGithub(false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        FwersFragBinding?.root?.requestLayout()
    }

    override fun onDestroy() {
        FwersFragBinding = null

        super.onDestroy()
    }

    private fun showNull(value: Boolean) {
        if (value) {
            FwersFragBinding?.apply {
                nullIcon.visibility = View.VISIBLE
                nullText.visibility = View.VISIBLE
            }
        } else {
            FwersFragBinding?.apply {
                nullIcon.visibility = View.INVISIBLE
                nullText.visibility = View.INVISIBLE
            }
        }
    }
}