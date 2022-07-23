package com.dicoding.picodiploma.submission_alfan.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission_alfan.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {
    private val FwingUserGH = MutableLiveData<ArrayList<User>>()

    fun getFollowingUserGithub(uNameUser: String) {
        val dataFollowingUser = ArrayList<User>()
        val connectAPI = AsyncHttpClient()
        val urlFollowingUserGithub = "https://api.github.com/users/$uNameUser/following"

        connectAPI.addHeader("Authorization", "token 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") //ganti token nya ambil di github, tutorial ada di submission
        connectAPI.addHeader("User-Agent", "request")

        connectAPI.get(urlFollowingUserGithub, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val resultFollowingUserGithub = String(responseBody)

                try {
                    val responseArrayFollowing = JSONArray(resultFollowingUserGithub)

                    for (f in 0 until responseArrayFollowing.length()) {
                        val followingUser = responseArrayFollowing.getJSONObject(f)
                        val userGithub = User()

                        val username = followingUser.getString("login")
                        val avatar = followingUser.getString("avatar_url")

                        userGithub.username = username
                        userGithub.image = avatar

                        dataFollowingUser.add(userGithub)
                    }
                    FwingUserGH.postValue(dataFollowingUser)
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
        return FwingUserGH
    }
}