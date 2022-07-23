package com.dicoding.picodiploma.submission_alfan.data

data class User(
    var name: String? = "-",
    var username: String = "-",
    var location: String? = "-",
    var company: String? = "-",
    var image: String = "-",
    var followers: Int? = 0,
    var following: Int? = 0,
    var repository: Int = 0,
    var id: String = "-",
    var repositoryName: String? = "-"
)