package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Fragments.FriendListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.makeramen.roundedimageview.RoundedImageView
import java.io.File

class UserAdapter(val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var x1: Context
    private lateinit var storageReference: StorageReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View  = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        val image_recycler:View  = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        x1=parent.context
        return UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textname.text = currentUser.name
        var cr_userUid = currentUser.uid
        storageReference = FirebaseStorage.getInstance().reference.child("User/$cr_userUid")

        storageReference.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()

            Glide.with(x1)
                .load(imageURL)
                .into(holder.image_recycler)

        }

        holder.itemView.setOnClickListener {
            val intent = Intent(x1,ChatWithActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            x1.startActivity(intent)
        }
        holder.informationButton.setOnClickListener {
            val intent = Intent(x1, FriendProfileActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("email",currentUser.email)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("Check","0")

            x1.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.txt_name)
        val image_recycler = itemView.findViewById<RoundedImageView>(R.id.image_recycler)
        val informationButton = itemView.findViewById<FloatingActionButton>(R.id.informationButton)

    }


}