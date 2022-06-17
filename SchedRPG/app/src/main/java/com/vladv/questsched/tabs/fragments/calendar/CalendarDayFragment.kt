package com.vladv.questsched.tabs.fragments.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.schedrpg.databinding.FragmentCalendarDayBinding
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate
import java.util.*

class CalendarDayFragment : Fragment {

    private lateinit var date: MyDate

    constructor(){}

    constructor(date: MyDate) : super() {
        this.date = date
    }

    private var _binding: FragmentCalendarDayBinding? = null
    private val binding get() = _binding!!


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarDayBinding.inflate(inflater, container, false)

        auxActivity=activity
        MyFragmentManager.currentFragment = this

        return binding.root

    }

    override fun onStart() {
        super.onStart()

        if(!this::date.isInitialized) return

        activity?.title = "Quest from ${date.toStringDate()}"




        // bigger than current date
        if(date.compareDates(MyDate(Calendar.getInstance())))
        {


            if(User().quests.isNullOrEmpty()) {
                binding.noQuestTextView2.visibility = View.VISIBLE
                return
            }

            binding.noQuestTextView2.visibility = View.GONE

            val questList = User().quests?.filter{ quest ->
                quest.validDate(date)
            }
            val questArray = questList?.let { ArrayList(it) }
            listAdapter = CalendarDayAdapter(requireContext(), questArray)
            listView = binding.dayview
            listView!!.adapter = listAdapter
        }
        else//history
        {
            val quests = User().questHistory?.questHistoryMap?.get(date.toStringDateKey())
            if(quests.isNullOrEmpty()) {
                binding.noQuestTextView2.visibility = View.VISIBLE
                return
            }
            val questArray = ArrayList(quests)
            listAdapterHistory = CalendarDayHistoryAdapter(requireContext(), questArray)

            binding.noQuestTextView2.visibility = View.GONE
            listView = binding.dayview
            listView!!.adapter = listAdapterHistory


        }


    }

    companion object {

        var auxActivity: FragmentActivity? = null

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: CalendarDayAdapter? = null
        var listAdapterHistory: CalendarDayHistoryAdapter? = null

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(!this::date.isInitialized) return
        outState.putString("savedDate", date.toString())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            val dateString = savedInstanceState.getString("savedDate", "09/09/9999")
            date = MyDate(dateString)

        }
    }

}