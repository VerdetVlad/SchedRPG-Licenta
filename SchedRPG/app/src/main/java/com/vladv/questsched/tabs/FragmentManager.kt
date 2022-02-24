package com.vladv.questsched.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityFragmentManagerBinding
import com.google.android.material.navigation.NavigationView
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.fragments.LogInSuccess2
import com.vladv.questsched.tabs.fragments.TaskCreation

class FragmentManager : AppCompatActivity() {
    var binding : ActivityFragmentManagerBinding? = null
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManagerBinding.inflate(layoutInflater)
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

                R.id.nav_home -> supportFragmentManager.beginTransaction().apply {
                    replace(binding!!.flFragment.id, LogInSuccess2())
                    commit()
                    drawerLayout.closeDrawers()
                }

                R.id.nav_new_quest -> supportFragmentManager.beginTransaction().apply {
                    replace(binding!!.flFragment.id, TaskCreation())
                    commit()
                    drawerLayout.closeDrawers()
                }
                R.id.nav_quest_list -> startActivity(
                    Intent(
                        this@FragmentManager,
                        TasksViewTab::class.java
                    )
                )
                R.id.nav_setting -> Toast.makeText(applicationContext,"Clicked Setting",Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> startActivity(
                    Intent(
                        this@FragmentManager,
                        LogIn::class.java
                    )
                )

            }

            true
        }

        supportFragmentManager.beginTransaction().apply {
            replace(binding!!.flFragment.id, LogInSuccess2())
            commit()
        }






    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){

            return true


        }
        return super.onOptionsItemSelected(item)
    }
}