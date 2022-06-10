package com.vladv.questsched.authentification

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityLoginBinding
import com.example.schedrpg.databinding.PopUpForgotPasswordBinding
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


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)


        firebaseAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null && firebaseUser.isEmailVerified) {
                retrieveUserData()
            }
        }


        binding!!.createAccount.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        binding!!.buttonLogin.setOnClickListener { userLogin() }

        binding!!.forgotPasswordLogIn.setOnClickListener {
            val dialog: AlertDialog?
            val dialogBuilder: AlertDialog.Builder =  AlertDialog.Builder(this, R.style.AlertDialogStyle)
            val inflater = LayoutInflater.from(this)
            val auxBinding = PopUpForgotPasswordBinding.inflate(inflater)



            dialogBuilder.setView(auxBinding.root)
            dialog = dialogBuilder.create()
            dialog.show()

            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            dialog.window!!.attributes = layoutParams

            auxBinding.forgotPasswordSend.setOnClickListener {
                forgotPassword(auxBinding.forgotPasswordEmail)
                dialog.dismiss()
            }

            auxBinding.forgotPasswordClose.setOnClickListener{
                dialog.dismiss()
            }
        }

        editTextEmail = binding!!.email
        editTextPassword = binding!!.password



//        //for testing only
//        editTextEmail!!.setText("tefema5526@musezoo.com")
//        editTextPassword!!.setText("123456")

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
                if (!task.isSuccessful) {
                    Toast.makeText(this@LogIn, "Failed to LogIn. Please check your credentials and try again.", Toast.LENGTH_LONG).show()
                    stopLoading()
                }
                else
                {
                    retrieveUserData()
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

                MyFragmentManager.userData = snapshot.getValue(User::class.java)

                stopLoading()


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
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

    private fun forgotPassword(username : EditText){
        if (username.text.toString().isEmpty()) {
            Toast.makeText(this,"Empty email field.",Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()) {
            Toast.makeText(this,"Please enter a valid email.",Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.fetchSignInMethodsForEmail(username.text.toString())
            .addOnCompleteListener { task ->
                val isNewUser = task.result.signInMethods!!.isEmpty()
                if (isNewUser) {
                    Toast.makeText(this,"Email not in database. New user?",Toast.LENGTH_SHORT).show()
                } else {
                    firebaseAuth.sendPasswordResetEmail(username.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this,"Email sent.",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }



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