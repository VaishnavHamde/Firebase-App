package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.sign

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)

        signUpBinding.buttonSignUp.setOnClickListener {
            val userEmail = signUpBinding.editTextEmailSIgnUp.text.toString()
            val userPassword = signUpBinding.editTextPasswordSignUp.text.toString()

            signUpWithFirebase(userEmail, userPassword)
        }
    }

    fun signUpWithFirebase(userEmail : String, userPassword : String){
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            if(task.isSuccessful){
                finish()
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}