package com.vladv.questsched.tabs.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentHomeNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.home.subfragments.HomeQuestsFragment
import com.vladv.questsched.tabs.fragments.home.subfragments.HomeStatsFragment
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialSearchFragment


class HomeNavFragment : Fragment() {

    private var _binding: FragmentHomeNavBinding? = null
    private val binding get() = _binding!!
    companion object{
        var currentFragment : Fragment = HomeQuestsFragment()
        var pressedButtonId : Int = R.id.home_nav_quest_menu
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNavBinding.inflate(inflater, container, false)


        activity?.title = "Home"

        val bottomNav : BottomNavigationView = binding.homeBottomNav
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_nav_quest_menu -> {
                    currentFragment = HomeQuestsFragment()
                    pressedButtonId = R.id.home_nav_quest_menu
                }
                R.id.home_nav_stats_menu -> {
                    currentFragment = HomeStatsFragment()
                    pressedButtonId = R.id.home_nav_stats_menu
                }
            }

            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                replace(binding.homeNavFragmentLayout.id, currentFragment)
            }
            true
        }

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        binding.homeBottomNav.selectedItemId = pressedButtonId
        parentFragmentManager.beginTransaction().replace(binding.homeNavFragmentLayout.id,
            currentFragment
        ).commit()
    }




}