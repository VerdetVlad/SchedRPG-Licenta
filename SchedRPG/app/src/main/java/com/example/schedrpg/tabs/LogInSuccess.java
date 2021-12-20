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
import com.example.schedrpg.user.UserTask;
import com.example.schedrpg.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogInSuccess extends AppCompatActivity implements View.OnClickListener, OnGetDataListener {

    private Button logout;
    private Button testButton;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userID;
    private ChangeFirebaseData changeFirebaseData;
    private long countTasks;

    public static User userProfile  = new User("ceva","ceva");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_success);


        logout = (Button) findViewById(R.id.buttonLogout);
        logout.setOnClickListener(this);

        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference(User.class.getSimpleName());
        userID = firebaseUser.getUid();

        final TextView textViewFullName = (TextView) findViewById((R.id.fullNameProfile));
        final TextView textViewEmail = (TextView) findViewById((R.id.emailProfile));

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                textViewFullName.setText(user.getFullName());
                textViewEmail.setText(user.getTasks().get(0).toString());
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
            case R.id.testButton:
                //userTools.updateTask(new UserTask((int) countTasks,(int) countTasks,"SomeName","Test"),String.valueOf(countTasks));
                System.out.println("--------------------------------------------------------------------------");
                System.out.println(userProfile);
                System.out.println("--------------------------------------------------------------------------");
                break;
            case R.id.buttonLogout:
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

    static class Holder
    {
        private static User myConstUser;

        public Holder() {

        }

        public void setMyConstUser(User user)
        {
            Holder.myConstUser = user;
        }

        public User getMyConstUser()
        {
            return Holder.myConstUser;
        }

    }

}