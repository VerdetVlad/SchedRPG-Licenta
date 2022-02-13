package com.vladv.questsched.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vladv.questsched.user.User;
import com.vladv.questsched.authentification.LogIn;
import com.example.schedrpg.databinding.ActivityLogInSuccessBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInSuccess extends AppCompatActivity {

    ActivityLogInSuccessBinding binding;

    public static User userProfile  = new User("ceva","ceva");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInSuccessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.viewTaskButton.setOnClickListener(v -> startActivity(new Intent(LogInSuccess.this, TasksViewTab.class)));

        binding.createTaskButton.setOnClickListener(v -> startActivity(new Intent(LogInSuccess.this, TaskCreationTab.class)));

        binding.LogOutButton.setOnClickListener(v -> startActivity(new Intent(LogInSuccess.this, LogIn.class)));


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
        String userID = firebaseUser.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userProfile = snapshot.getValue(User.class);
                binding.fullNameProfile.setText(userProfile.getFullName());
                binding.emailProfile.setText(userProfile.getEmail());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInSuccess.this,"Something went wrong: " + error,Toast.LENGTH_LONG).show();

            }
        });



    }



}