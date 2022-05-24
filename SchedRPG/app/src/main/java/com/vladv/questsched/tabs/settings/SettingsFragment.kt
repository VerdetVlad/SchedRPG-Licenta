package com.vladv.questsched.tabs.settings

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.applandeo.materialcalendarview.EventDay
import com.example.schedrpg.R
import com.example.schedrpg.databinding.ActivityPickAvatarBinding
import com.example.schedrpg.databinding.FragmentCalendarBinding
import com.example.schedrpg.databinding.FragmentSettingsBinding
import com.vladv.questsched.tabs.MyFragmentManager
import java.util.*

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


        if(MyFragmentManager().isNightMode()) {
            binding.darkLightSwitch.text = "Night Mode"
            binding.darkLightSwitch.isChecked = true
        }
        else{
            binding.darkLightSwitch.text = "Light Mode"
            binding.darkLightSwitch.isChecked = false
        }
        return binding.root
    }

}