package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MySentRequestsAdapter(val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var x1: Context
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()
    private lateinit var storageReference: StorageReference
    private lateinit var declineFriendButton: FloatingActionButton

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_layout3, parent, false)
        declineFriendButton=view.findViewById(R.id.declineFriendButton)
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
        declineFriendButton.setOnClickListener{
            databaseReference.child("Requests").child(firebaseAuth.currentUser?.uid+cr_userUid).removeValue()
            databaseReference.child("Requests").child(cr_userUid+firebaseAuth.currentUser?.uid).removeValue()

            Toast.makeText(x1,"Sent Request Removed!!!",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}