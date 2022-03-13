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

        binding.fullNameProfile.text = User().fullName
        binding.emailProfile.text = User().email


        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}