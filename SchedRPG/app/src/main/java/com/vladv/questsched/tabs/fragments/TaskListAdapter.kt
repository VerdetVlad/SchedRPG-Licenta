package com.vladv.questsched.tabs.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.schedrpg.R
import com.vladv.questsched.user.UserTask

class TaskListAdapter(context: Context?, userArrayList: ArrayList<UserTask>?) :
    ArrayAdapter<UserTask?>(
        context!!, R.layout.fragment_task_view_item, userArrayList!! as List<UserTask?>
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convtView = convertView
        val userTask = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_task_view_item, parent, false)
        }
        val name = convtView!!.findViewById<TextView>(R.id.taskItemName)
        val difficulty = convtView.findViewById<TextView>(R.id.taskItemDifficulty)
        val type = convtView.findViewById<TextView>(R.id.taskItemType)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)
        val deleteButton = convtView.findViewById<Button>(R.id.deleteTaskItem)
        name.text = userTask!!.name
        difficulty.text = userTask.dificultyStringValue()
        type.text = userTask.typeStringValue()
        description.text = userTask.description
        deleteButton.setOnClickListener {
            TaskListFragment.removeItem(userTask)
        }
        return convtView
    }
}