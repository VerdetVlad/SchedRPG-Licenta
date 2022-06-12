package com.vladv.questsched.tabs.fragments.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.schedrpg.R
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.utilities.FinishedQuestData


class CalendarDayHistoryAdapter(context: Context?, questList: ArrayList<FinishedQuestData>?) :
    ArrayAdapter<FinishedQuestData?>(context!!, R.layout.fragment_calendar_day_item_history, questList!! as List<FinishedQuestData?>)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convtView = convertView
        val quest = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_calendar_day_item_history, parent, false)
        }
        val name = convtView!!.findViewById<TextView>(R.id.taskItemName)
        val difficulty = convtView.findViewById<TextView>(R.id.taskItemDifficulty)
        val type = convtView.findViewById<TextView>(R.id.taskItemType)
        val typeImage = convtView.findViewById<ImageView>(R.id.dayQuestTypeImage)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)

        val completed = convtView.findViewById<ImageView>(R.id.questHistoryCompletedImage)
        val abandoned = convtView.findViewById<ImageView>(R.id.questHistoryFailedImage)



        name.text = quest!!.name
        difficulty.text = quest.difficulty
        type.text = quest.type
        description.text = quest.description
        quest.typeImageValue().let { typeImage.setImageResource(it) }

        description.text = if(quest.description == "")  "No description" else quest.description

        if(quest.completed) {
            completed.visibility = View.VISIBLE
            abandoned.visibility = View.GONE
        }
        else {
            abandoned.visibility = View.VISIBLE
            completed.visibility = View.GONE
        }

        val layout = convtView.findViewById<RelativeLayout>(R.id.calendarDayItemRelLayout)
        layout.setOnClickListener{

            MyFragmentManager.createQuestPopUp(quest,context)
        }



        return convtView
    }



}