package com.vladv.questsched.tabs.fragments.home.subfragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentHomeStatsBinding
import com.vladv.questsched.tabs.fragments.home.HomeNavFragment
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
        HomeNavFragment.currentFragment = HomeStatsFragment()


        val statsCurrProgress = ArrayList<Int>(6)
        for(i in 0..5)
            statsCurrProgress.add(User().stats!!.statsCurrXP[i]
                .times(100).div(User().stats!!.statsMaxXP[i]))


        val stats:ArrayList<Int> = User().stats?.statsLvl!!
        binding.userName.text = User().username + " LvL." + User().stats?.level

        User().avatar?.drawableBody?.let { binding.fullAvatarImageView.setImageResource(it) }

        binding.strTextView.text = "STR:" + "\n" + stats[0]
        binding.progressBarLvlSTR.progress = statsCurrProgress[0]
        binding.conTextView.text = "CON:" + "\n" + stats[1]
        binding.progressBarLvlCON.progress = statsCurrProgress[1]
        binding.dexTextView.text = "DEX:" + "\n" + stats[2]
        binding.progressBarLvlDEX.progress = statsCurrProgress[2]
        binding.intTextView.text = "INT:" + "\n" + stats[3]
        binding.progressBarLvlINT.progress = statsCurrProgress[3]
        binding.wisTextView.text = "WIS:" + "\n" + stats[4]
        binding.progressBarLvlWIS.progress = statsCurrProgress[4]
        binding.chaTextView.text = "CHA:" + "\n" + stats[5]
        binding.progressBarLvlCHA.progress = statsCurrProgress[5]

        return binding.root
    }

}