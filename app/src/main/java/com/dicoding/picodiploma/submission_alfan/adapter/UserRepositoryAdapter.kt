package com.dicoding.picodiploma.submission_alfan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.submission_alfan.data.User
import com.dicoding.picodiploma.submission_alfan.databinding.ListRepositoryUserGithubBinding

class UserRepositoryAdapter :
    RecyclerView.Adapter<UserRepositoryAdapter.ListRepositoryViewHolder>() {
    private var dataUserGH = arrayListOf<User>()

    fun listRepositoryUserGithub(arrayUser: ArrayList<User>) {
        this.dataUserGH = arrayUser
        this.notifyDataSetChanged()
    }

    inner class ListRepositoryViewHolder(private var listRepositoryBinding: ListRepositoryUserGithubBinding) :
        RecyclerView.ViewHolder(listRepositoryBinding.root) {
        fun binding(user: User) {
            listRepositoryBinding.repositoryName.text = user.repositoryName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRepositoryViewHolder {
        val listRepositoryView = ListRepositoryUserGithubBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ListRepositoryViewHolder(listRepositoryView)
    }

    override fun onBindViewHolder(holder: ListRepositoryViewHolder, position: Int) =
        holder.binding(dataUserGH[position])

    override fun getItemCount(): Int = dataUserGH.size
}