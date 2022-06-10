package com.vladv.questsched.tabs.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentSettingsChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentSettingsChangePasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private lateinit var currentPassword:String
    private lateinit var newPassword:String
    private lateinit var newPasswordConfirm:String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsChangePasswordBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        binding.changePasswordButton.setOnClickListener {
            changePassword()
        }


        return binding.root
    }

    private fun changePassword() {

        if (binding.settingsCurrentPass.text.isNotEmpty() &&
            binding.settingsNewPassConfirm.text.isNotEmpty() &&
            binding.settingsNewPass.text.isNotEmpty()
        ) {

            currentPassword = binding.settingsCurrentPass.text.toString()
            newPassword = binding.settingsNewPass.text.toString()
            newPasswordConfirm = binding.settingsNewPassConfirm.text.toString()


            if (newPasswordConfirm == newPassword) {

                val user = auth.currentUser
                if (user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, currentPassword)


                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(context, "Re-Authentication successful.", Toast.LENGTH_SHORT).show()
                                user.updatePassword(newPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Password changed successfully.", Toast.LENGTH_SHORT).show()
                                            auth.signOut()
                                        }
                                    }

                            } else {
                                Toast.makeText(context, "Re-Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }

            } else {
                Toast.makeText(context, "Password mismatching.", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "Please enter all the fields.", Toast.LENGTH_SHORT).show()
        }

    }
}