package com.vladv.questsched.authentification

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schedrpg.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.vladv.questsched.tabs.FragmentManager
import com.vladv.questsched.tabs.LogInSuccess

class LogIn : AppCompatActivity() {
    private var editTextEmail: EditText? = null
    private var editTextPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null
    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)


        mAuth = FirebaseAuth.getInstance()
        binding!!.createAccount.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Register::class.java
                )
            )
        }
        binding!!.buttonLogin.setOnClickListener { userLogin() }
        editTextEmail = binding!!.email
        editTextPassword = binding!!.password
        progressBar = binding!!.progressBar
    }

    private fun userLogin() {
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
        progressBar!!.visibility = View.VISIBLE
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    progressBar!!.visibility = View.GONE
                    startActivity(Intent(this@LogIn, FragmentManager::class.java))
                } else {
                    Toast.makeText(
                        this@LogIn,
                        "Failed to LogIn. Please check your credentials and try again.",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar!!.visibility = View.GONE
                }
            }
    }
}