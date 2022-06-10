package com.vladv.questsched.tabs.fragments.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentSettingsAvatarPickBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialSearchFragment
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.AvatarList


class AvatarPickFragment : Fragment() {
    private var _binding: FragmentSettingsAvatarPickBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSettingsAvatarPickBinding.inflate(inflater, container, false)

        listAdapter = AvatarPickAdapter(requireContext(), AvatarList.avatarList)
        listView = binding.avatarList
        listView!!.adapter = listAdapter



        return binding.root
    }


    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null

        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        var listAdapter: AvatarPickAdapter? = null


    }

}