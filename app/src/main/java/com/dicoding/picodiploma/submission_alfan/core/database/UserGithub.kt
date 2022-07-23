package com.dicoding.picodiploma.submission_dicoding.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicoding.picodiploma.submission_alfan.data.User
import com.dicoding.picodiploma.submission_dicoding.core.database.GithubDB.FavDB.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserGithub(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String = "",

    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "avatar_url")
    val avatar_url: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "company")
    val company: String = "",

    @ColumnInfo(name = "followers")
    val followers: Int = 0,

    @ColumnInfo(name = "following")
    val following: Int = 0,

    @ColumnInfo(name = "repository")
    val repository: Int = 0,

    @ColumnInfo(name = "repository_name")
    val repositoryName: String = ""
)

fun List<UserGithub>.toUsers(): ArrayList<User> {
    val list = ArrayList<User>()

    forEach {
        list.add(User(id = it.id, username = it.username, image = it.avatar_url))
    }

    return list
}
