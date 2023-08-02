package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.databinding.ActivityForegetBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {

    lateinit var forgetBinding: ActivityForegetBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgetBinding = ActivityForegetBinding.inflate(layoutInflater)
        val view = forgetBinding.root
        setContentView(view)

        forgetBinding.buttonResetPassword.setOnClickListener {
            val email = forgetBinding.editTextResetEmail.text.toString()

            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "We have sent a password reset mail to your mail address.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}