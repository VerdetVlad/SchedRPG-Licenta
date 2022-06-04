package com.vladv.questsched.tabs.fragments.social.subfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialFriendsBinding
import com.example.schedrpg.databinding.FragmentSocialRequestBinding
import com.example.schedrpg.databinding.FragmentSocialSearchBinding
import com.vladv.questsched.tabs.fragments.social.SocialNavFragment


class SocialRequestFragment : Fragment() {

    private var _binding: FragmentSocialRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSocialRequestBinding.inflate(inflater, container, false)
        activity?.title = "Friend Request"
        SocialNavFragment.currentFragment = SocialRequestFragment()



        return binding.root
    }


}