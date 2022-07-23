package com.dicoding.picodiploma.submission_alfan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.submission_alfan.data.User
import com.dicoding.picodiploma.submission_alfan.databinding.ListUserGithubBinding

class UserGithubAdapter : RecyclerView.Adapter<UserGithubAdapter.ListUserViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var dataUserGH = ArrayList<User>()

    interface OnItemClickCallback {
        fun setItemClicked(uNameUser: String)
    }

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallBack
    }

    fun listUserGithub(arrayUser: ArrayList<User>) {
        this.dataUserGH = arrayUser
        this.notifyDataSetChanged()
    }

    inner class ListUserViewHolder(private val listUserBinding: ListUserGithubBinding) :
        RecyclerView.ViewHolder(listUserBinding.root) {
        fun listBinding(user: User) {
            listUserBinding.name.text = user.username

            Glide.with(itemView)
                .load(user.image)
                .into(listUserBinding.userPicture)

            itemView.setOnClickListener {
                onItemClickCallback.setItemClicked(dataUserGH[adapterPosition].username)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUserViewHolder {
        val listUsers =
            ListUserGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListUserViewHolder(listUsers)
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.listBinding(dataUserGH[position])
    }

    override fun getItemCount(): Int = dataUserGH.size

}