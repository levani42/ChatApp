package com.example.myapplication.Fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myapplication.ChangePassword
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import kotlin.math.log

class ChatListFragment : Fragment() {

    private lateinit  var changePasswordUP : Button
    private lateinit  var profileLgobutton : Button
    private var  firebaseAuth =  FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()
    private lateinit var storageReference: StorageReference
    private lateinit var textView2 : TextView
    private lateinit var textView4 : TextView
    private lateinit  var uid : String
    private lateinit  var userN : String
    private lateinit var imageView : ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileLgobutton = view.findViewById(R.id.profileLgobutton)
        changePasswordUP = view.findViewById(R.id.changePasswordUP)
        textView2 = view.findViewById(R.id.textView2)
        textView4 = view.findViewById(R.id.textView4)
        imageView = view.findViewById(R.id.imageView)

        init()

    }
    private fun init(){

        getData()

        changePasswordUP.setOnClickListener { //change password
            val intent = Intent(activity, ChangePassword::class.java)
            startActivity(intent)
        }
        profileLgobutton.setOnClickListener{// log out

            firebaseAuth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            }

    }
    private fun getData(){
        uid = firebaseAuth.currentUser?.uid.toString()
        textView4.text = firebaseAuth.currentUser?.email.toString()
        getProfileData()

        storageReference = FirebaseStorage.getInstance().reference.child("User/$uid")
        val localFile = File.createTempFile("tempImage","png")
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageView.setImageBitmap(bitmap)

        }.addOnFailureListener{
            Toast.makeText(activity,"Error, While Loading Profile Picture",Toast.LENGTH_SHORT).show()
        }

    }
    private fun getProfileData(){
        databaseReference.child("User").addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children) {
                    val currentUser = i.getValue(User::class.java)
                    if(firebaseAuth.currentUser?.uid == currentUser?.uid){
                        textView2.text = currentUser?.name.toString()
                        break
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error, While Getting Name",Toast.LENGTH_SHORT).show()
            }


        })

    }


}