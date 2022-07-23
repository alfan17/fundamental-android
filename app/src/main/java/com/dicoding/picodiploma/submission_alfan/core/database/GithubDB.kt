package com.dicoding.picodiploma.submission_dicoding.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodiploma.submission_dicoding.core.database.GithubDB.FavDB.Companion.TABLE_VERSION

class GithubDB {

    @Database(entities = [UserGithub::class], version = TABLE_VERSION, exportSchema = false)
    abstract class FavDB : RoomDatabase() {

        abstract fun daoFavUser(): UserGHDao

        companion object {
            const val TABLE_NAME = "user"
            const val TABLE_VERSION = 1

            @Volatile
            var favoriteInstance: FavDB? = null

            fun getFavoriteInstance(context: Context): FavDB? {
                if (favoriteInstance == null) {
                    synchronized(FavDB::class) {
                        favoriteInstance = Room.databaseBuilder(
                            context.applicationContext,
                            FavDB::class.java,
                            TABLE_NAME
                        ).allowMainThreadQueries().build()
                    }
                }
                return favoriteInstance
            }
        }
    }
}