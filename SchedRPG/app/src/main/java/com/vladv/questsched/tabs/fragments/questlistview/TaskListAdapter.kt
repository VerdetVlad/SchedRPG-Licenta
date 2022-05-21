package com.vladv.questsched.tabs.fragments.questlistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.schedrpg.R
import com.vladv.questsched.utilities.Quest

class TaskListAdapter(context: Context?, userArrayList: ArrayList<Quest>?) :
    ArrayAdapter<Quest?>(
        context!!, R.layout.fragment_task_view_item, userArrayList!! as List<Quest?>
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convtView = convertView
        val quest = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_task_view_item, parent, false)
        }
        val name = convtView!!.findViewById<TextView>(R.id.taskItemName)
        val difficulty = convtView.findViewById<TextView>(R.id.taskItemDifficulty)
        val type = convtView.findViewById<TextView>(R.id.taskItemType)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)
        val deleteButton = convtView.findViewById<Button>(R.id.abandonQuestButton)
        name.text = quest!!.name
        difficulty.text = quest.dificultyStringValue()
        type.text = quest.typeStringValue()
        description.text = quest.description
        deleteButton.setOnClickListener {
            TaskListFragment.removeItem(quest)
        }
        return convtView
    }
}