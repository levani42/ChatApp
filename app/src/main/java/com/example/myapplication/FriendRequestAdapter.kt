package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Fragments.FriendListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FriendRequestAdapter(val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var x1: Context
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()
    private lateinit var storageReference: StorageReference
    private lateinit var addFriendButton: FloatingActionButton
    private lateinit var declineFriendButton: FloatingActionButton

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_layout2, parent, false)
        x1=parent.context
        addFriendButton=view.findViewById(R.id.addFriendButton)
        declineFriendButton=view.findViewById(R.id.declineFriendButton)
        return UserAdapter.UserViewHolder(view)

    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {

        val currentUser = userList[position]
        holder.textname.text = currentUser.name
        var cr_userUid = currentUser.uid
        var id=cr_userUid+firebaseAuth.currentUser!!.uid
        var uid_var = firebaseAuth.currentUser!!.uid
        addFriendButton.setOnClickListener{
            //databaseReference.child("FriendList1").child("hi").setValue(id)
            databaseReference.child("FriendList").child(firebaseAuth.currentUser!!.uid+currentUser.uid).setValue(FriendListUser(firebaseAuth.currentUser!!.uid,currentUser.uid))

            databaseReference.child("Requests").child(id).removeValue()
            userList.removeAt(position)
            notifyDataSetChanged()
        }
        declineFriendButton.setOnClickListener{
            databaseReference.child("Requests").child(id).removeValue()
            userList.removeAt(position)
            notifyDataSetChanged()
        }
        storageReference = FirebaseStorage.getInstance().reference.child("User/$cr_userUid")

        storageReference.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()

            Glide.with(x1)
                .load(imageURL)
                .into(holder.image_recycler)

        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textname = itemView.findViewById<TextView>(R.id.txt_name)

    }


}