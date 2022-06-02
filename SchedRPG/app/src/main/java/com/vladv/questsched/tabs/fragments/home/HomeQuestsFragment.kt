package com.vladv.questsched.tabs.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentHomeQuestsBinding
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate
import com.vladv.questsched.utilities.Quest
import java.util.*
import kotlin.collections.ArrayList


class HomeQuestsFragment : Fragment() {

    private var _binding: FragmentHomeQuestsBinding? = null
    private val binding get() = _binding!!


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeQuestsBinding.inflate(inflater, container, false)

        val calend:Calendar = Calendar.getInstance()
        val date:MyDate = MyDate(calend)

        activity?.title = "Quest's today ${date.toStringDate()}"



        val user = User()
        val questList = user.quests?.filter{ quest ->
            quest.validDate(date)
        }
        val questArray = questList?.let { ArrayList<Quest>(it) }
        if(!questArray.isNullOrEmpty()) {
            binding.noQuestTextView.isVisible = false
            listAdapter = HomeQuestsAdapter(requireContext(), questArray)
            listView = binding.questListView
            listView!!.adapter = listAdapter
        }
        else
        {
            binding.noQuestTextView.isVisible = true
        }


        return binding.root

    }

    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: HomeQuestsAdapter? = null

    }
}