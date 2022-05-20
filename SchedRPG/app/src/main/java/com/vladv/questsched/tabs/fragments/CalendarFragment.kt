package com.vladv.questsched.tabs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentCalendarBinding
import com.vladv.questsched.utilities.MyDate


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        activity?.title = "Quest Calendar"






        val transaction = activity?.supportFragmentManager?.beginTransaction()
        binding.calendarView.setOnDayClickListener(OnDayClickListener { eventDay ->
            val clickedDay= eventDay.calendar.time
            val words = clickedDay.toString().split("\\s".toRegex()).toTypedArray()
            val date = MyDate(words[0],words[2],words[1],words[5])
            val msg = date.toString()

            val bundle = Bundle()
            bundle.putString("date",msg)

            val calendarDayFrag = CaldendarDayFragment()
            calendarDayFrag.arguments = bundle

            if(transaction != null) {
                transaction.replace(R.id.flFragment, calendarDayFrag)
                transaction.addToBackStack("")
                transaction.commit()
            }
        })



        return binding.root
    }
}
