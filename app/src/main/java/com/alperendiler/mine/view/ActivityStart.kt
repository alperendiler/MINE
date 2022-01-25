package com.alperendiler.mine.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alperendiler.mine.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ActivityStart : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null){
            val intent = Intent(applicationContext, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun signInclicked(view : View){

        val email = binding.mailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    //Signed In
                    Toast.makeText(applicationContext,"Welcome: ${auth.currentUser?.email.toString()}",Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }


    }

    fun signUpClicked(view : View) {

        val userEmail = binding.mailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (userEmail.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val intent = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

            }
        }



    }
    }


