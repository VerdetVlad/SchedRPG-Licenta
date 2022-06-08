package com.vladv.questsched.tabs.fragments.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSettingsBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.social.subfragments.SocialSearchFragment

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        activity?.title = "Settings"
        MyFragmentManager.currentFragment = SettingsFragment()

        auxActivity = requireActivity()

        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.darkLightSwitch.text = "Night Mode"
                binding.darkLightSwitch.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.darkLightSwitch.text = "Light Mode"
                binding.darkLightSwitch.isChecked = false
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {}
        }


        binding.darkLightSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                binding.darkLightSwitch.text = "Night Mode"
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.darkLightSwitch.text = "Light Mode"
            }
        }




        binding.settingsChangeAvatarButton.setOnClickListener{
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                replace(R.id.flFragment, AvatarPickFragment())
                addToBackStack(null)
            }
        }




        return binding.root
    }

    companion object{
        lateinit var auxActivity:Activity
    }
}