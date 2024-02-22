package com.example.cloneinstagramudemy.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloneinstagramudemy.R
import com.example.cloneinstagramudemy.adapter.FeedAdapter
import com.example.cloneinstagramudemy.databinding.ActivityFeedBinding
import com.example.cloneinstagramudemy.model.Posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFeedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Posts>
    private lateinit var feedAdapter : FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize authentication and firestore database
        auth = Firebase.auth
        firestore = Firebase.firestore

        //initialize posts array
        postArrayList = arrayListOf()

        //get data to display in feed activity
        getData()

        //initialize adapter
        binding.recyclerFeed.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedAdapter(postArrayList)
        binding.recyclerFeed.adapter = feedAdapter




    }

    //get data function to display data in recyclerview
    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){
        //get documents in collection by key-value pairs
        firestore.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null){
                //if an error occurs : show a toast message
                Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value != null){
                    //if query snapshot is not empty
                    if(!value.isEmpty){
                        //get documents
                        val documents = value.documents

                        postArrayList.clear()

                        for(document in documents){
                            // get documents one by one to display in feed
                            val description = document.get("description")
                            val userEmail = document.get("userEmail")
                            val imageUrl = document.get("imageUrl")
                            val date = document.getDate("date")

                            //split date into parts for displaying in feed
                            val parts = date.toString().split("\\s".toRegex())
                            //println(parts)

                            //format splitted date parts
                            val dateToDisplay = parts[2] + " " + parts[1] + " " + parts[5]

                            //Log.d("data",description.toString())
                            //Log.d("data", userEmail.toString())
                            //Log.d("data",imageUrl.toString())
                            //Log.d("data", date.toString())

                            val post = Posts(userEmail.toString(), description.toString(), imageUrl.toString(),dateToDisplay)
                            postArrayList.add(post)

                        }
                        feedAdapter.notifyDataSetChanged()
                    }
                }
            }
        }


    }
    //inflating options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_options,menu)

        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signOutMenu -> {
                //signout and go to login page
                auth.signOut()
                val intent = Intent(this@FeedActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.addPostMenu -> {
                //go to posting page
                val intent = Intent(this@FeedActivity, PostActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}