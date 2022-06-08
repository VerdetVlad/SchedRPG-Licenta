package com.vladv.questsched.tabs.fragments.calendar

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.example.schedrpg.R
import com.vladv.questsched.tabs.MyFragmentManager
import com.vladv.questsched.tabs.fragments.questlistview.EditQuestFragment
import com.vladv.questsched.user.Quest
import com.vladv.questsched.user.User
import com.vladv.questsched.utilities.MyDate
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
        val typeImage = convtView.findViewById<ImageView>(R.id.dayQuestTypeImage)
        val description = convtView.findViewById<TextView>(R.id.taskItemDescription)
        val buttonEdit = convtView.findViewById<ImageButton>(R.id.dayEditQuestButton)



        name.text = quest!!.name
        difficulty.text = quest.difficultyStringValue()
        type.text = quest.typeStringValue()
        description.text = quest.description
        quest.typeImageValue().let { typeImage.setImageResource(it) }

        description.text = if(quest.description == "")  "No description" else quest.description

        val layout = convtView.findViewById<RelativeLayout>(R.id.calendarDayItemRelLayout)
        layout.setOnClickListener{

            MyFragmentManager.createQuestPopUp(quest,context)
        }


        buttonEdit.setOnClickListener{
            val questPosition = User().quests?.indexOf(quest)
            if (questPosition != null) {
                changeFragmentFromAdapter(CalendarDayFragment.auxActivity!!,quest,questPosition)
            }
        }


        return convtView
    }




    private fun changeFragmentFromAdapter(activity: FragmentActivity, quest:Quest, position: Int) {


        val fragmentManager = activity.supportFragmentManager
        fragmentManager.commit {
            setCustomAnimations(
                R.anim.fragment_fadein,
                R.anim.fragment_fadeout,
                R.anim.fragment_fadein,
                R.anim.fragment_fadeout
            )
            replace(R.id.flFragment, EditQuestFragment(quest, position))
            addToBackStack(null)
        }

    }
}