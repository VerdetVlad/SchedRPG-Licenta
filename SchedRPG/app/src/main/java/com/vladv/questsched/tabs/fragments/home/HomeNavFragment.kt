package com.vladv.questsched.tabs.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentHomeNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeNavFragment : Fragment() {

    private var _binding: FragmentHomeNavBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentFragment : Fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNavBinding.inflate(inflater, container, false)

        activity?.supportFragmentManager?.beginTransaction()?.replace(binding.homeNavFragmentLayout.id,HomeQuestsFragment())?.commit()

        val bottomNav : BottomNavigationView = binding.homeBottomNav
        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_nav_quest_menu -> {
                    currentFragment = HomeQuestsFragment()
                }
                R.id.home_nav_stats_menu -> {
                    currentFragment = HomeStatsFragment()
                }
            }
            activity?.supportFragmentManager?.beginTransaction()?.replace(binding.homeNavFragmentLayout.id,currentFragment)?.commit()
            true
        }

        return binding.root
    }


}