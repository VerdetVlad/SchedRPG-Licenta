package com.example.schedrpg.myfirebasetool;

import androidx.annotation.NonNull;

import com.example.schedrpg.user.User;
import com.example.schedrpg.user.UserTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ChangeFirebaseData {
    private DatabaseReference databaseReference;
    private String userID;

    public ChangeFirebaseData()
    {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(User.class.getSimpleName());
    }

    public void updateUserData(User userData)
    {
        databaseReference.child(userID).setValue(userData);
    }

    public void updateTask(UserTask userTask, String i)
    {
        databaseReference.child(userID).child("tasks").child(i).setValue(userTask);
    }



}
