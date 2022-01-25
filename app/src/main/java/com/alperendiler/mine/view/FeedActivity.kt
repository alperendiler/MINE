package com.alperendiler.mine.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperendiler.mine.R
import com.alperendiler.mine.adapter.FeedRecyclerAdapter
import com.alperendiler.mine.databinding.ActivityFeedBinding
import com.alperendiler.mine.databinding.ActivityVideoPlayerBinding
import com.alperendiler.mine.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFeedBinding;
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    val postArrayList : ArrayList<Post> = ArrayList()
    var adapter : FeedRecyclerAdapter? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.mine_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_post) {
            //Upload Activity
            val intent = Intent(applicationContext, UploadActivity::class.java)
            startActivity(intent)

        } else if (item.itemId == R.id.sign_out) {
            //Logout

            auth.signOut()
            val intent = Intent(applicationContext, ActivityStart::class.java)
            startActivity(intent)
            finish()

        }

        return super.onOptionsItemSelected(item)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        db =Firebase.firestore


        getDataFromFirestore()

        binding.recyclerView.layoutManager =LinearLayoutManager(this)


        adapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = adapter



    }


    private fun getDataFromFirestore(){

        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            } else {

                if (snapshot != null) {
                    if (!snapshot.isEmpty) {

                        postArrayList.clear()

                        val documents = snapshot.documents
                        for (document in documents) {
                            val comment = document.get("comment") as String
                            val userEmail = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            //val timestamp = document.get("date") as Timestamp
                            //val date = timestamp.toDate()

                            val post = Post(userEmail,comment, downloadUrl)
                            postArrayList.add(post)
                        }
                        adapter!!.notifyDataSetChanged()

                    }
                }

            }
        }

    }
    }












