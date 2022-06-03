package com.vladv.questsched.tabs.fragments.questlistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.Quest

class QuestListAdapter( context: Context?, userArrayList: ArrayList<Quest>?) :
    ArrayAdapter<Quest?>(
        context!!, R.layout.fragment_quest_view_item, userArrayList!! as List<Quest?>
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convtView = convertView
        val quest = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_quest_view_item, parent, false)
        }

        val name = convtView!!.findViewById<TextView>(R.id.taskItemName)
        val difficulty = convtView.findViewById<TextView>(R.id.taskItemDifficulty)
        val type = convtView.findViewById<TextView>(R.id.taskItemType)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)
        val deleteButton = convtView.findViewById<Button>(R.id.abandonQuestButton)
        val editButton = convtView.findViewById<Button>(R.id.editQuestViewButton)

        name.text = quest!!.name
        difficulty.text = quest.difficultyStringValue()
        type.text = quest.typeStringValue()
        description.text = quest.description


        editButton.setOnClickListener {
            changeFragmentFromAdapter(QuestListFragment.auxActivity!!,quest,position)
        }


        deleteButton.setOnClickListener {
            QuestListFragment.removeItem(quest)
        }
        return convtView
    }


    fun changeFragmentFromAdapter(activity: FragmentActivity, quest:Quest, position: Int) {

        val fragmentManager = activity.supportFragmentManager
        fragmentManager.commit {
            setCustomAnimations(
                R.anim.fragment_fadein,
                R.anim.fragment_fadeout,
                R.anim.fragment_fadein,
                R.anim.fragment_fadeout
            )
            replace(MyFragmentManager.binding.flFragment.id,EditQuestFragment(quest, position))
            addToBackStack(null)
        }

    }






}