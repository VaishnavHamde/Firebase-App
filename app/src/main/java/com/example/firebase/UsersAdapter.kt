package com.example.firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityAddUserBinding
import com.example.firebase.databinding.UsersItemBinding

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

        holder.adapterBinding.linearLayout.setOnClickListener{
            val intent = Intent(context, UpdateUserActivity::class.java)
            intent.putExtra("id",usersList[position].userId)
            intent.putExtra("name", usersList[position].userName)
            intent.putExtra("age", usersList[position].userAge)
            intent.putExtra("email", usersList[position].userEmail)
            context.startActivity(intent)
        }
    }

    fun getUserId(position : Int) : String{
        return usersList[position].userId
    }

}