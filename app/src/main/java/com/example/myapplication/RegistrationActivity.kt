package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegistrationActivity : AppCompatActivity() {
    lateinit var imgUri: Uri
    private lateinit var registrationImageView: ImageView
    lateinit var registrationEmailEditText : EditText
    lateinit var registrationPasswordEditText : EditText
    lateinit var registrationUsernameEditText : EditText
    lateinit var  registrationButton : Button
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()
    lateinit private var storageReference : StorageReference
    var req_code = 1
    lateinit var hz : Uri
    var da = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar?.hide()
        init()
    }

    private fun init(){

        registrationEmailEditText = findViewById(R.id.registrationEmailEditText)
        registrationPasswordEditText = findViewById(R.id.registrationPasswordEditText)
        registrationButton = findViewById(R.id.registrationButton)
        registrationUsernameEditText = findViewById(R.id.registrationUsernameEditText)
        registrationImageView = findViewById(R.id.registrationProfileImageView)


        signup()

    }

    private fun signup(){
        registrationImageView.setOnClickListener {
            addUserProfilePicture()
        }
        registrationButton.setOnClickListener {

            val name:String = registrationUsernameEditText.text.toString()
            val email:String = registrationEmailEditText.text.toString()
            val password:String = registrationPasswordEditText.text.toString()


            if(email.isEmpty() || password.length < 8){
                Toast.makeText(this, "Password too short, it has to be more than 8", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task->
                if(task.isSuccessful){
                    addUserToDatabase(name,email,firebaseAuth.currentUser?.uid!!) // currend users vamatebs real timeshi
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Error has occurred while creating user",Toast.LENGTH_SHORT).show()
                }
            }



        }
    }

    private fun addUserProfilePicture(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, req_code)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == req_code && resultCode == RESULT_OK) {
            registrationImageView.setImageURI(data?.data)
            hz = data?.data!!
            da = 1
        }
    }
    private fun addUserToDatabase(name: String, email: String, uid: String){
        databaseReference.child("User").child(uid).setValue(User(name,email,uid)) //real time user added
        if(da==0){  // default surati
        imgUri= Uri.parse("android.resource://$packageName/${R.drawable.defpic1}")}
        else{imgUri=hz}  // uploaded surati
        storageReference = FirebaseStorage.getInstance().getReference("User/"+uid)
        storageReference.putFile(imgUri).addOnCompleteListener(){task->
            if(task.isSuccessful){}
            else{Toast.makeText(this,"Error has occurred while uploading picture",Toast.LENGTH_SHORT).show()}

        }

    }

}