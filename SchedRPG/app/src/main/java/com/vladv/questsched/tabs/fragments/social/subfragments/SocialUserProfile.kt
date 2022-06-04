package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialRequestBinding
import com.example.schedrpg.databinding.FragmentSocialUserProfileBinding


class SocialUserProfile(private val viewedUserID:String) : Fragment() {

    private var _binding: FragmentSocialUserProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSocialUserProfileBinding.inflate(inflater, container, false)

        


        return binding.root
    }


}