package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var forgotPasswordEmailEditText : EditText
    lateinit var forgotPasswordSend : Button
    var firebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        init()
    }

    private fun init(){
        forgotPasswordEmailEditText = findViewById(R.id.forgotPasswordEmailEditText)
        forgotPasswordSend = findViewById(R.id.forgotPasswordSend)
        supportActionBar?.hide()
        resetPassword()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun resetPassword(){
        forgotPasswordSend.setOnClickListener {
        var email = forgotPasswordEmailEditText.text.toString()
            if(email.isEmpty()){
                Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(this, "Error has occurred", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

}