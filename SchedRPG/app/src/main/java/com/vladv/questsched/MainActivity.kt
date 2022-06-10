package com.vladv.questsched

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.schedrpg.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.User
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user!= null && user.isEmailVerified)
        {
            retrieveUserData()
        }else
        {
            val intent = Intent(this, LogIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            finish()
        }


    }

    override fun onStart() {
        super.onStart()

        try{
            val filename = "logcat_" + System.currentTimeMillis() +".txt"
            val outputFile = File(applicationContext?.externalCacheDir,filename)
            Runtime.getRuntime().exec("logcat -f" + outputFile.absolutePath)

        }catch (e:Exception)
        {
            Toast.makeText(applicationContext, "Error trying to write log: $e", Toast.LENGTH_LONG).show()
        }


    }

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }


    private fun retrieveUserData()
    {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
        val userID = firebaseUser!!.uid
        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                MyFragmentManager.userData = snapshot.getValue(User::class.java)

                if (MyFragmentManager.userData?.themeNightMode == true) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }


                val intent = Intent(this@MainActivity, MyFragmentManager::class.java)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
                finish()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong when retrieving data: $error", Toast.LENGTH_LONG).show()

            }
        })
    }
}