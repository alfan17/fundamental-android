package com.dicoding.picodiploma.submission_alfan.ui.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.submission_alfan.adapter.UserGithubAdapter
import com.dicoding.picodiploma.submission_alfan.databinding.ActivityMainBinding
import com.dicoding.picodiploma.submission_alfan.viewmodel.UserSearchViewModel
import com.dicoding.picodiploma.submission_dicoding.core.DataStoreThemeUI
import com.dicoding.picodiploma.submission_dicoding.core.UIMode
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var listUserGH: RecyclerView
    private lateinit var userGH: UserSearchViewModel
    private lateinit var searchUserAdapter: UserGithubAdapter
    private var doubleClickedValue: Boolean = false

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting_theme_ui.pref")

    private lateinit var settingTheme: DataStoreThemeUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        settingTheme = DataStoreThemeUI.getInstance(dataStore)

        showTitleAndSearchButton(true)

        listUserGH = mainBinding.rvUserGithub
        listUserGH.setHasFixedSize(true)

        val dialogPilihMode = AlertDialog.Builder(this)
        dialogPilihMode.setTitle("Pilih Mode")
        val types = arrayOf("Dark Mode", "Light Mode")
        dialogPilihMode.setItems(types) { dialog, which ->
            dialog.dismiss()
            when (which) {
                0 -> {
                    lifecycleScope.launch {
                        settingTheme.setThemeMode(UIMode.DARK)
                    }
                }
                1 -> {
                    lifecycleScope.launch {
                        settingTheme.setThemeMode(UIMode.LIGHT)
                    }
                }
            }
        }

        settingTheme.uiModeFlow.asLiveData().observe(this) {
            setCheckedMode(it)
        }

        mainBinding.actionSetting.setOnClickListener {
            dialogPilihMode.show()
        }

        listUserGH.layoutManager = GridLayoutManager(this, 2)
        searchUserAdapter = UserGithubAdapter()
        listUserGH.adapter = searchUserAdapter
        searchUserAdapter.notifyItemChanged(listUserGH.size)
        searchUserAdapter.setOnItemClickCallback(object : UserGithubAdapter.OnItemClickCallback {
            override fun setItemClicked(uNameUser: String) {
                val searchUserIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                searchUserIntent.putExtra(UserDetailActivity.EXTRA_DETAIL_USER, uNameUser)
                startActivity(searchUserIntent)
            }
        })

        userGH = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserSearchViewModel::class.java)

        userGH.getReturnUserGithub().observe(this, {
            if (it != null) {
                if (it.isEmpty()) {
                    showProgressBar(false)
                    showUnknown(true)
                    showEditText(true)
                    showTitleAndSearchButton(false)
                    // ini adapternya.
                    searchUserAdapter.listUserGithub(arrayListOf())
                } else {
                    showProgressBar(false)
                    showUnknown(false)
                    showEditText(false)
                    showTitleAndSearchButton(true)
                    // ini adapternya.
                    searchUserAdapter.listUserGithub(it)
                }
            }
        })


        val searchingButton = mainBinding.searchButton
        searchingButton.setOnClickListener {
            showTitleAndSearchButton(false)
        }

        mainBinding.favorite.setOnClickListener{
            val intent = Intent(this, FavoriteActivity::class.java)

            startActivity(intent)
        }

    }

    private fun setCheckedMode(uiMode: UIMode) {
        when (uiMode) {
            UIMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            UIMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun showProgressBar(progressValue: Boolean) {
        if (progressValue) {
            mainBinding.searchProgressBar.visibility = View.VISIBLE
        } else {
            mainBinding.searchProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun showUnknown(unknownValue: Boolean) {
        if (unknownValue) {
            mainBinding.unknownImage.visibility = View.VISIBLE
            mainBinding.unknownText.visibility = View.VISIBLE
        } else {
            mainBinding.unknownImage.visibility = View.INVISIBLE
            mainBinding.unknownText.visibility = View.INVISIBLE
        }
    }

    private fun showTitleAndSearchButton(visibleValue: Boolean) {
        if (visibleValue) {
            mainBinding.searchButton.visibility = View.VISIBLE
            mainBinding.titleMainHeader.visibility = View.VISIBLE

            showEditText(false)
        } else {
            mainBinding.searchButton.visibility = View.INVISIBLE
            mainBinding.titleMainHeader.visibility = View.INVISIBLE

            showEditText(true)
        }
    }

    private fun showEditText(editTextValue: Boolean) {
        if (editTextValue) {
            val searchUserManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            mainBinding.searchEditText.visibility = View.VISIBLE
            mainBinding.searchEditText.setOnQueryTextListener(this)
            mainBinding.searchEditText.setSearchableInfo(
                searchUserManager.getSearchableInfo(
                    componentName
                )
            )
        } else {
            mainBinding.searchEditText.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        if (doubleClickedValue) {

            super.onBackPressed()
            return
        }
        this.doubleClickedValue = true
        showTitleAndSearchButton(true)
        Toast.makeText(this, "Press back again to exit application", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleClickedValue = false }, 2000)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        userGH.getUserGithub(this, query)

        if (query.isEmpty())
            searchUserAdapter.listUserGithub(arrayListOf())

        showProgressBar(true)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

}