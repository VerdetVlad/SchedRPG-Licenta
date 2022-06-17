package com.vladv.questsched

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.schedrpg.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.User
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference

    private val  CREATE_FILE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRef = FirebaseDatabase.getInstance().reference.child("User")


        createLogFile()

        alreadyLoggedInCheck()



    }

    override fun onStart() {
        super.onStart()



    }

    private fun alreadyLoggedInCheck()
    {
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

    private fun createLogFile()
    {
        try{
            val filename = "logcat_file.txt"
            val downloadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val logFiles = Files.createDirectories(Paths.get("$downloadFile/schedLogFile"))
            val outputFile = File(logFiles.toFile(),filename)

            if(outputFile.exists()) outputFile.delete()
            Runtime.getRuntime().exec("logcat -f " + outputFile.absolutePath)

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

    private fun setTheme(darkTheme:Boolean?)
    {
        if (darkTheme == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun retrieveUserData()
    {

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
        val userID = firebaseUser!!.uid

        var token: String?
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isComplete){
                token = task.result.toString()

                userRef.child(userID).child("token").setValue(token).addOnCompleteListener{
                    if(it.isSuccessful)
                    {
                        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                MyFragmentManager.userData = snapshot.getValue(User::class.java)

                                setTheme(MyFragmentManager.userData?.themeNightMode)

                                val intent = Intent(this@MainActivity, MyFragmentManager::class.java)
                                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                                startActivity(intent)
                                finish()
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext,
                                    "Something went wrong when retrieving data: $error",
                                    Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                    else{
                        Toast.makeText(applicationContext,
                            "Something went wrong when retrieving data user token",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


}