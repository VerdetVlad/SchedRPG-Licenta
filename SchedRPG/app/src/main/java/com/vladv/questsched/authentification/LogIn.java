package com.vladv.questsched.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schedrpg.databinding.ActivityLoginBinding;
import com.vladv.questsched.tabs.LogInSuccess;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity{

    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        binding.createAccount.setOnClickListener(v -> startActivity(new Intent(this, Register.class)));

        binding.buttonLogin.setOnClickListener(v -> userLogin());

        editTextEmail = binding.email;
        editTextPassword = binding.password;
        progressBar = binding.progressBar;

    }



    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is required.");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide a valid email.");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError("Minimum password length is 6  characters.");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(LogIn.this, LogInSuccess.class));
            }
            else{
                Toast.makeText(LogIn.this,"Failed to LogIn. Please check your credentials and try again.",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}