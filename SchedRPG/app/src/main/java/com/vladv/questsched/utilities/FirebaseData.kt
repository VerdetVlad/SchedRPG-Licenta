package com.vladv.questsched.utilities

import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.vladv.questsched.user.User

class FirebaseData {
    private val databaseReference: DatabaseReference
    private val userID: String = FirebaseAuth.getInstance().currentUser!!.uid
    fun updateUserData() {
        databaseReference.child(userID).setValue(User())
    }

    fun updateTask(quest: Quest?, i: String?) {
        databaseReference.child(userID).child("tasks").child(i!!).setValue(quest)
    }

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference(User::class.java.simpleName)
    }
}