package com.dicoding.picodiploma.submission_alfan.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.submission_alfan.R
import com.dicoding.picodiploma.submission_alfan.adapter.SectionPageAdapter
import com.dicoding.picodiploma.submission_alfan.databinding.ActivityUserDetailBinding
import com.dicoding.picodiploma.submission_alfan.viewmodel.UserDetailViewModel
import com.dicoding.picodiploma.submission_alfan.viewmodel.UserDetailViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var detBinding: ActivityUserDetailBinding
    private lateinit var detUserVM: UserDetailViewModel
    private lateinit var nameUser: String

    companion object {
        var EXTRA_DETAIL_USER = "extra_detail_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(detBinding.root)

        detUserVM = ViewModelProvider(
            this,
            UserDetailViewModelFactory(application)
        )[UserDetailViewModel::class.java]

        nameUser = intent.getStringExtra(EXTRA_DETAIL_USER).toString()

        val backMain = detBinding.backDetail
        backMain.setOnClickListener {
            super.onBackPressed()
        }

        val shareMain = detBinding.shareDetail
        shareMain.setOnClickListener {
            val shareItem: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, nameUser)
                type = "text/plain"
            }

            val shareMainIntent = Intent.createChooser(shareItem, null)
            startActivity(shareMainIntent)
        }

        userGithubDetail()
    }

    private fun showProgressBar(progressValue: Boolean) {
        if (progressValue) {
            detBinding.progressBarDetail.visibility = View.VISIBLE
        } else {
            detBinding.progressBarDetail.visibility = View.INVISIBLE
        }
    }

    private fun userGithubDetail() {
        detUserVM.getUserDetail(this@UserDetailActivity, nameUser)
        detUserVM.getReturnUserDetail().observe(this) {
            if (it != null) {
                showProgressBar(false)
                detBinding.nameDetail.text = it[0].name
                detBinding.usernameDetail.text = it[0].username
                detBinding.companyDetail.text =
                    it[0].company ?: print("User not insert data").toString()
                detBinding.locationDetail.text = it[0].location

                detUserVM.checkFavorite(nameUser)?.observe(this) { count ->
                    if (count != 0) {
                        detBinding.favoriteButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_rate_24, null))
                        detBinding.favoriteButton.setOnClickListener {
                            detUserVM.deleteFavorite(nameUser)
                        }
                    } else {
                        detBinding.favoriteButton.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_star_border_24, null))
                        detBinding.favoriteButton.setOnClickListener { _ ->
                            detUserVM.saveFavorite(it[0].username, it[0].id, it[0].image)
                        }
                    }
                }

                Glide.with(this)
                    .load(it[0].image)
                    .into(detBinding.imageUser)

                val followerText = resources.getString(R.string.followers)
                val followingText = resources.getString(R.string.following)
                val repositoryText = resources.getString(R.string.repository)

                val tabLayout = detBinding.detailTab
                val viewPager = detBinding.detailViewPager
                val intentUserGithub = intent.getStringExtra(EXTRA_DETAIL_USER)
                val detailBundle = Bundle()
                detailBundle.putString(EXTRA_DETAIL_USER, intentUserGithub)

                val tabAdapter = SectionPageAdapter(this, detailBundle)

                viewPager.adapter = tabAdapter

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    when (position) {
                        0 -> {
                            tab.text = StringBuilder("${it[0].followers}\n$followerText")
                        }
                        1 -> {
                            tab.text = StringBuilder("${it[0].following}\n$followingText")
                        }
                        2 -> {
                            tab.text = StringBuilder("${it[0].repository}\n$repositoryText")
                        }
                    }
                }.attach()
            }
            else {
                showProgressBar(true)
            }
        }
    }
}