package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var navcontroller : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //supportActionBar?.hide()



        nav_controller()
    }

    private fun nav_controller(){  //navigacia da appbaris titles shecvla
        bottomNavigationView = findViewById(R.id.profilebottomNavigationView)
        navcontroller =  findNavController(R.id.profileFragmentContainerView)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.id_friendListFragment2, R.id.id_chatListFragment2))
        setupActionBarWithNavController(navcontroller, appBarConfiguration )

        bottomNavigationView.setupWithNavController(navcontroller)
    }


}