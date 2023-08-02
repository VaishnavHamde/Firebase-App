package com.example.firebase
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebase.databinding.ActivityUpdateUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class UpdateUserActivity : AppCompatActivity() {

    lateinit var updateUserBinding : ActivityUpdateUserBinding
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference = database.reference.child("MyUsers")
    val firebaseStorage : FirebaseStorage = FirebaseStorage.getInstance()
    val storageReference : StorageReference = firebaseStorage.reference

    var imageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateUserBinding = ActivityUpdateUserBinding.inflate(layoutInflater)
        val view = updateUserBinding.root
        setContentView(view)

        supportActionBar?.title = "Update User"

        registerActivityForResult()

        getAndSetData()

        updateUserBinding.buttonUpdateUser.setOnClickListener {
            uploadPhoto()
        }

        updateUserBinding.userUpdateProfileImage.setOnClickListener{
            chooseImage()
        }

    }

    private fun getAndSetData(){
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age", 0).toString()
        val email = intent.getStringExtra("email")
        val imageUrl = intent.getStringExtra("imageUrl")

        updateUserBinding.editTextUpdateName.setText(name)
        updateUserBinding.editTextUpdateAge.setText(age)
        updateUserBinding.editTextUpdateEmail.setText(email)
        Picasso.get().load(imageUrl).into(updateUserBinding.userUpdateProfileImage)
    }

    private fun updateData(imageUrl : String, imageName : String){
        val updatedName = updateUserBinding.editTextUpdateName.text.toString()
        val updatedAge = updateUserBinding.editTextUpdateAge.text.toString().toInt()
        val updatedEmail = updateUserBinding.editTextUpdateEmail.text.toString()
        val userId = intent.getStringExtra("id").toString()

        val userMap = mutableMapOf<String, Any>()
        userMap["userId"] = userId
        userMap["userName"] = updatedName
        userMap["userAge"] = updatedAge
        userMap["userEmail"] = updatedEmail
        userMap["url"] = imageUrl
        userMap["imageName"] = imageName

        myReference.child(userId).updateChildren(userMap).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "The user has been updated", Toast.LENGTH_LONG).show()

                updateUserBinding.buttonUpdateUser.visibility = View.VISIBLE
                updateUserBinding.progressBarUpdateUser.visibility = View.INVISIBLE

                finish()
            }
        }
    }

    fun chooseImage(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        else{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    fun registerActivityForResult(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
            , ActivityResultCallback { result ->
                val resultCode = result.resultCode
                val imageData = result.data

                if(resultCode == RESULT_OK && imageData != null){
                    imageUri = imageData.data

                    imageUri.let {
                        Picasso.get().load(it).into(updateUserBinding.userUpdateProfileImage)
                    }
                }
            })
    }

    fun uploadPhoto(){
        updateUserBinding.buttonUpdateUser.visibility = View.INVISIBLE
        updateUserBinding.progressBarUpdateUser.visibility = View.VISIBLE

        val imageName = intent.getStringExtra("imageName").toString()

        val imageReference = storageReference.child("images").child(imageName)

        imageUri?.let {uri ->
            imageReference.putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Image uploaded", Toast.LENGTH_LONG).show()

                val myUploadedImageReference = storageReference.child("images").child(imageName)

                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->
                    val imageURL = url.toString()

                    updateData(imageURL, imageName)
                }
            }.addOnFailureListener{
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}