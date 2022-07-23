package com.dicoding.picodiploma.submission_alfan.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_dicoding.core.database.GithubDB
import com.dicoding.picodiploma.submission_dicoding.core.database.UserGHDao

class FavoriteViewModel(application: Application): ViewModel() {
    private var daoFavoriteUser: UserGHDao? = null

    private var DBFavoriteUser: GithubDB.FavDB? = null

    init {
        DBFavoriteUser = GithubDB.FavDB.getFavoriteInstance(application)
        daoFavoriteUser = DBFavoriteUser?.daoFavUser()
    }

    fun getList() = daoFavoriteUser?.getFavoriteUser()

}

class FavoriteViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(application) as T
    }
}