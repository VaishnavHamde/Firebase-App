package com.example.firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityAddUserBinding
import com.example.firebase.databinding.UsersItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class UsersAdapter(var context : Context,
                   var usersList : ArrayList<Users>) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(){

    inner class UsersViewHolder(val adapterBinding : UsersItemBinding) : RecyclerView.ViewHolder(adapterBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding = UsersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.adapterBinding.textViewName.text = usersList[position].userName
        holder.adapterBinding.textViewAge.text = usersList[position].userAge.toString()
        holder.adapterBinding.textViewEmail.text = usersList[position].userEmail

        val imageUrl = usersList[position].url

        Picasso.get().load(imageUrl).into(holder.adapterBinding.imageView, object : Callback{
            override fun onSuccess() {
                holder.adapterBinding.progressBar.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
                Toast.makeText(context, e?.localizedMessage, Toast.LENGTH_LONG).show()
            }

        })

        holder.adapterBinding.linearLayout.setOnClickListener{
            val intent = Intent(context, UpdateUserActivity::class.java)
            intent.putExtra("id",usersList[position].userId)
            intent.putExtra("name", usersList[position].userName)
            intent.putExtra("age", usersList[position].userAge)
            intent.putExtra("email", usersList[position].userEmail)
            intent.putExtra("imageUrl", imageUrl)
            intent.putExtra("imageName", usersList[position].imageName)
            context.startActivity(intent)
        }
    }

    fun getUserId(position : Int) : String{
        return usersList[position].userId
    }

    fun getImageName(position: Int) : String{
        return usersList[position].imageName
    }

}