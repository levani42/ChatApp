package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Fragments.FriendListFragment
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var x1: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View  = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        x1=parent.context
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textname.text = currentUser.name

        holder.itemView.setOnClickListener {
            val intent = Intent(x1,ChatWithActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            x1.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.txt_name)

    }


}