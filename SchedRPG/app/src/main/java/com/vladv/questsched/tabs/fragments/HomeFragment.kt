package com.vladv.questsched.tabs.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.vladv.questsched.authentification.LogIn
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.User


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        activity?.title = "Home";

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        binding.createTaskButton.setOnClickListener {
            if(transaction != null) {
                transaction.replace(R.id.flFragment, TaskCreationFragment())
                transaction.addToBackStack("")
                transaction.commit()
            }
        }

        binding.viewTaskButton.setOnClickListener {
            if(transaction != null) {
                transaction.replace(R.id.flFragment,TaskListFragment() )
                transaction.addToBackStack("")
                transaction.commit()
            }
        }
        binding.LogOutButton.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    LogIn::class.java
                )
            )
        }

        (activity as MyFragmentManager).startLoading()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference(User::class.java.simpleName)
        val userID = firebaseUser!!.uid
        reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userProfile = snapshot.getValue(
                    User::class.java
                )
                binding.fullNameProfile.text = userProfile!!.fullName
                binding.emailProfile.text = userProfile!!.email
                (activity as MyFragmentManager).stopLoading()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Something went wrong: $error", Toast.LENGTH_LONG)
                    .show()
            }
        })

        return binding.root
    }

    companion object {
        var userProfile: User? = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}