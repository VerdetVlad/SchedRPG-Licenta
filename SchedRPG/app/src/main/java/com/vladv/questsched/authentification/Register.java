package com.vladv.questsched.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schedrpg.databinding.ActivityRegisterBinding;
import com.vladv.questsched.user.User;
import com.vladv.questsched.user.UserTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText editTextFullName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        binding.appNameReg.setOnClickListener(v -> startActivity(new Intent(this, LogIn.class)));

        binding.buttonRegister.setOnClickListener(v -> registerUser());

        editTextFullName = binding.fullName;
        editTextEmail = binding.emailReg;
        editTextPassword = binding.passwordReg;

        progressBar = binding.progressBarReg;

    }

    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(fullName.isEmpty())
        {
            editTextFullName.setError("Full name is required.");
            editTextFullName.requestFocus();
            return;
        }

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
            editTextPassword.setError("Please provide a password with a minimum of 6 characters.");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        User user = new User(fullName,email);
                        user.addTask(new UserTask(1,2,"Run","I like to run"));
                        FirebaseDatabase.getInstance().getReference(User.class.getSimpleName())
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful())
                                    {
                                        Toast.makeText(Register.this,"User has been registered successfully.",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(Register.this, LogIn.class));

                                    }
                                    else
                                    {
                                        Toast.makeText(Register.this,"Failed to register. Please try again.",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    }
                    else{
                        Toast.makeText(Register.this,"Failed to register. Please try again.",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }
}