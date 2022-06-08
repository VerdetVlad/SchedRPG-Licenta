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
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate

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

        return binding.root

    }

    override fun onStart() {
        super.onStart()

        if(!this::date.isInitialized) return

        activity?.title = "Quest from ${date.toStringDate()}"

        if(User().quests.isNullOrEmpty()) return


        val questList = User().quests?.filter{ quest ->
            quest.validDate(date)
        }
        val questArray = questList?.let { ArrayList<Quest>(it) }
        listAdapter = CalendarDayAdapter(requireContext(), questArray)
        listView = binding.dayview
        listView!!.adapter = listAdapter

    }

    companion object {
        private var user = User()

        var auxActivity: FragmentActivity? = null

        @SuppressLint("StaticFieldLeak")
        var listView: ListView? = null


        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var listAdapter: CalendarDayAdapter? = null

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