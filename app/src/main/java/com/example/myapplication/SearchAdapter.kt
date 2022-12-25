package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Fragments.FriendListFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SearchAdapter(val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var x1: Context
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()
    private lateinit var storageReference: StorageReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        x1=parent.context
        return UserAdapter.UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
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
                databaseReference.child("Requests").child(firebaseAuth.currentUser!!.uid+currentUser.uid).setValue(SearchUser(firebaseAuth.currentUser!!.uid,currentUser.uid))
                    Toast.makeText(x1,"Friend Request Sent To User", Toast.LENGTH_SHORT).show()
        }
        holder.informationButton.setOnClickListener {
            val intent = Intent(x1, FriendProfileActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("email",currentUser.email)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("Check","1")

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