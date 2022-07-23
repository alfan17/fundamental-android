package com.dicoding.picodiploma.submission_alfan.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.submission_alfan.data.User
import com.dicoding.picodiploma.submission_dicoding.core.database.GithubDB
import com.dicoding.picodiploma.submission_dicoding.core.database.UserGithub
import com.dicoding.picodiploma.submission_dicoding.core.database.UserGHDao
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserDetailViewModel(application: Application) : ViewModel() {
    private val detUserGH = MutableLiveData<ArrayList<User>>()

    private var daoFavUser: UserGHDao? = null

    private var FavUserDB: GithubDB.FavDB? = null

    init {
        FavUserDB = GithubDB.FavDB.getFavoriteInstance(application)
        daoFavUser = FavUserDB?.daoFavUser()
    }

    fun saveFavorite(username: String, id: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            daoFavUser?.addFavoriteUser(UserGithub(username, id, image))
        }
    }

    fun deleteFavorite(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            daoFavUser?.delete(UserGithub(username))
        }
    }

    fun checkFavorite(username: String) = daoFavUser?.checkFavoriteUser(username)

    fun getUserDetail(context: Context, uNameUser: String) {
        val dataDetailUser = ArrayList<User>()
        val connectAPI = AsyncHttpClient()
        val urlDetailUserGithub = "https://api.github.com/users/$uNameUser"

        connectAPI.addHeader("Authorization", "token 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")
        connectAPI.addHeader("User-Agent", "request")

        if (uNameUser.isNotEmpty()) {
            connectAPI.get(urlDetailUserGithub, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    val resultUserDetail = String(responseBody)

                    try {
                        val responseUserObject = JSONObject(resultUserDetail)
                        val userGithub = User()

                        fun JSONObject.getNullableString(name : String, fallback: String? = null) : String?{
                            return if (this.has(name) && !this.isNull(name)){
                                this.getString(name)
                            }else {
                                fallback
                            }
                        }

                        userGithub.image = responseUserObject.getString("avatar_url")
                        userGithub.name = responseUserObject.getNullableString("name", "user not set name")
                        userGithub.username = responseUserObject.getString("login")
                        userGithub.company = responseUserObject.getNullableString("company", "user not set company")
                        userGithub.location = responseUserObject.getNullableString("location", "user not set location")
                        userGithub.id = responseUserObject.getString("id")
                        userGithub.followers = responseUserObject.getInt("followers")
                        userGithub.following = responseUserObject.getInt("following")
                        userGithub.repository = responseUserObject.getInt("public_repos")

                        dataDetailUser.add(userGithub)
                        detUserGH.postValue(dataDetailUser)
                    } catch (e: Exception) {
                        Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Failed!! Read : ${e.message}")
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    val errorMessage = when (statusCode) {
                        400 -> "Bad Request! Code : $statusCode"
                        401 -> "Bad Request! Code : $statusCode"
                        403 -> "Bad Request! Code : $statusCode"
                        404 -> "Bad Request! Code : $statusCode"

                        else -> error?.message + "! Code : $statusCode"
                    }

                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    fun getReturnUserDetail(): MutableLiveData<ArrayList<User>> {
        return detUserGH
    }
}

class UserDetailViewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserDetailViewModel(application) as T
    }
}