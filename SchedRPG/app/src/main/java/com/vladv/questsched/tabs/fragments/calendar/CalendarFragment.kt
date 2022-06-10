package com.vladv.questsched.tabs.fragments.calendar


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.applandeo.materialcalendarview.EventDay
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentCalendarBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.home.HomeNavFragment
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

        MyFragmentManager.currentFragment = this

        calendar.set(Calendar.DAY_OF_MONTH,1)
        setCalendarEvents()

        binding.calendarView.setOnForwardPageChangeListener{
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1)
            setCalendarEvents()
        }

        binding.calendarView.setOnPreviousPageChangeListener{
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-2,1)
            setCalendarEvents()
        }


        binding.calendarView.setOnDayClickListener { eventDay ->
            val clickedDay = eventDay.calendar.time
            val words = clickedDay.toString().split("\\s".toRegex()).toTypedArray()
            val date = MyDate(words[0], words[2], words[1], words[5])

            if(date == MyDate(Calendar.getInstance()))
            {
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(R.id.flFragment, HomeNavFragment())
                }
            }
            else
            {
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout,
                        R.anim.fragment_fadein,
                        R.anim.fragment_fadeout
                    )
                    replace(R.id.flFragment, CalendarDayFragment(date))
                }
            }




        }



        return binding.root
    }


    private fun setCalendarEvents(){

        if(User().quests.isNullOrEmpty()) return

        val lastMonthDay = calendar.getActualMaximum(Calendar.DATE)

        val auxDate = MyDate(
            myDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK)),
            calendar.get(Calendar.DAY_OF_MONTH),
            (calendar.get(Calendar.MONTH)+1),
            calendar.get(Calendar.YEAR))

        val highlightDays: MutableList<Calendar> = ArrayList()

        for(i in 1..lastMonthDay) {

            if(auxDate == MyDate(Calendar.getInstance()))
            {
                if(!User().lastLogIn?.unfinishedQuests.isNullOrEmpty())
                    {
                        val newTime = calendar.clone() as Calendar
                        events.add(EventDay(newTime, R.drawable.calendar_event_notification3))
                    }
            }
            else if(!auxDate.compareDates(MyDate(Calendar.getInstance()))){

                if(User().questHistory != null)
                    if(!User().questHistory!!
                            .questHistoryMap[auxDate.toStringDateKey()].isNullOrEmpty())
                    {
                        val newTime = calendar.clone() as Calendar
                        events.add(EventDay(newTime, R.drawable.calendar_event_notification2))
                    }
            }
            else {
                for (quest in User().quests!!) {
                    if (quest.validDate(auxDate)) {
                        val newTime = calendar.clone() as Calendar
                        events.add(EventDay(newTime, R.drawable.calendar_event_notification))
                        highlightDays.add(newTime)
                        break
                    }
                }
            }
            auxDate.increaseDayByOne()
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }



        binding.calendarView.setEvents(events)
        binding.calendarView.setHighlightedDays(highlightDays)

    }

    private fun myDayOfWeek(dayOfWeek:Int) :Int
    {
        return if(dayOfWeek == 1) 7
        else (dayOfWeek - 1)
    }


}
