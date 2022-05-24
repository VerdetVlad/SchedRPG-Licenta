package com.vladv.questsched.tabs.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentHomeStatsBinding
import com.vladv.questsched.user.User


class HomeStatsFragment : Fragment() {

    private var _binding: FragmentHomeStatsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStatsBinding.inflate(inflater, container, false)

        activity?.title = "Character screen"

        val progressXP = User().stats?.currXP?.times(100)?.div(User().stats!!.maxXP!!)
        val stats:ArrayList<Int> = User().stats?.stats!!
        binding.userName.text = User().fullName + " LvL." + User().stats?.level

        binding.xpProgressBar2.progress = progressXP!!
        binding.strTextView.text = "STR:" + "\n" + stats[0]
        binding.conTextView.text = "CON:" + "\n" + stats[1]
        binding.dexTextView.text = "DEX:" + "\n" + stats[2]
        binding.intTextView.text = "INT:" + "\n" + stats[3]
        binding.wisTextView.text = "WIS:" + "\n" + stats[4]
        binding.chaTextView.text = "CHA:" + "\n" + stats[5]

        return binding.root
    }

}