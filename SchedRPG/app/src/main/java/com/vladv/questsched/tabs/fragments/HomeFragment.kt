package com.vladv.questsched.tabs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
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

        activity?.title = "Home"

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
            FirebaseAuth.getInstance().signOut()
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