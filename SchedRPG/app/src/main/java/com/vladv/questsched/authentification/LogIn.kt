package com.vladv.questsched.authentification

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.schedrpg.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.User

class LogIn : AppCompatActivity() {
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null

    private var mAuth: FirebaseAuth? = null
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener


    private var binding: ActivityLoginBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null) {
                retrieveUserData()
            }
        }


        binding!!.createAccount.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
        binding!!.buttonLogin.setOnClickListener { userLogin() }
        editTextEmail = binding!!.email
        editTextPassword = binding!!.password



        //for testing only
        editTextEmail!!.setText("a@g.com")
        editTextPassword!!.setText("123456")

    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this.authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this.authStateListener)
    }

    private fun userLogin() {
        mAuth = FirebaseAuth.getInstance()

        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            editTextEmail!!.error = "Email is required."
            editTextEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail!!.error = "Please provide a valid email."
            editTextEmail!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            editTextPassword!!.error = "Password is required."
            editTextPassword!!.requestFocus()
            return
        }
        if (password.length < 6) {
            editTextPassword!!.error = "Minimum password length is 6  characters."
            editTextPassword!!.requestFocus()
            return
        }
        startLoading()




        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {

                    retrieveUserData()

                } else {
                    Toast.makeText(
                        this@LogIn,
                        "Failed to LogIn. Please check your credentials and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    stopLoading()
                }
            }
    }

    private fun retrieveUserData()
    {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
        val userID = firebaseUser!!.uid
        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                MyFragmentManager.userProfile = snapshot.getValue(User::class.java)

                //stopLoading()


                val intent = Intent(this@LogIn, MyFragmentManager::class.java)
                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
                finish()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Something went wrong when retrieving data: $error", Toast.LENGTH_LONG).show()
                stopLoading()
            }
        })
    }

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

    fun startLoading()
    {
        binding!!.progressBarContainer.visibility = View.VISIBLE
    }

    fun stopLoading()
    {
        binding!!.progressBarContainer.visibility = View.GONE
    }
}