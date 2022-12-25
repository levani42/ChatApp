package com.example.myapplication.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.myapplication.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RequestListFragment : Fragment() {

    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var requestList:ArrayList<String?>
    private lateinit var adapter : FriendRequestAdapter
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var addFriendButton:FloatingActionButton

    private var databaseReference = FirebaseDatabase.getInstance().getReference()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userList = ArrayList()
        requestList=ArrayList()



        addFriendButton=view.findViewById(R.id.addFriendButton)

        func()
        userRecyclerView = view.findViewById(R.id.userRecyclerView2)
        userRecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FriendRequestAdapter(userList)
        userRecyclerView.adapter = adapter



        databaseReference.child("Requests").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                requestList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(SearchUser::class.java)
                    if(currentUser?.to == firebaseAuth.currentUser?.uid){
                        requestList.add(currentUser!!.from)
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //gamoaqvs userebi friend recycle view-shi
        databaseReference.child("User").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(currentUser?.uid in requestList){
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
    private fun func(){
        addFriendButton.setOnClickListener{
            val intent = Intent(activity, MySentRequestsActivity::class.java)
            startActivity(intent)
        }
    }

}