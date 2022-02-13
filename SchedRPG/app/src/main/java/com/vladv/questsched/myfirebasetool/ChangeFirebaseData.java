package com.vladv.questsched.myfirebasetool;

import com.vladv.questsched.user.User;
import com.vladv.questsched.user.UserTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeFirebaseData {
    private final DatabaseReference databaseReference;
    private final String userID;

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
