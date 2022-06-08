package com.vladv.questsched.tabs.fragments.social

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSocialNavBinding
import com.example.schedrpg.databinding.FragmentSocialSearchBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.home.HomeNavFragment
import com.vladv.questsched.tabs.fragments.home.subfragments.HomeQuestsFragment
import com.vladv.questsched.tabs.fragments.home.subfragments.HomeStatsFragment
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialFriendsFragment
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialRequestFragment
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialSearchFragment


class SocialNavFragment : Fragment() {

    private var _binding: FragmentSocialNavBinding? = null
    private val binding get() = _binding!!

    companion object{
        var currentFragment : Fragment = SocialFriendsFragment()
        var pressedButtonId : Int = R.id.social_nav_friends_menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSocialNavBinding.inflate(inflater, container, false)


        binding.socialBottomNav.selectedItemId = pressedButtonId

        if(savedInstanceState==null) parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.fragment_fadein,
                R.anim.fragment_fadeout,
                R.anim.fragment_fadein,
                R.anim.fragment_fadeout
            )
            replace(binding.socialNavFragmentLayout.id, currentFragment)

        }




        val bottomNav : BottomNavigationView = binding.socialBottomNav
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.social_nav_friends_menu -> {
                    currentFragment = SocialFriendsFragment()
                    pressedButtonId = R.id.social_nav_friends_menu
                }
                R.id.social_nav_search_menu -> {
                    currentFragment = SocialSearchFragment()
                    pressedButtonId = R.id.social_nav_search_menu
                }
                R.id.social_nav_requests_menu -> {
                    currentFragment = SocialRequestFragment()
                    pressedButtonId = R.id.social_nav_requests_menu
                }
            }

            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                replace(binding.socialNavFragmentLayout.id, currentFragment)
            }
            true
        }



        return binding.root

    }


}