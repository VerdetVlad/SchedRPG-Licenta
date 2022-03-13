package com.vladv.questsched.tabs.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentCaldendayDayBinding

class CaldendayDayFragment : Fragment() {

    private var _binding: FragmentCaldendayDayBinding? = null
    private val binding get() = _binding!!


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaldendayDayBinding.inflate(inflater, container, false)

        activity?.title = ""

        val bundle = this.arguments

        if (bundle != null) {

            val date = bundle.getString("date")

            binding.dateTextView.text = "Quest from $date"
            activity?.title = "Quest from $date"
        }


        binding.goBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root

    }
}