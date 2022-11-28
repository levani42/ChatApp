package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ChangePassword : AppCompatActivity() {
    private lateinit var editTextTextPersonName: EditText
    private lateinit var changePasswordBtn: Button
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)
        changePasswordBtn = findViewById(R.id.changePasswordBtn)
        firebaseAuth = FirebaseAuth.getInstance()

        changePass()
    }

    private fun changePass(){
        changePasswordBtn.setOnClickListener {
            val newPass = editTextTextPersonName.text.toString()

            if(newPass.isEmpty() || newPass.length < 8){
                Toast.makeText(this,"Password too short, it has to be more than 8",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            firebaseAuth.currentUser?.updatePassword(newPass)?.addOnCompleteListener{ task->
                if(task.isSuccessful){ Toast.makeText(this,"Password Changed",Toast.LENGTH_SHORT).show()
                finish()
                }
                else{Toast.makeText(this,"Error, While Changing Password",Toast.LENGTH_SHORT).show()  }

            }



        }
    }

}