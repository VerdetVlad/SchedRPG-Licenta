package com.vladv.questsched.authentification

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schedrpg.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vladv.questsched.user.User

class Register : AppCompatActivity() {
    private var editTextFullName: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var binding: ActivityRegisterBinding? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()
        binding!!.appNameReg.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LogIn::class.java
                )
            )
        }
        binding!!.buttonRegister.setOnClickListener { registerUser() }
        editTextFullName = binding!!.fullName
        editTextEmail = binding!!.emailReg
        editTextPassword = binding!!.passwordReg
        progressBar = binding!!.progressBarReg
    }

    private fun registerUser() {
        val fullName = editTextFullName!!.text.toString().trim { it <= ' ' }
        val email = editTextEmail!!.text.toString().trim { it <= ' ' }
        val password = editTextPassword!!.text.toString().trim { it <= ' ' }
        if (fullName.isEmpty()) {
            editTextFullName!!.error = "Full name is required."
            editTextFullName!!.requestFocus()
            return
        }
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
            editTextPassword!!.error = "Please provide a password with a minimum of 6 characters."
            editTextPassword!!.requestFocus()
            return
        }
        progressBar!!.visibility = View.VISIBLE
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user = User(fullName, email)
                    FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user).addOnCompleteListener { task1: Task<Void?> ->
                            if (task1.isSuccessful) {
                                Toast.makeText(
                                    this@Register,
                                    "User has been registered successfully.",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar!!.visibility = View.GONE
                                val intent = Intent(this@Register, LogIn::class.java)
                                intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@Register,
                                    "Failed to register. Please try again.",
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar!!.visibility = View.GONE
                            }
                        }
                } else {
                    Toast.makeText(
                        this@Register,
                        "Failed to register. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
    }

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }
}