package com.vladv.questsched.tabs

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityFragmentManagerBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.fragments.QuestCreationFragment
import com.vladv.questsched.tabs.fragments.calendar.CalendarFragment
import com.vladv.questsched.tabs.fragments.home.HomeNavFragment
import com.vladv.questsched.tabs.fragments.questlistview.QuestListFragment
import com.vladv.questsched.tabs.settings.SettingsFragment
import com.vladv.questsched.user.User


class MyFragmentManager : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentManagerBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var firebaseAuth : FirebaseAuth

    private val TIME_INTERVAL = 2000
    private var backPressed:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManagerBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)



        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener  { firebaseAuth ->
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

                userProfile = snapshot.getValue(User::class.java)

                stopLoading()

                val userName = findViewById<View>(R.id.user_name) as TextView
                val mailAddress = findViewById<View>(R.id.nav_mail_adress) as TextView
                val avatarProfile = findViewById<View>(R.id.avatarProfilePic) as ImageView

                userName.text = userProfile!!.fullName
                mailAddress.text = userProfile!!.email
                userProfile!!.avatar?.drawableFace?.let { avatarProfile.setImageResource(it) }

                supportFragmentManager.beginTransaction().apply {
                    replace(binding.flFragment.id, HomeNavFragment())
                    commit()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong when retrieving data: $error", Toast.LENGTH_LONG).show()
                stopLoading()
            }
        })


        reference.child(userID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                startLoading()


                userProfile = snapshot.getValue(User::class.java)

                stopLoading()

                val userName = findViewById<View>(R.id.user_name) as TextView
                val mailAddress = findViewById<View>(R.id.nav_mail_adress) as TextView
                val avatarProfile = findViewById<View>(R.id.avatarProfilePic) as ImageView

                userName.text = userProfile!!.fullName
                mailAddress.text = userProfile!!.email
                userProfile!!.avatar?.drawableFace?.let { avatarProfile.setImageResource(it) }

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

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
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

                R.id.nav_home -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(binding.flFragment.id, HomeNavFragment())
                    addToBackStack(null)
                    drawerLayout.closeDrawers()
                }

                R.id.nav_new_quest -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(binding.flFragment.id, QuestCreationFragment())
                    addToBackStack(null)
                    drawerLayout.closeDrawers()
                }


                R.id.nav_quest_list -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(binding.flFragment.id, QuestListFragment())
                    addToBackStack(null)
                    drawerLayout.closeDrawers()
                }

                R.id.nav_calendar -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(binding.flFragment.id, CalendarFragment())
                    addToBackStack(null)
                    drawerLayout.closeDrawers()
                }

                R.id.nav_setting -> supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(binding.flFragment.id, SettingsFragment())
                    addToBackStack(null)
                    drawerLayout.closeDrawers()
                }




                R.id.nav_logout -> FirebaseAuth.getInstance().signOut()

            }

            true
        }
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        val actionbar: ActionBar = supportActionBar!!
        actionbar.setHomeAsUpIndicator(R.drawable.sidenav_display_drawer)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){

            return true


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {

        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if(backStackEntryCount != 0) {
            super.onBackPressed()
            return
        }

        if(backPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed()
            return
        }
        else{
            Toast.makeText(baseContext, "Press Back Again to Exit App", Toast.LENGTH_SHORT ).show()
        }
        backPressed = System.currentTimeMillis()
    }


    fun startLoading()
    {
        binding.progressBarContainer.visibility = View.VISIBLE
    }

    fun stopLoading()
    {
        binding.progressBarContainer.visibility = View.GONE
    }


    fun isNightMode(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    companion object {
        var userProfile: User? = null
    }

    fun refreshActivity(){
        val mIntent = intent
        finish()
        startActivity(mIntent)
    }

}