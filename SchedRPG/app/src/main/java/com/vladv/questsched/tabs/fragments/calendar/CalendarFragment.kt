package com.vladv.questsched.tabs.fragments.calendar


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.EventDay
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentCalendarBinding
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate
import java.util.*


class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var events: MutableList<EventDay> = ArrayList()
    private var calendar :Calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        activity?.title = "Quest Calendar"

        setCalendarEvents()

        binding.calendarView.setOnForwardPageChangeListener{
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1)
            setCalendarEvents()
        }

        binding.calendarView.setOnPreviousPageChangeListener{
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1)
            setCalendarEvents()
        }

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        binding.calendarView.setOnDayClickListener { eventDay ->
            val clickedDay = eventDay.calendar.time
            val words = clickedDay.toString().split("\\s".toRegex()).toTypedArray()
            val date = MyDate(words[0], words[2], words[1], words[5])
            val msg = date.toString()

            val bundle = Bundle()
            bundle.putString("date", msg)

            val calendarDayFrag = CaldendarDayFragment()
            calendarDayFrag.arguments = bundle

            if (transaction != null) {
                transaction.replace(R.id.flFragment, calendarDayFrag)
                transaction.addToBackStack("")
                transaction.commit()
            }
        }



        return binding.root
    }


    private fun setCalendarEvents(){

        val lastMonthDay = calendar.getActualMaximum(Calendar.DATE)
        val startMonthDay = calendar.get(Calendar.DAY_OF_MONTH)
        val auxDate = MyDate(
            myDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),
            calendar.get(Calendar.DAY_OF_MONTH),
            (calendar.get(Calendar.MONTH)+1),
            calendar.get(Calendar.YEAR))

        for(i in startMonthDay..lastMonthDay)
        {
            for(quest in User().quests!!) {
                if(quest.validDate(auxDate)) {
                    val newTime = calendar.clone() as Calendar
                    events.add(EventDay(newTime, R.drawable.calendar_event_notification,Color.parseColor("#0EAB08")))
                    break
                }
            }
            auxDate.increaseDayByOne()
            calendar.add(Calendar.DAY_OF_MONTH,1)


        }
        binding.calendarView.setEvents(events)

    }

    private fun myDayOfWeek(dayOfWeek:Int) :Int
    {
        return if(dayOfWeek == 1) 7
        else (dayOfWeek - 1)
    }


}
