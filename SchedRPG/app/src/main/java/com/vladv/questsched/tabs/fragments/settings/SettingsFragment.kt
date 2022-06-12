package com.vladv.questsched.tabs.fragments.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentSettingsBinding
import com.example.schedrpg.databinding.PopUpAboutBinding
import com.example.schedrpg.databinding.PopUpAssetsBinding
import com.example.schedrpg.databinding.PopUpProfileDescriptionBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.FirebaseUtilities


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
                User.setNighMode(true)
                FirebaseUtilities().updateUserData(requireActivity(),"Night Mode Saved", "Nigh Mode Save Failed")
                binding.darkLightSwitch.text = "Night Mode"
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                User.setNighMode(false)
                FirebaseUtilities().updateUserData(requireActivity(),"Light Mode Saved", "Light Mode Save Failed")
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
            }
        }

        binding.settingsChangePasswordButton.setOnClickListener{
            parentFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout,
                    R.anim.fragment_fadein,
                    R.anim.fragment_fadeout
                )
                replace(R.id.flFragment, ChangePasswordFragment())
            }
        }

        binding.settingsChangePublicDescription.setOnClickListener {
            createNewDescriptionPopUp()
        }

        binding.settingsAboutButton.setOnClickListener {
            createAboutPopUp()
        }

        binding.settingsAssetsButton.setOnClickListener {
            createAssetsPopUp()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    fun createNewDescriptionPopUp()
    {
        val dialog: AlertDialog?
        val dialogBuilder: AlertDialog.Builder = context.let { AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle) }
        val inflater = LayoutInflater.from(context)
        val auxBinding = PopUpProfileDescriptionBinding.inflate(inflater)
        auxBinding.settingProfileDescriptionEdit.setText(User().profileDescription)


        dialogBuilder.setView(auxBinding.root)

        dialog = dialogBuilder.create()

        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes ?: return)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams


        auxBinding.settingsNewDescriptionButton.setOnClickListener{
            User.setDescription(auxBinding.settingProfileDescriptionEdit.text.toString())
            FirebaseUtilities().updateUserData()
            dialog.dismiss()
        }


    }


    fun createAboutPopUp()
    {
        val dialog: AlertDialog?
        val dialogBuilder: AlertDialog.Builder = context.let { AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle) }
        val inflater = LayoutInflater.from(context)
        val auxBinding = PopUpAboutBinding.inflate(inflater)


        dialogBuilder.setView(auxBinding.root)

        dialog = dialogBuilder.create()

        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes ?: return)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams


        auxBinding.closeAboutButton.setOnClickListener{

            dialog.dismiss()
        }


    }

    fun createAssetsPopUp()
    {
        val dialog: AlertDialog?
        val dialogBuilder: AlertDialog.Builder = context.let { AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle) }
        val inflater = LayoutInflater.from(context)
        val auxBinding = PopUpAssetsBinding.inflate(inflater)


        dialogBuilder.setView(auxBinding.root)

        dialog = dialogBuilder.create()

        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes ?: return)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams



        auxBinding.closeAboutButton.setOnClickListener{

            dialog.dismiss()
        }


    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var auxActivity: Activity

    }
}