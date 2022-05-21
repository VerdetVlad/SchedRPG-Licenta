package com.vladv.questsched.tabs.fragments.calendar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.schedrpg.R
import com.example.schedrpg.databinding.FragmentCaldendayDayBinding
import com.example.schedrpg.databinding.FragmentCalendarDayItemBinding
import com.vladv.questsched.utilities.MyDate
import com.vladv.questsched.utilities.Quest
import com.vladv.questsched.utilities.Recurrence


class CalendarDayAdapter(context: Context?, questList: ArrayList<Quest>?) :
    ArrayAdapter<Quest?>(context!!, R.layout.fragment_calendar_day_item, questList!! as List<Quest?>)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convtView = convertView
        val quest = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_calendar_day_item, parent, false)
        }
        val name = convtView!!.findViewById<TextView>(R.id.taskItemName)
        val difficulty = convtView.findViewById<TextView>(R.id.taskItemDifficulty)
        val type = convtView.findViewById<TextView>(R.id.taskItemType)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)
        val buttonFinish = convtView.findViewById<Button>(R.id.finishQuestButton)
        val buttonAbandon = convtView.findViewById<Button>(R.id.abandonQuestButton)
        val layout = convtView.findViewById<LinearLayout>(R.id.questItemDayLinLayout)

        name.text = quest!!.name
        difficulty.text = quest.dificultyStringValue()
        type.text = quest.typeStringValue()
        description.text = quest.description

        buttonFinish.setOnClickListener{
            layout.background.setTint(Color.GREEN)
            test()
        }
        buttonAbandon.setOnClickListener{
            layout.background.setTint(Color.RED)
            test()
        }

        return convtView
    }

    private fun test() {
        val rec = Recurrence(1,null, MyDate(1,1,1,1))
        val q = Quest("test","","10/10/2022", rec,1,1)
        add(q)
    }
}