package com.dicoding.picodiploma.submission_alfan.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.picodiploma.submission_alfan.R
import com.dicoding.picodiploma.submission_alfan.adapter.UserRepositoryAdapter
import com.dicoding.picodiploma.submission_alfan.databinding.FragmentRepositoryBinding
import com.dicoding.picodiploma.submission_alfan.ui.activity.UserDetailActivity
import com.dicoding.picodiploma.submission_alfan.viewmodel.RepositoryViewModel

class RepositoryFragment : Fragment(R.layout.fragment_repository) {
    private var RepoFragBinding: FragmentRepositoryBinding? = null
    private lateinit var RepoFragAdapter: UserRepositoryAdapter
    private lateinit var repositoryVM: RepositoryViewModel
    private lateinit var nameUser: String

    private fun showRepositoryUserGithub(repositoryValue: Boolean) {
        if (repositoryValue) {
            RepoFragBinding?.repositoryProgressBar?.visibility = View.VISIBLE
        } else {
            RepoFragBinding?.repositoryProgressBar?.visibility = View.INVISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameUser = arguments?.getString(UserDetailActivity.EXTRA_DETAIL_USER).toString()

        RepoFragBinding = FragmentRepositoryBinding.bind(view)

        val repositoryRV = RepoFragBinding?.repositoryRv
        RepoFragAdapter = UserRepositoryAdapter()
        repositoryRV?.size?.let { RepoFragAdapter.notifyItemChanged(it) }

        repositoryRV?.setHasFixedSize(true)
        repositoryRV?.layoutManager = GridLayoutManager(activity, 2)
        repositoryRV?.adapter = RepoFragAdapter

        showRepositoryUserGithub(true)

        repositoryVM = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(RepositoryViewModel::class.java)
        repositoryVM.getRepositoryUserGithub(nameUser)
        repositoryVM.getReturnUserFollowers().observe(viewLifecycleOwner, {
            if (it != null) {
                if (it.isEmpty()) {
                    showNull(true)

                    showRepositoryUserGithub(false)
                } else {
                    RepoFragAdapter.listRepositoryUserGithub(it)

                    showNull(false)
                    showRepositoryUserGithub(false)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        RepoFragBinding?.root?.requestLayout()
    }

    override fun onDestroy() {
        RepoFragBinding = null

        super.onDestroy()
    }

    private fun showNull(value: Boolean) {
        if (value) {
            RepoFragBinding?.apply {
                nullIcon.visibility = View.VISIBLE
                nullText.visibility = View.VISIBLE
            }
        } else {
            RepoFragBinding?.apply {
                nullIcon.visibility = View.INVISIBLE
                nullText.visibility = View.INVISIBLE
            }
        }
    }

}