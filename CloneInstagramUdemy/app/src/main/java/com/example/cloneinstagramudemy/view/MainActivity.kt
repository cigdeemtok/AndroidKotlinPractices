package com.example.cloneinstagramudemy.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cloneinstagramudemy.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//login-logout page
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebase auth
        auth = Firebase.auth

        //check if a user already logged in
        val currentUser = auth.currentUser

        if(currentUser != null ){
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

        listeners()



    }

    //define onClicklisteners
    private fun listeners(){
        //sign up
        binding.signUpButton.setOnClickListener {
            //get edit texts's input
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            //check edit texts to see if inputs are valid
            if(email.isNotEmpty() && password.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                    //if sign in succeeds
                    val intent = Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener{
                    //if sign in fails
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }else{
                //if inputs are invalid
                Toast.makeText(this,"Please enter email and password!", Toast.LENGTH_LONG).show()
            }
        }

        //sign in
        binding.signInButton.setOnClickListener {
            // get inputs
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            //check edit texts to see if inputs are valid
            if(email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                    //if sign in succeeds
                    val intent = Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()

                }.addOnFailureListener {
                    //if sign in fails
                    Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()

                }
            }
            else{
                //if inputs are invalid
                Toast.makeText(this,"Please enter email and password!", Toast.LENGTH_LONG).show()
            }


        }

    }


}