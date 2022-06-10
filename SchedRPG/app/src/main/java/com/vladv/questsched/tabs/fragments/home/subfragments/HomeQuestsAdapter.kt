package com.vladv.questsched.tabs.fragments.home.subfragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.schedrpg.R
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User


class HomeQuestsAdapter(context: Context?, questList: ArrayList<Quest>?) :
    ArrayAdapter<Quest?>(context!!, R.layout.fragment_home_quests_item, questList!! as List<Quest?>)
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convtView = convertView
        val quest = getItem(position)
        if (convtView == null) {
            convtView =
                LayoutInflater.from(context).inflate(R.layout.fragment_home_quests_item, parent, false)
        }
        val name = convtView!!.findViewById<TextView>(R.id.taskItemName)
        val difficulty = convtView.findViewById<TextView>(R.id.taskItemDifficulty)
        val type = convtView.findViewById<TextView>(R.id.taskItemType)
        val typeImage = convtView.findViewById<ImageView>(R.id.homeQuestTypeImage)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)
        val buttonFinish = convtView.findViewById<ImageButton>(R.id.finishQuestButton)
        val buttonAbandon = convtView.findViewById<ImageButton>(R.id.abandonQuestButton)

        name.text = quest!!.name
        difficulty.text = quest.difficultyStringValue()
        type.text = quest.typeStringValue()
        description.text = quest.description
        quest.typeImageValue().let { typeImage.setImageResource(it) }
        description.text = if(quest.description == "")  "No description" else quest.description

        val layout = convtView.findViewById<RelativeLayout>(R.id.homeItemRelLayout)
        layout.setOnClickListener{

            MyFragmentManager.createQuestPopUp(quest,context)
        }

        buttonFinish.setOnClickListener{
            User().completeQuest(quest)
            User().addToQuestHistory(HomeQuestsFragment.date,quest,true)
            HomeQuestsFragment.removeItem(quest,"completed")
        }
        buttonAbandon.setOnClickListener{

            User().abandonQuest(quest)
            User().addToQuestHistory(HomeQuestsFragment.date,quest,false)
            HomeQuestsFragment.removeItem(quest,"abandoned")
        }

        return convtView
    }

}