package com.vladv.questsched.tabs

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityFragmentManagerBinding
import com.example.schedrpg.databinding.NavHeaderBinding
import com.google.android.material.navigation.NavigationView
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.fragments.CalendarFragment
import com.vladv.questsched.tabs.fragments.HomeFragment
import com.vladv.questsched.tabs.fragments.TaskCreationFragment
import com.vladv.questsched.tabs.fragments.TaskListFragment


class MyFragmentManager : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentManagerBinding
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentManagerBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        stopLoading()

        //drawer setup
        configureDrawer()
        configureToolbar()




        //Fragment managing
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragment.id, HomeFragment())
            commit()
        }

    }




    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
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
                R.id.nav_logout -> startActivity(
                    Intent(
                        this@MyFragmentManager,
                        LogIn::class.java
                    )
                )

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



}