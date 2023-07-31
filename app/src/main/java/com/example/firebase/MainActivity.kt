package com.example.firebase

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityMainBinding

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference : DatabaseReference = database.reference.child("MyUsers")

    val usersList = ArrayList<Users>()

    lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = usersAdapter.getUserId(viewHolder.adapterPosition)

                myReference.child(id).removeValue()

                Toast.makeText(this@MainActivity, "The user is deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(mainBinding.recyclerView)

        retrieveDataFromDatabase()

    }

    fun retrieveDataFromDatabase(){
        myReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                usersList.clear()

                for(eachUser in snapshot.children){
                    val user = eachUser.getValue(Users::class.java)

                    if(user != null)
                        usersList.add(user)

                    usersAdapter = UsersAdapter(this@MainActivity, usersList)

                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    mainBinding.recyclerView.adapter = usersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_delete_all, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_all){
            showDialogMessage()
        }

        return true
    }

    private fun showDialogMessage(){
        val dialogMessage = AlertDialog.Builder(this)

        dialogMessage.setTitle("Delete All Users!")
        dialogMessage.setMessage("If click Yes, all users will be deleted, If you want to delete specific user, swipe the item you want " +
                "to delete left or right.")
        dialogMessage.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
        })
        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            myReference.removeValue().addOnCompleteListener {task ->
                if(task.isSuccessful){
                    usersAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "All usersw were deleted", Toast.LENGTH_SHORT).show()
                }
            }
        })

        dialogMessage.create().show()

    }
}