package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MySentRequestsActivity : AppCompatActivity() {
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var tempUserList : ArrayList<String?>
    private lateinit var adapter : MySentRequestsAdapter
    private lateinit var searchView: SearchView
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sent_requests)


        userList = ArrayList()
        tempUserList = ArrayList()

        userRecyclerView = findViewById(R.id.userRecyclerView3)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MySentRequestsAdapter(userList)
        userRecyclerView.adapter = adapter

        databaseReference.child("Requests").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempUserList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(SearchUser::class.java)

                    if(firebaseAuth.currentUser?.uid == currentUser?.from || firebaseAuth.currentUser?.uid == currentUser?.to){
                        if(firebaseAuth.currentUser?.uid == currentUser?.from){
                            tempUserList.add(currentUser?.to)
                        }
                        if(firebaseAuth.currentUser?.uid == currentUser?.to){
                            tempUserList.add(currentUser?.from)
                        }


                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        databaseReference.child("User").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(currentUser?.uid in tempUserList){
                        userList.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}