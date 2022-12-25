package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AddFriendActivity : AppCompatActivity() {
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var tempArrayList : ArrayList<User>
    private lateinit var adapter : SearchAdapter
    private lateinit var searchView: SearchView
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        supportActionBar?.hide()
        userList = ArrayList()
        tempArrayList = ArrayList()

        searchView = findViewById(R.id.search_bar)
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SearchAdapter(tempArrayList)
        userRecyclerView.adapter = adapter

        databaseReference.child("User").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                tempArrayList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(firebaseAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                        tempArrayList.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    userList.forEach{
                            if(it.name!!.toLowerCase(Locale.getDefault()).contains(searchText)){
                                tempArrayList.add(it)
                            }
                    }
                    userRecyclerView.adapter!!.notifyDataSetChanged()

                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(userList)
                    userRecyclerView.adapter!!.notifyDataSetChanged()
                }



                return false
            }

        })
        return true
    }

    }


