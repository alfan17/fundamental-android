package com.dicoding.picodiploma.submission_alfan.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_alfan.adapter.UserGithubAdapter
import com.dicoding.picodiploma.submission_alfan.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.submission_alfan.viewmodel.FavoriteViewModel
import com.dicoding.picodiploma.submission_alfan.viewmodel.FavoriteViewModelFactory
import com.dicoding.picodiploma.submission_dicoding.core.database.toUsers

class FavoriteActivity: AppCompatActivity() {
    private lateinit var FavBinding: ActivityFavoriteBinding

    private lateinit var FavVM: FavoriteViewModel

    private lateinit var userGhAdapter: UserGithubAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(FavBinding.root)

        FavVM = ViewModelProvider(
            this,
            FavoriteViewModelFactory(application)
        )[FavoriteViewModel::class.java]

        userGhAdapter = UserGithubAdapter().apply {
            setOnItemClickCallback(object : UserGithubAdapter.OnItemClickCallback {
                override fun setItemClicked(uNameUser: String) {
                    val searchUserIntent = Intent(applicationContext, UserDetailActivity::class.java)
                    searchUserIntent.putExtra(UserDetailActivity.EXTRA_DETAIL_USER, uNameUser)
                    startActivity(searchUserIntent)
                }
            })
        }

        FavBinding.recyclerview.adapter = userGhAdapter

        FavVM.getList()?.observe(this) {
            userGhAdapter.listUserGithub(it.toUsers())
        }
    }
}