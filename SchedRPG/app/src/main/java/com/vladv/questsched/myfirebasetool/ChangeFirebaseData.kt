package com.vladv.questsched.myfirebasetool

import com.google.firebase.database.DatabaseReference
import com.vladv.questsched.user.UserTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vladv.questsched.user.User

class ChangeFirebaseData {
    private val databaseReference: DatabaseReference
    private val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
    fun updateUserData(userData: User?) {
        databaseReference.child(userID).setValue(userData)
    }

    fun updateTask(userTask: UserTask?, i: String?) {
        databaseReference.child(userID).child("tasks").child(i!!).setValue(userTask)
    }

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference(User::class.java.simpleName)
    }
}