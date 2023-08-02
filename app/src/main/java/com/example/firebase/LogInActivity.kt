package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    lateinit var logInBinding: ActivityLogInBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logInBinding = ActivityLogInBinding.inflate(layoutInflater)
        val view = logInBinding.root
        setContentView(view)

        logInBinding.buttonSignIn.setOnClickListener {
            val userEmail = logInBinding.editTextEmailSIgnIn.text.toString()
            val userPassword = logInBinding.editTextPasswordSignIn.text.toString()

            signInWithFirebase(userEmail, userPassword)
        }

        logInBinding.textViewSignUp.setOnClickListener {
            val intent = Intent(this@LogInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        logInBinding.textViewForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
        }

        logInBinding.textViewSignWithPhoneNumber.setOnClickListener{
            val intent = Intent(this, PhoneActivity::class.java)
            startActivity(intent)
        }
    }

    fun signInWithFirebase(userEmail : String, userPassword : String){
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Sign in is successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@LogInActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if(user != null){
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}