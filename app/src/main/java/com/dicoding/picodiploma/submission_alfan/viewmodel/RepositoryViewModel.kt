package com.dicoding.picodiploma.submission_alfan.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission_alfan.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class RepositoryViewModel : ViewModel() {
    private val RepoUserGH = MutableLiveData<ArrayList<User>>()

    fun getRepositoryUserGithub(uNameUser: String) {
        val dataRepositoryUser = ArrayList<User>()
        val connectAPI = AsyncHttpClient()
        val urlRepositoryUserGithub = "https://api.github.com/users/$uNameUser/repos"

        connectAPI.addHeader("Authorization", "token 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") //ganti token nya ambil di github, tutorial ada di submission
        connectAPI.addHeader("User-Agent", "request")

        connectAPI.get(urlRepositoryUserGithub, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val resultFollowingUserGithub = String(responseBody)

                try {
                    val responseArrayRepository = JSONArray(resultFollowingUserGithub)

                    for (r in 0 until responseArrayRepository.length()) {
                        val repositoryUser = responseArrayRepository.getJSONObject(r)
                        val userGithub = User()

                        val repositoryName = repositoryUser.getString("name")

                        userGithub.repositoryName = repositoryName

                        dataRepositoryUser.add(userGithub)
                    }
                    RepoUserGH.postValue(dataRepositoryUser)
                } catch (e: Exception) {
                    Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Error!! Read : ${e.message}")
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
                Log.d(UserSearchViewModel.EXTRA_USER_DATA, "Error!! Read : $errorMessage")
            }

        })
    }

    fun getReturnUserFollowers(): MutableLiveData<ArrayList<User>> {
        return RepoUserGH
    }
}