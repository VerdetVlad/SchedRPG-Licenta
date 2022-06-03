package com.vladv.questsched.utilities

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User


class FirebaseData {
    private val databaseReference: DatabaseReference
    private val userID: String = FirebaseAuth.getInstance().currentUser!!.uid

    fun updateUserData() {

        MyFragmentManager.startLoading()
        databaseReference.child(userID).setValue(User())
//        MyFragmentManager.stopLoading()


    }

    fun updateUserData(activity: FragmentActivity, onSuccess:String, onFailure:String) {

        MyFragmentManager.startLoading()
        databaseReference.child(userID).setValue(User())
            .addOnCompleteListener {
                Toast.makeText(activity, onSuccess, Toast.LENGTH_SHORT).show()
            }
            .addOnCanceledListener {
                Toast.makeText(activity, onFailure, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(activity, onFailure, Toast.LENGTH_SHORT).show()
            }
//        MyFragmentManager.stopLoading()

    }



    fun updateTask(quest: Quest?, i: String?) {
        databaseReference.child(userID).child("tasks").child(i!!).setValue(quest)
    }

    init {
        val db = FirebaseDatabase.getInstance()
        databaseReference = db.getReference(User::class.java.simpleName)
    }

}