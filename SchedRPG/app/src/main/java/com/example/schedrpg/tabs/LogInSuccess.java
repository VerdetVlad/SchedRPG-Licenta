package com.example.schedrpg.tabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedrpg.R;
import com.example.schedrpg.authentification.LogIn;
import com.example.schedrpg.myfirebasetool.ChangeFirebaseData;
import com.example.schedrpg.myfirebasetool.OnGetDataListener;
import com.example.schedrpg.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInSuccess extends AppCompatActivity implements View.OnClickListener, OnGetDataListener {

    private Button logout;
    private Button createTask;
    private Button viewTask;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userID;

    public static User userProfile  = new User("ceva","ceva");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_success);


        logout = (Button) findViewById(R.id.LogOutButton);
        logout.setOnClickListener(this);

        createTask = (Button) findViewById(R.id.createTaskButton);
        createTask.setOnClickListener(this);

        viewTask = (Button) findViewById(R.id.viewTaskButton);
        viewTask.setOnClickListener(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
        userID = firebaseUser.getUid();

        final TextView textViewFullName = (TextView) findViewById((R.id.fullNameProfile));
        final TextView textViewEmail = (TextView) findViewById((R.id.emailProfile));

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userProfile = snapshot.getValue(User.class);
                textViewFullName.setText(userProfile.getFullName());
                textViewEmail.setText(userProfile.getEmail());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInSuccess.this,"Something went wrong: " + error,Toast.LENGTH_LONG).show();

            }
        });



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createTaskButton:
                startActivity(new Intent(LogInSuccess.this, TaskCreationTab.class));
                break;
            case R.id.viewTaskButton:
                startActivity(new Intent(LogInSuccess.this, TasksViewTab.class));
                break;
            case R.id.LogOutButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogInSuccess.this, LogIn.class));
                break;

        }
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onSuccess(DataSnapshot data) {

    }

    @Override
    public void onFailed(DatabaseError databaseError) {

    }



}