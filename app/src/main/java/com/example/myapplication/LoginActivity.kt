package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var loginEmailEditText : EditText
    lateinit var loginPasswordEditText : EditText
    lateinit var loginButton : Button
    lateinit var loginRegistrationButton : Button
    lateinit var loginForgotPasswordButton : Button
    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        init()
    }



    private fun init() {
        loginEmailEditText = findViewById(R.id.LoginEmailEditText)
        loginPasswordEditText = findViewById(R.id.LoginPasswordEditText)
        loginButton = findViewById(R.id.LoginButton)
        loginRegistrationButton = findViewById(R.id.LoginRegistrationButton)
        loginForgotPasswordButton = findViewById(R.id.LoginForgotPasswordButton)

        login()
    }

    private fun login(){
        loginButton.setOnClickListener {
            val email:String = loginEmailEditText.text.toString()
            val password:String = loginPasswordEditText.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Email or Password is empty, Please try again!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener() {
                task ->
                if(task.isSuccessful){
                    val intent = Intent(this,ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{Toast.makeText(this,"User doesn't exist",Toast.LENGTH_SHORT).show()}
            }
        }

        loginRegistrationButton.setOnClickListener {  // გადადის registration აქთივითიზე
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }


        loginForgotPasswordButton.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)  //  გადადის forgot password აქთივითიზე
            startActivity(intent)

        }


    }
}