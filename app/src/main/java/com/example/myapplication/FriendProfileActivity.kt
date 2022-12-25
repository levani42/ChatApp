package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FriendProfileActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameView: TextView
    private lateinit var emailView: TextView
    private lateinit var unfriend: Button
    private lateinit var storageReference: StorageReference
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_profile)


        imageView=findViewById(R.id.imageView)
        nameView=findViewById(R.id.textView2)
        emailView=findViewById(R.id.textView4)
        unfriend=findViewById(R.id.unfriendButton)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val receiverUid = intent.getStringExtra("uid")
        val check = intent.getStringExtra("Check").toString()

        if(check == "1"){
            unfriend.visibility = View.GONE
        }

        nameView.text = name.toString()
        emailView.text = email.toString()

        unfriend.setOnClickListener{
            databaseReference.child("FriendList").child(firebaseAuth.currentUser?.uid+receiverUid).removeValue()
            databaseReference.child("FriendList").child(receiverUid+firebaseAuth.currentUser?.uid).removeValue()

            super.onBackPressed()
        }
        storageReference = FirebaseStorage.getInstance().reference.child("User/$receiverUid")
        storageReference.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()

            Glide.with(this)
                .load(imageURL)
                .into(imageView)

        }

    }
}