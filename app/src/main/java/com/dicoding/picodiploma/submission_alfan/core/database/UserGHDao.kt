package com.dicoding.picodiploma.submission_dicoding.core.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.picodiploma.submission_dicoding.core.database.GithubDB.FavDB.Companion.TABLE_NAME

@Dao
interface UserGHDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteUser(userGithub: UserGithub)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getFavoriteUser(): LiveData<List<UserGithub>>

    @Query("SELECT COUNT(*) FROM $TABLE_NAME WHERE $TABLE_NAME.username = :username")
    fun checkFavoriteUser(username: String): LiveData<Int>

    @Delete
    fun delete(userGithub: UserGithub)
}