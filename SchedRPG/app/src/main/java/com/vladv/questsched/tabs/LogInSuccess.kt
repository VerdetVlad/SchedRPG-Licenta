package com.vladv.questsched.tabs

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.setupWithNavController
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityLogInSuccessBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.user.User

class LogInSuccess : AppCompatActivity() {
    var binding: ActivityLogInSuccessBinding? = null
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInSuccessBinding.inflate(
            layoutInflater
        )
        val view: View = binding!!.root

        setContentView(view)




        val drawerLayout : DrawerLayout = binding!!.drawerLayout
        val navView : NavigationView = binding!!.navView

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_new_quest -> startActivity(
                    Intent(
                        this@LogInSuccess,
                        TaskCreationTab::class.java
                    )
                )
                R.id.nav_quest_list -> startActivity(
                    Intent(
                        this@LogInSuccess,
                        TasksViewTab::class.java
                    )
                )
                R.id.nav_setting -> Toast.makeText(applicationContext,"Clicked Setting",Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> startActivity(
                    Intent(
                        this@LogInSuccess,
                        LogIn::class.java
                    )
                )

            }

            true


        }


        binding!!.viewTaskButton.setOnClickListener {
            startActivity(
                Intent(
                    this@LogInSuccess,
                    TasksViewTab::class.java
                )
            )
        }
        binding!!.createTaskButton.setOnClickListener {
            startActivity(
                Intent(
                    this@LogInSuccess,
                    TaskCreationTab::class.java
                )
            )
        }
        binding!!.LogOutButton.setOnClickListener {
            startActivity(
                Intent(
                    this@LogInSuccess,
                    LogIn::class.java
                )
            )
        }
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
        val userID = firebaseUser!!.uid
        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userProfile = snapshot.getValue(
                    User::class.java
                )
                binding!!.fullNameProfile.text = userProfile!!.fullName
                binding!!.emailProfile.text = userProfile!!.email
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LogInSuccess, "Something went wrong: $error", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    companion object {
        var userProfile: User? = User("ceva", "ceva")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){

            return true


        }
        return super.onOptionsItemSelected(item)
    }
}