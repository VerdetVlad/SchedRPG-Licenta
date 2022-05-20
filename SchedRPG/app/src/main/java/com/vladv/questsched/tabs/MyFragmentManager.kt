package com.vladv.questsched.tabs

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityFragmentManagerBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.fragments.CalendarFragment
import com.vladv.questsched.tabs.fragments.HomeFragment
import com.vladv.questsched.tabs.fragments.TaskCreationFragment
import com.vladv.questsched.tabs.fragments.TaskListFragment
import com.vladv.questsched.user.User


class MyFragmentManager : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentManagerBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManagerBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                val intent = Intent(this, LogIn::class.java)
                startActivity(intent)
            }
        }

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
        val userID = firebaseUser!!.uid
        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userProfile = snapshot.getValue(
                    User::class.java
                )
                stopLoading()

                val userName = findViewById<View>(R.id.nav_user_name) as TextView
                val mailAddress = findViewById<View>(R.id.nav_mail_adress) as TextView

                userName.text = userProfile!!.fullName
                mailAddress.text = userProfile!!.email

                supportFragmentManager.beginTransaction().apply {
                    replace(binding.flFragment.id, HomeFragment())
                    commit()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong when retrieving data: $error", Toast.LENGTH_LONG).show()
                stopLoading()
            }
        })

        //drawer setup
        configureDrawer()
        configureToolbar()



    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this.authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this.authStateListener)
    }



    private fun configureDrawer()
    {
        val drawerLayout : DrawerLayout  = binding.drawerLayout
        val navView : NavigationView = binding.navView
        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_home -> supportFragmentManager.beginTransaction().apply {
                    replace(binding.flFragment.id, HomeFragment())
                    addToBackStack("")
                    commit()
                    drawerLayout.closeDrawers()
                }

                R.id.nav_new_quest -> supportFragmentManager.beginTransaction().apply {
                    replace(binding.flFragment.id, TaskCreationFragment())
                    addToBackStack("")
                    commit()
                    drawerLayout.closeDrawers()
                }
                R.id.nav_quest_list -> supportFragmentManager.beginTransaction().apply {
                    replace(binding.flFragment.id, TaskListFragment())
                    addToBackStack("")
                    commit()
                    drawerLayout.closeDrawers()
                }

                R.id.nav_calendar -> supportFragmentManager.beginTransaction().apply {
                    replace(binding.flFragment.id, CalendarFragment())
                    addToBackStack("")
                    commit()
                    drawerLayout.closeDrawers()
                }

                R.id.nav_setting -> Toast.makeText(applicationContext,"Clicked Setting",Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> FirebaseAuth.getInstance().signOut()

            }

            true
        }
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar = supportActionBar!!
        actionbar.setHomeAsUpIndicator(R.drawable.ic_display_drawer)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){

            return true


        }
        return super.onOptionsItemSelected(item)
    }


    fun startLoading()
    {
        binding.progressBarContainer.visibility = View.VISIBLE
    }
    fun stopLoading()
    {
        binding.progressBarContainer.visibility = View.GONE
    }

    companion object {
        var userProfile: User? = null
    }



}