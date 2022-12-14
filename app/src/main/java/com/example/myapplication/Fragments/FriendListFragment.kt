package com.example.myapplication.Fragments

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FriendListFragment : Fragment() {
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var tempuserList:ArrayList<String>
    private lateinit var adapter : UserAdapter
    private  var firebaseAuth = FirebaseAuth.getInstance()
    private  lateinit var  addFriend : FloatingActionButton
    private var databaseReference = FirebaseDatabase.getInstance().getReference()

    private lateinit var refreshProfile:SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userList = ArrayList()
        tempuserList=ArrayList()


        addFriend = view.findViewById(R.id.addFriendButton)
        refreshProfile=view.findViewById(R.id.refreshProfile)

        func()
        func_refresh()
        userRecyclerView = view.findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = UserAdapter(userList)
        userRecyclerView.adapter = adapter

        databaseReference.child("FriendList").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tempuserList.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(FriendListUser::class.java)

                    if(currentUser?.friend1==firebaseAuth.currentUser?.uid || currentUser?.friend2==firebaseAuth.currentUser?.uid){
                        if(currentUser?.friend1==firebaseAuth.currentUser?.uid){tempuserList.add(currentUser?.friend2.toString())}
                        if(currentUser?.friend2==firebaseAuth.currentUser?.uid){tempuserList.add(currentUser?.friend1.toString())}


                    }

                }
                adapter.notifyDataSetChanged()
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

                    if(firebaseAuth.currentUser?.uid != currentUser?.uid && currentUser?.uid in tempuserList){
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
        addFriend.setOnClickListener {
            val intent = Intent(activity, AddFriendActivity::class.java)
            startActivity(intent)
        }

    }

    private fun func_refresh(){
        refreshProfile.setOnRefreshListener {
            activity?.finish()
            startActivity(activity?.intent)
            activity?.overridePendingTransition(0,0)
            refreshProfile.isRefreshing=false

        }



    }


}