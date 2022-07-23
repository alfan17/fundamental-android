package com.dicoding.picodiploma.submission_alfan.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.submission_alfan.data.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserSearchViewModel : ViewModel() {
    private val listUser = MutableLiveData<ArrayList<User>>()

    companion object {
        val EXTRA_USER_DATA: String = (UserSearchViewModel::class.java).simpleName
    }

    fun getUserGithub(context: Context, uNameUser: String) {
        val listUserGithub = ArrayList<User>()
        val connectAPI = AsyncHttpClient()
        val urlSearchUserGithub = "https://api.github.com/search/users?q=$uNameUser"

        connectAPI.addHeader("Authorization", "token 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")
        connectAPI.addHeader("User-Agent", "request")
        Log.d(EXTRA_USER_DATA, "getUser: $urlSearchUserGithub")

        if (urlSearchUserGithub.isNotEmpty()) {
            connectAPI.get(urlSearchUserGithub, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
                ) {
                    val resultUserSearch = String(responseBody)

                    try {
                        val responseUserObject = JSONObject(resultUserSearch)
                        val userItems = responseUserObject.getJSONArray("items")

                        for (i in 0 until userItems.length()) {
                            val item = userItems.getJSONObject(i)
                            val dataUserGithub = User()

                            val usernameUser = item.getString("login")
                            val avatarUser = item.getString("avatar_url")

                            dataUserGithub.username = usernameUser
                            dataUserGithub.image = avatarUser

                            listUserGithub.add(dataUserGithub)
                        }
                        listUser.postValue(listUserGithub)
                    } catch (e: Exception) {
                        Log.d(EXTRA_USER_DATA, "Failed!! Read : ${e.message}")
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
        } else {
            listUser.postValue(arrayListOf())
        }
    }

    fun getReturnUserGithub(): MutableLiveData<ArrayList<User>> {
        return listUser
    }
}