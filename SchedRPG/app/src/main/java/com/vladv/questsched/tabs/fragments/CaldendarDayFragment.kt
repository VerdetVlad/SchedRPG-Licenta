package com.vladv.questsched.tabs.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.schedrpg.databinding.FragmentCaldendayDayBinding
import com.vladv.questsched.utilities.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate

class CaldendarDayFragment : Fragment() {

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
        val dateString = bundle!!.getString("date")
        val date = MyDate(dateString)
        activity?.title = "Quest from ${date.toStringDate()}"

        binding.goBackButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }


        val user = User()
        val questList = user.quests?.filter{ quest ->
            quest.initialDate == date.toStringDate() ||
            quest.repeat?.recurringFrequency == 1 ||
            (quest.repeat?.recurringFrequency == 2 && quest.repeat?.recurringDays?.get(date.weekDay!!-1) == true) ||
            (quest.repeat?.recurringFrequency == 3 && quest.getQuestDay() == date.day)

        }
        val questArray = questList?.let { ArrayList<Quest>(it) }
        listAdapter = CalendarDayAdapter(requireContext(), questArray)
        listView = binding.dayview
        listView!!.adapter = listAdapter

        

        return binding.root

    }

    companion object {
        private var user = User()

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: CalendarDayAdapter? = null

    }
}